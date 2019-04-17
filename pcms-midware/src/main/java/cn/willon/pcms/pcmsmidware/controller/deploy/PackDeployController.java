package cn.willon.pcms.pcmsmidware.controller.deploy;

import cn.willon.pcms.pcmsmidware.domain.DeployCondition;
import cn.willon.pcms.pcmsmidware.service.DeployService;
import cn.willon.pcms.pcmsmidware.service.PackService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * PackController
 * 工程打包部署Controller
 *
 * @author Willon
 * @since 2019-04-14
 */
@RestController
public class PackDeployController {


    @Resource
    PackService packService;

    @Resource
    DeployService deployService;

    /**
     * 部署
     */
    @PostMapping("/deploy")
    public void deploy(@RequestBody DeployCondition deployCondition) {
        String hostname = deployCondition.getHostname();
        String branchName = deployCondition.getBranchName();
        String env = deployCondition.getEnv();
        boolean pack = packService.pack(hostname, branchName, env);
        if (pack) {
            deployService.deploy(hostname, branchName);
        }
    }
}
