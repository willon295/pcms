package cn.willon.pcms.pcmsmidware.controller.change;

import cn.willon.pcms.pcmsmidware.domain.bean.Changes;
import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import cn.willon.pcms.pcmsmidware.domain.bean.Project;
import cn.willon.pcms.pcmsmidware.domain.bo.ChangeKvmsDO;
import cn.willon.pcms.pcmsmidware.domain.dto.SaveChangeDto;
import cn.willon.pcms.pcmsmidware.domain.vo.ChangeDetailVO;
import cn.willon.pcms.pcmsmidware.domain.vo.ChangeVO;
import cn.willon.pcms.pcmsmidware.service.ChangeService;
import cn.willon.pcms.pcmsmidware.service.GitlabService;
import cn.willon.pcms.pcmsmidware.service.KvmService;
import cn.willon.pcms.pcmsmidware.service.UserService;
import cn.willon.pcms.pcmsmidware.util.Result;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    @DeleteMapping(value = "/change/{changeId}")
    public void deleteChange(@PathVariable(name = "changeId") Integer changeId) {
        Changes changeWithKvms = changeService.findChangeWithKvmIds(changeId);
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
    }


    /**
     * 获取变更详情
     *
     * @param changeId 变更ID
     * @return 变更详情
     */
    @GetMapping("/change/{changeId}")
    public Result changeDetail(@PathVariable(name = "changeId") Integer changeId) {

        ChangeKvmsDO ckdo = changeService.changeDetail(changeId);
        ChangeDetailVO changeDetailVO = new ChangeDetailVO();
        Changes change = ckdo.getChange();
        ChangeVO changeVO = new ChangeVO();
        changeVO.setChangeId(change.getChangeId());
        changeVO.setChangeName(change.getChangeName());
        changeVO.setBranchName(change.getBranchName());
        Instant createInstant = Instant.ofEpochMilli(change.getCreateDate());
        LocalDateTime t1 = LocalDateTime.ofInstant(createInstant, ZoneId.systemDefault());
        String createDate = String.format("%s-%s-%s", t1.getYear(), t1.getMonthValue(), t1.getDayOfMonth());

        Instant expireInstant = Instant.ofEpochMilli(change.getExpireDate());
        LocalDateTime t2 = LocalDateTime.ofInstant(expireInstant, ZoneId.systemDefault());
        String expireDate = String.format("%s-%s-%s", t2.getYear(), t2.getMonthValue(), t2.getDayOfMonth());

        changeVO.setCreateDate(createDate);
        changeVO.setExpireDate(expireDate);
        changeDetailVO.setChange(changeVO);
        changeDetailVO.setOwnerId(ckdo.getOwnerId());
        changeDetailVO.setOwnerName(ckdo.getOwnerName());
        changeDetailVO.setKvms(ckdo.getKvms());

        return Result.successResult(changeDetailVO);
    }


    /**
     * // TODO 修改变更
     * 1. 修改变更的owner
     * 2. 修改工程的参与者
     * 3. 添加依赖工程
     *
     * @return 处理结果
     */
    @PutMapping("/change")
    public Result updateChange() {


        return  null;
    }
}
