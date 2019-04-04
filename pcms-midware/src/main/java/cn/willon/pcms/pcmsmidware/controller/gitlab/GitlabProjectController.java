package cn.willon.pcms.pcmsmidware.controller.gitlab;

import org.gitlab.api.GitlabAPI;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * GitlabProjectController
 *
 * @author Willon
 * @since 2019-01-20
 */
@RestController
public class GitlabProjectController {

    @Resource
    private GitlabAPI gitlabAPI;

    @GetMapping("/projects")
    public List projects() throws IOException {
        return gitlabAPI.getProjects();
    }

}
