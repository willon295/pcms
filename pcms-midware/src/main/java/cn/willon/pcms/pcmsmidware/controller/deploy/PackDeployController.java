package cn.willon.pcms.pcmsmidware.controller.deploy;

import cn.willon.pcms.pcmsmidware.service.DeployService;
import cn.willon.pcms.pcmsmidware.service.PackService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
     *
     * @param hostname   主机名
     * @param branchName 分支名
     */
    @GetMapping("/deploy/{env}/{hostname}/{branchName}/")
    public void deploy(@PathVariable String env, @PathVariable String hostname, @PathVariable String branchName) {
        boolean pack = packService.pack(hostname, branchName, env);
        if (pack) {
            deployService.deploy(hostname, branchName);
        }
    }
}
