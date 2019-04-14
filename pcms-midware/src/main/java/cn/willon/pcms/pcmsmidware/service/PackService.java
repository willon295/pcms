package cn.willon.pcms.pcmsmidware.service;

import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import cn.willon.pcms.pcmsmidware.domain.constrains.DevStatusEnums;
import cn.willon.pcms.pcmsmidware.domain.constrains.EnvType;
import cn.willon.pcms.pcmsmidware.domain.constrains.PubStatusEnums;
import cn.willon.pcms.pcmsmidware.executor.PackBashExecutor;
import lombok.extern.slf4j.Slf4j;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * PackService
 *
 * @author Willon
 * @since 2019-04-14
 */
@Slf4j
@Service
public class PackService {

    private static final String MASTER = "master";
    private static final String PUBLISH = "publish";
    private static final String DEV = "dev";


    @Resource
    PackBashExecutor packBashExecutor;
    @Resource
    KvmService kvmService;

    @Resource
    GitlabAPI gitlabAPI;

    @Resource
    ChangeService changeService;


    private void packDev(String hostname, String branchName) throws IOException {
        Kvm kvm = kvmService.findByHostname(hostname);
        Integer projectId = kvm.getProjectId();
        GitlabProject project = gitlabAPI.getProject(projectId);
        String projectName = project.getName();
        String sshUrl = project.getSshUrl();
        packBashExecutor.gitClone(sshUrl, branchName, projectName);
        packBashExecutor.mvnPackage(projectName, DEV);
        packBashExecutor.movePackageAndRemoveTmp(projectName);
    }


    private void packPub(String hostname, String branchName) throws IOException {
        Kvm kvm = kvmService.findByHostname(hostname);
        Integer changeId = kvm.getChangeId();
        Integer projectId = kvm.getProjectId();
        GitlabProject project = gitlabAPI.getProject(projectId);
        String projectName = project.getName();
        String sshUrl = project.getSshUrl();
        // 判断是否有权限
        if (changeService.hasProjectPublishPermission(projectId, changeId)) {
            packBashExecutor.gitClone(sshUrl, MASTER, projectName);
            packBashExecutor.gitMerge(projectName, branchName);
            packBashExecutor.mvnPackage(projectName, PUBLISH);
            packBashExecutor.movePackageAndRemoveTmp(projectName);
        } else {
            log.error("没有操作权限");
        }
    }


    public boolean pack(String hostname, String branchName, Integer env) {
        if (env.equals(EnvType.DEV)) {
            try {
                packDev(hostname, branchName);
                return true;
            } catch (IOException e) {
                Kvm byHostname = kvmService.findByHostname(hostname);
                kvmService.updateDevStatus(byHostname.getKvmId(), DevStatusEnums.PACK_FAIL.getStatus());
                return false;
            }

        } else if (env.equals(EnvType.PUBLISH)) {
            try {
                packPub(hostname, branchName);
                return true;
            } catch (IOException e) {
                Kvm kvm = kvmService.findByHostname(hostname);
                Integer changeId = kvm.getChangeId();
                Integer projectId = kvm.getProjectId();
                changeService.updateProjectStatus(changeId, projectId, PubStatusEnums.PACK_FAIL.getStatus());
                return false;
            }
        }
        return false;

    }
}
