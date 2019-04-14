package cn.willon.pcms.pcmsmidware.controller.deploy;

import cn.willon.pcms.pcmsmidware.service.DeployService;
import cn.willon.pcms.pcmsmidware.service.PackService;
import cn.willon.pcms.pcmsmidware.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
     * 打包
     *
     * @param hostname   主机名
     * @param branchName 分支名
     * @param env        环境
     */
    @GetMapping("/pack/{hostname}/{branchName}")
    public Result pack(@PathVariable String hostname, @PathVariable String branchName, @RequestParam(name = "env") Integer env) {
        boolean pack = packService.pack(hostname, branchName, env);
        return Result.successResult(pack);
    }


    /**
     * 部署
     *
     * @param hostname   主机名
     * @param branchName 分支名
     */
    @GetMapping("/deploy{hostname}/{branchName}")
    public void deploy(@PathVariable String hostname, @PathVariable String branchName) {
        deployService.deploy(hostname, branchName);
    }
}
