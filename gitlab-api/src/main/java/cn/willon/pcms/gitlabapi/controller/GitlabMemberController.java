package cn.willon.pcms.gitlabapi.controller;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProjectMember;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * GitlabMemberController
 *
 * @author Willon
 * @since 2019-01-20
 */
@RestController
public class GitlabMemberController {


    @Resource
    private GitlabAPI gitlabAPI;


    @GetMapping("/members")
    public List members(String projectId) {

        List<GitlabProjectMember> projectMembers = null;
        try {
            projectMembers = gitlabAPI.getProjectMembers(projectId);
        } catch (IOException e) {
            return null;
        }
        return projectMembers;

    }

}
