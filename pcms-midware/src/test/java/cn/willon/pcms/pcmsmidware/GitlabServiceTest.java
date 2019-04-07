package cn.willon.pcms.pcmsmidware;

import cn.willon.pcms.pcmsmidware.domain.bean.Project;
import cn.willon.pcms.pcmsmidware.domain.bean.User;
import cn.willon.pcms.pcmsmidware.service.GitlabService;
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
    GitlabService gitlabService;

    @Test
    public void queryProjects() throws IOException {
        List<Project> projects = gitlabService.findAllProjects();
        System.out.println(JSON.toJSONString(projects));
    }

    @Test
    public void queryMember() throws IOException {

        List<User> allUser = gitlabService.findAllUser();
        System.out.println(JSON.toJSONString(allUser));
    }



    @Test
    public void  branch() throws IOException {

        gitlabService.deleteBranch(3,"test1");

    }
}
