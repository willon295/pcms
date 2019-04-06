package cn.willon.pcms.pcmsmidware.service;

import com.google.common.collect.Lists;

import cn.willon.pcms.pcmsmidware.domain.bean.Project;
import cn.willon.pcms.pcmsmidware.domain.bean.User;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GitalbService
 *
 * @author Willon
 * @since 2019-04-06
 */
@Service
public class GitlabService {

    @Resource
    private GitlabAPI gitlabAPI;


    /**
     * 获取所有 gitlab用户信息
     *
     * @return 用户列表
     * @throws IOException 异常
     */
    public List<User> findAllUser() throws IOException {
        List<GitlabUser> users = gitlabAPI.getUsers();
        List<User> collect = users.stream().map(u -> {
            User user = new User();
            user.setUserId(u.getId());
            user.setUsername(u.getUsername());
            user.setRealName(u.getName());
            return user;
        }).collect(Collectors.toList());
        return collect;
    }


    public List<Project> findAllProjects() throws IOException {
        List<GitlabProject> projects = gitlabAPI.getProjects();
        List<Project> collect = projects.stream().map(p -> {
            Project project = new Project();
            project.setProjectId(p.getId());
            project.setProjectName(p.getName());
            project.setDevelopers(Lists.newArrayList());
            return project;

        }).collect(Collectors.toList());
        return collect;
    }

}
