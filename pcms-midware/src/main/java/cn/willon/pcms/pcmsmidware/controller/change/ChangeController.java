package cn.willon.pcms.pcmsmidware.controller.change;

import cn.willon.pcms.pcmsmidware.domain.AccessCheckCondition;
import cn.willon.pcms.pcmsmidware.domain.bean.Changes;
import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import cn.willon.pcms.pcmsmidware.domain.bean.Project;
import cn.willon.pcms.pcmsmidware.domain.bean.PubCheck;
import cn.willon.pcms.pcmsmidware.domain.bo.ChangeKvmsDO;
import cn.willon.pcms.pcmsmidware.domain.bo.ProjectDO;
import cn.willon.pcms.pcmsmidware.domain.constrains.DevStatusEnums;
import cn.willon.pcms.pcmsmidware.domain.constrains.PubStatusEnums;
import cn.willon.pcms.pcmsmidware.domain.dto.SaveChangeDto;
import cn.willon.pcms.pcmsmidware.domain.dto.UpdateChangeDto;
import cn.willon.pcms.pcmsmidware.domain.dto.UpdateKvmDto;
import cn.willon.pcms.pcmsmidware.domain.vo.*;
import cn.willon.pcms.pcmsmidware.service.ChangeService;
import cn.willon.pcms.pcmsmidware.service.GitlabService;
import cn.willon.pcms.pcmsmidware.service.KvmService;
import cn.willon.pcms.pcmsmidware.service.UserService;
import cn.willon.pcms.pcmsmidware.util.Result;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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


    public static final String SUCCESS = "success";
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
     * 获取开发环境的变更新
     *
     * @param changeId 变更id
     * @return 详情
     */
    @GetMapping("/change/dev/{changeId}")
    public Result viewDevChange(@PathVariable Integer changeId, @RequestParam(name = "userId") Integer userId) {
        // 获取当前变更   所有kvm
        Changes change = changeService.findChangeWithKvmIds(changeId);
        List<Integer> kvmIds = change.getKvmIds();
        List<Kvm> kvms = kvmService.findKvmByIds(kvmIds);
        String branchName = change.getBranchName();
        // 获取用户拥有权限的kvm
        List<Kvm> hasPermissionKvms = kvmService.findHasPermissionKvms(changeId, userId);
        Map<Integer, Kvm> map = hasPermissionKvms.stream().collect(Collectors.toMap(Kvm::getKvmId, k -> k));

        // 合并
        List<KvmVO> all = kvms.stream().map(k -> {
            KvmVO kv = new KvmVO();
            BeanUtils.copyProperties(k, kv);
            if (map.containsKey(k.getKvmId())) {
                kv.setPermission("all");
            } else {
                kv.setPermission(null);
            }
            Instant createInstant = Instant.ofEpochMilli(k.getCreateDate());
            LocalDateTime t1 = LocalDateTime.ofInstant(createInstant, ZoneId.systemDefault());
            String createDate = String.format("%s-%s-%s", t1.getYear(), t1.getMonthValue(), t1.getDayOfMonth());

            Instant expireInstant = Instant.ofEpochMilli(k.getExpireDate());
            LocalDateTime t2 = LocalDateTime.ofInstant(expireInstant, ZoneId.systemDefault());
            String expireDate = String.format("%s-%s-%s", t2.getYear(), t2.getMonthValue(), t2.getDayOfMonth());
            kv.setCreateDate(createDate);
            kv.setExpireDate(expireDate);
            kv.setBranchName(branchName);
            return kv;
        }).collect(Collectors.toList());

        DevVO devVO = new DevVO();
        devVO.setChangeName(change.getChangeName());
        devVO.setKvms(all);
        return Result.successResult(devVO);
    }


    /**
     * 获取线上环境的变更新
     *
     * @param changeId 变更id
     * @return 详情
     */
    @GetMapping("/change/publish/{changeId}")
    public Result viewPublishChange(@PathVariable Integer changeId, @RequestParam(name = "userId") Integer userId) {
        // 查询本工程相关的 projectName
        ChangeKvmsDO changeKvmsDO = changeService.changeDetail(changeId);
        String changeName = changeKvmsDO.getChange().getChangeName();
        // 通过 projectName 获取相关的  线上工程信息
        List<Kvm> kvms = changeKvmsDO.getKvms();
        List<String> changeAllProjectNames = kvms.stream().map(Kvm::getProjectName).distinct().collect(Collectors.toList());
        // 线上服务器信息
        List<ProjectDO> changeAllProjects = changeService.findAllPublishProjects(changeAllProjectNames);
        // 获取拥有权限的工程
        List<Kvm> hasPermissionKvms = kvmService.findHasPermissionKvms(changeId, userId);
        Map<String, Kvm> hasPermissionMap = hasPermissionKvms.stream().collect(Collectors.toMap(Kvm::getProjectName, k -> k));

        List<ServerVO> servers = changeAllProjects.stream().map(p -> {
            // 当前变更， 是否可以操作、当前工程

            String projectName = p.getProjectName();
            ServerVO serverVO = new ServerVO();
            serverVO.setProjectId(p.getProjectId());
            serverVO.setProjectName(projectName);
            serverVO.setServerIp(p.getServerIp());
            serverVO.setPubStatus(p.getPubStatus());
            if (!changeService.hasProjectPublishPermission(p.getProjectId(), changeId)) {
                serverVO.setPubStatus(PubStatusEnums.PENDING.getStatus());
            }
            if (hasPermissionMap.containsKey(projectName)) {
                serverVO.setPermission("all");
            } else {
                serverVO.setPermission(null);
            }
            return serverVO;

        }).collect(Collectors.toList());
        PublishVO publishVO = new PublishVO();
        publishVO.setChangeId(changeId);
        publishVO.setChangeName(changeName);
        publishVO.setServers(servers);
        // 获取需要本人审核的信息
        List<PubCheck> receives = changeService.findMyReceivePubChecks(userId);
        List<PubCheckVO> receiveVOs = receives.stream().map(r -> {
            PubCheckVO re = new PubCheckVO();
            re.setCheckId(r.getCheckId());
            re.setCheckApplyUserId(r.getCheckApplyUserId());
            String username = userService.findUsernameByUserId(r.getCheckApplyUserId());
            re.setCheckApplyUsername(username);
            Integer checkChangeId = r.getCheckChangeId();
            String checkChangeName = changeService.findChangeNameByChangeId(checkChangeId);
            re.setCheckChangeId(checkChangeId);
            re.setCheckChangeName(checkChangeName);
            re.setCheckStatus(r.getCheckStatus());
            re.setCheckProjectName(r.getCheckProjectName());
            return re;

        }).collect(Collectors.toList());
        publishVO.setReceives(receiveVOs);
        // 获取我发起的申请
        List<PubCheck> sends = changeService.findMySendPubChecks(changeId, userId);
        List<PubCheckVO> sendVOs = sends.stream().map(s -> {
            PubCheckVO re = new PubCheckVO();
            re.setCheckId(s.getCheckId());
            re.setCheckReceiveUserId(s.getCheckReceiveUserId());
            re.setCheckReceiveUsername(userService.findUsernameByUserId(s.getCheckReceiveUserId()));
            re.setCheckChangeId(s.getCheckChangeId());
            re.setCheckChangeName(changeService.findChangeNameByChangeId(s.getCheckChangeId()));
            re.setCheckStatus(s.getCheckStatus());
            re.setCheckProjectName(s.getCheckProjectName());
            return re;
        }).collect(Collectors.toList());
        publishVO.setSends(sendVOs);

        return Result.successResult(publishVO);
    }


    /**
     * 开发环境测试通过
     */
    @PutMapping("/testPass/{hostname}")
    public Result devTestPass(@PathVariable String hostname) {
        Kvm kvm = kvmService.findByHostname(hostname);
        kvmService.updateDevStatus(kvm.getKvmId(), DevStatusEnums.TEST_PASS.getStatus());
        return Result.successResult("ok");
    }


    /**
     * 拒绝线上工程审核
     */
    @PutMapping("/check/{checkId}")
    public void denyPubCheck(@PathVariable Integer checkId) {
        changeService.denyPubcheck(checkId);
    }

    /**
     * 同意线上工程审核
     */
    @PutMapping("/check/")
    public void accessPubCheck(@RequestBody AccessCheckCondition condition) {
        Integer checkId = condition.getCheckId();
        Integer changeId = condition.getChangeId();
        String projectName = condition.getProjectName();
        changeService.accessPubCheck(checkId, projectName, changeId);
    }


    /**
     * 1. 修改变更的owner
     * 2. 修改工程的参与者
     * 3. 添加依赖工程
     *
     * @return 处理结果
     */
    @PutMapping("/change")
    public Result updateChange(@RequestBody UpdateChangeDto dto) {
        // 变更相关的修改
        changeService.updateChange(dto);
        return Result.successResult(Result.SUCCESS);
    }

    @PutMapping("/change/kvm")
    public Result updateKvm(@RequestBody ArrayList<UpdateKvmDto> dtos) {
        kvmService.updateKvm(dtos);
        return Result.successResult(Result.SUCCESS);
    }
}
