package cn.willon.pcms.pcmsmidware.controller.change;

import cn.willon.pcms.pcmsmidware.domain.bean.Changes;
import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import cn.willon.pcms.pcmsmidware.domain.bean.Project;
import cn.willon.pcms.pcmsmidware.domain.dto.SaveChangeDto;
import cn.willon.pcms.pcmsmidware.service.ChangeService;
import cn.willon.pcms.pcmsmidware.service.GitlabService;
import cn.willon.pcms.pcmsmidware.service.KvmService;
import cn.willon.pcms.pcmsmidware.service.UserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ChangeController
 *
 * @author Willon
 * @since 2019-04-06
 */
@Slf4j
@CrossOrigin(allowedHeaders = "*", maxAge = 3600)
@RestController
public class ChangeController {


    @Resource
    private ChangeService changeService;

    @Resource
    private KvmService kvmService;

    @Resource
    private UserService userService;
    @Resource
    private GitlabService gitlabService;

    /**
     * 添加一个变更
     *
     * @param dto 数据
     */
    @PostMapping(value = "/change")
    public void saveChange(@RequestBody SaveChangeDto dto) {
        changeService.saveChange(dto);
        // 开始异步创建Kvm
        String branchName = dto.getBranchName();
        ArrayList<String> hostnames = Lists.newArrayList();
        List<Project> projects = dto.getProjects();
        for (Project project : projects) {
            String hostname = project.getProjectName();
            hostname = hostname + "-" + branchName;
            hostnames.add(hostname);
            // 创建代码分支
            try {
                gitlabService.createBranch(project.getProjectId(), branchName);
            } catch (IOException e) {
                log.info(String.format("创建分支失败： { projectId: %s , branchName: %s}", project.getProjectId(), branchName));
            }
        }
        kvmService.createKvmAsync(hostnames);
    }


    /**
     * 删除一个变更
     *
     * @param changeId 变更id
     */
    @DeleteMapping(value = "/change")
    public void deleteChange(Integer changeId) {
        Changes changeWithKvms = changeService.findChangeWithKvmIds(changeId);
        String branchName = changeWithKvms.getBranchName();
        List<Integer> kvmIds = changeWithKvms.getKvmIds();
        List<Kvm> kvmWithIds = kvmService.findKvmByIds(kvmIds);
        List<String> hostnames = kvmWithIds.stream().map(Kvm::getHostname).collect(Collectors.toList());
        // 1.  删除Kvm记录,  销毁kvm主机
        kvmService.deleteKvmRecordByChangeId(changeId);
        kvmService.destroyKvmAsync(hostnames);
        // 2. 解除用户kvm 的关系
        userService.deleteUserKvm(kvmIds);
        // 3. 删除 用户 变更关系
        userService.deleteUserChange(changeId);
        // 4. 删除变更
        changeService.deleteChange(changeId);
        // 5. 删除Gitlab代码分支
        kvmWithIds.forEach(r -> {
            Integer projectId = r.getProjectId();
            try {
                gitlabService.deleteBranch(projectId, branchName);
            } catch (IOException e) {
                log.info(String.format("删除分支失败： {projectId：%s,branchName: %s}", projectId, branchName));
            }
        });
    }
}
