package cn.willon.pcms.pcmsmidware.controller.deploy;

import cn.willon.pcms.pcmsmidware.domain.DeployCondition;
import cn.willon.pcms.pcmsmidware.service.DeployService;
import cn.willon.pcms.pcmsmidware.service.PackService;
import cn.willon.pcms.pcmsmidware.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * PackController
 * 工程打包部署Controller
 *
 * @author Willon
 * @since 2019-04-14
 */
@Slf4j
@CrossOrigin(allowedHeaders = "*", maxAge = 3600)
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
    public Result deploy(@RequestBody DeployCondition deployCondition) {

        boolean pack = packService.pack(deployCondition);
        if (pack) {
            deployService.deploy(deployCondition);
        }
        return Result.successResult("success");


    }
}
