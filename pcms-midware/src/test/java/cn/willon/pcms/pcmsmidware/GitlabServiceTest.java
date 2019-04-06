package cn.willon.pcms.pcmsmidware;

import com.alibaba.fastjson.JSON;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * GitlabServiceTest
 *
 * @author Willon
 * @since 2019-04-06
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GitlabServiceTest {


    @Resource
    GitlabAPI gitlabAPI;

    @Test
    public void queryProjects() throws IOException {
        List<GitlabProject> projects = gitlabAPI.getProjects();
        for (GitlabProject project : projects) {
            Integer id = project.getId();
            String name = project.getName();
            System.out.println(id + ":" + name);
        }
    }

    @Test
    public void queryMember() throws IOException {
        List<GitlabUser> users = gitlabAPI.getUsers();
        System.out.println(JSON.toJSONString(users));

    }
}
