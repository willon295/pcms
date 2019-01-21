package cn.willon.pcms.gitlabapi.conf;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * GitlabConfTest
 *
 * @author Willon
 * @since 2019-01-20
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class GitlabConfTest {

    @Resource
    private GitlabAPI gitlabAPI;


    @Test
    public void getConnect() throws IOException {

        List<GitlabProject> ownedProjects = gitlabAPI.getOwnedProjects();
        List<GitlabGroup> groups = gitlabAPI.getGroups();
        for (GitlabProject ownedProject : ownedProjects) {
            System.out.println(ownedProject.getId() + ": " + ownedProject.getName());
        }

        for (GitlabGroup group : groups) {

            System.out.println(group.getId() + ": " + group.getName());
        }
    }
}