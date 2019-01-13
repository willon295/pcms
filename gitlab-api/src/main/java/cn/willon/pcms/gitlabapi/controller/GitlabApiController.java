package cn.willon.pcms.gitlabapi.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GitlabApiController
 *
 * @author Willon
 * @since 2019-01-13
 */
@RestController
public class GitlabApiController {

    @Value("${gitlab.accessToken}")
    private String accessToken;

    @GetMapping("/get")
    public String access() {

        // TODO 获取gitlab api
        return "";
    }

}
