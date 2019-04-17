package cn.willon.pcms.pcmsmidware.service;

import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import cn.willon.pcms.pcmsmidware.domain.constrains.DevStatusEnums;
import cn.willon.pcms.pcmsmidware.domain.constrains.PubStatusEnums;
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


    private static final String TOMCAT_PORT = "8080";
    private static final String MASTER = "master";
    private static final int TIMEOUT = 10;
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
        Integer kvmId = kvm.getKvmId();
        Integer changeId = kvm.getChangeId();
        Integer projectId = kvm.getProjectId();
        String projectName = kvm.getProjectName();
        String ip;
        if (MASTER.equals(branchName)) {
            ip = changeService.getPublishProjectIp(changeId, projectId);
        } else {
            ip = kvm.getIp();
        }
        // 远程拷贝并且部署App
        try {
            deployBashExecutor.deploy(projectName, branchName, ip);
        } catch (Exception e) {
            if (MASTER.equals(branchName)) {
                changeService.updateProjectStatus(changeId, projectId, PubStatusEnums.DEPLOY_FAIL.getStatus());
            } else {
                kvmService.updateDevStatus(kvmId, DevStatusEnums.DEPLOY_FAIL.getStatus());
            }
        }


        // 检查是否有部署成功
        boolean run = deployBashExecutor.isReachable(ip, TOMCAT_PORT, TIMEOUT);

        if (run) {
            if (MASTER.equals(branchName)) {
                changeService.updateProjectStatus(changeId, projectId, PubStatusEnums.RUNNING.getStatus());
            } else {
                kvmService.updateDevStatus(kvmId, DevStatusEnums.RUNNING.getStatus());
            }
        } else {
            if (MASTER.equals(branchName)) {
                changeService.updateProjectStatus(changeId, projectId, PubStatusEnums.DEPLOY_FAIL.getStatus());
            } else {
                kvmService.updateDevStatus(kvmId, DevStatusEnums.DEPLOY_FAIL.getStatus());
            }

        }
    }

}
