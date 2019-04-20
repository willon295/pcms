package cn.willon.pcms.pcmsmidware.service;

import cn.willon.pcms.pcmsmidware.domain.DeployCondition;
import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import cn.willon.pcms.pcmsmidware.domain.constrains.DevStatusEnums;
import cn.willon.pcms.pcmsmidware.domain.constrains.PubStatusEnums;
import cn.willon.pcms.pcmsmidware.executor.DeployBashExecutor;
import cn.willon.pcms.pcmsmidware.util.ServerUtil;
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


    private static final Integer TOMCAT_PORT = 8080;
    private static final String MASTER = "master";
    private static final int TIMEOUT = 10000;
    @Resource
    DeployBashExecutor deployBashExecutor;


    @Resource
    KvmService kvmService;


    @Resource
    ChangeService changeService;

    /**
     * 部署
     */
    public boolean deploy(DeployCondition condition) {

        String branchName = condition.getBranchName();
        if (MASTER.equals(branchName)) {
            return deployPublish(condition);
        } else {
            return deployDev(condition);
        }

    }


    private boolean deployDev(DeployCondition condition) {
        String branchName = condition.getBranchName();
        String hostname = condition.getHostname();
        Kvm kvm = kvmService.findByHostname(hostname);
        Integer kvmId = kvm.getKvmId();
        String projectName = kvm.getProjectName();
        String ip = kvm.getIp();
        // 远程拷贝并且部署App
        try {
            deployBashExecutor.deploy(projectName, branchName, ip);
        } catch (Exception e) {
            kvmService.updateDevStatus(kvmId, DevStatusEnums.DEPLOY_FAIL.getStatus());
            return false;
        }
        // 检查是否有部署成功
        boolean run = ServerUtil.isReachable(ip, TOMCAT_PORT, TIMEOUT);
        if (run) {
            kvmService.updateDevStatus(kvmId, DevStatusEnums.RUNNING.getStatus());
            return true;
        } else {
            kvmService.updateDevStatus(kvmId, DevStatusEnums.DEPLOY_FAIL.getStatus());
            return false;
        }
    }

    private boolean deployPublish(DeployCondition condition) {
        String hostname = condition.getHostname();
        Integer changeId = condition.getChangeId();
        String branchName = condition.getBranchName();
        Integer projectId = kvmService.findPublishProjectIdByHostname(hostname);
        String ip = kvmService.findPublishServerIpByHostname(hostname);
        try {
            deployBashExecutor.deploy(hostname, branchName, ip);
        } catch (Exception e) {
            changeService.updateProjectStatus(changeId, projectId, PubStatusEnums.DEPLOY_FAIL.getStatus());
            return false;
        }
        // 检查是否有部署成功

        boolean run = ServerUtil.isReachable(ip, TOMCAT_PORT, TIMEOUT);
        if (run) {
            changeService.updateProjectStatus(changeId, projectId, PubStatusEnums.RUNNING.getStatus());
            return true;
        } else {
            changeService.updateProjectStatus(changeId, projectId, PubStatusEnums.DEPLOY_FAIL.getStatus());
            return false;
        }
    }
}
