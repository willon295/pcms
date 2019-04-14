package cn.willon.pcms.pcmsmidware.service;

import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import cn.willon.pcms.pcmsmidware.executor.DeployBashExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * DeployService
 *
 * @author Willon
 * @since 2019-04-14
 */
@Service
public class DeployService {


    public static final String MASTER = "master";
    @Resource
    DeployBashExecutor deployBashExecutor;


    @Resource
    KvmService kvmService;


    @Resource
    ChangeService changeService;

    /**
     * 部署
     *
     * @param hostname   主机名
     * @param branchName 分支名
     */
    public void deploy(String hostname, String branchName) {

        Kvm kvm = kvmService.findByHostname(hostname);
        Integer changeId = kvm.getChangeId();
        Integer projectId = kvm.getProjectId();
        String projectName = kvm.getProjectName();
        String ip;
        if (MASTER.equals(branchName)) {
            ip = changeService.getPublishProjectIp(changeId, projectId);
        } else {
            ip = kvm.getIp();
        }
        deployBashExecutor.scpFile(ip, projectName);
        deployBashExecutor.remoteDeploy(ip, projectName);
    }
}
