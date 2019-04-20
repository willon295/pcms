package cn.willon.pcms.pcmsmidware.controller.gitlab;

import cn.willon.pcms.pcmsmidware.service.GitlabService;
import cn.willon.pcms.pcmsmidware.util.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * GitlabController
 *
 * @author Willon
 * @since 2019-04-06
 */
@CrossOrigin(allowedHeaders = "*"  ,maxAge = 3600)
@RestController
public class GitlabController {


    @Resource
    private GitlabService gitlabService;


    @GetMapping("/gitlab/users")
    public Result users() {
        try {
            return Result.successResult(gitlabService.findAllUser());
        } catch (IOException e) {
            return Result.failResult(e);
        }
    }

    @GetMapping("/projects")
    public Result projects() {
        try {
            return Result.successResult(gitlabService.findAllProjects());
        } catch (IOException e) {
            return Result.failResult(e);
        }
    }
}
