package cn.willon.pcms.pcmsmidware.service;

import cn.willon.pcms.pcmsmidware.domain.bean.*;
import cn.willon.pcms.pcmsmidware.domain.bo.ChangeKvmsDO;
import cn.willon.pcms.pcmsmidware.domain.bo.ProjectDO;
import cn.willon.pcms.pcmsmidware.domain.condition.UpdateChangeDto;
import cn.willon.pcms.pcmsmidware.domain.constrains.DevStatusEnums;
import cn.willon.pcms.pcmsmidware.domain.dto.SaveChangeDto;
import cn.willon.pcms.pcmsmidware.mapper.ChangeMapper;
import cn.willon.pcms.pcmsmidware.mapper.KvmMapper;
import cn.willon.pcms.pcmsmidware.mapper.condition.*;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ChangeService
 *
 * @author Willon
 * @since 2019-04-06
 */
@Slf4j
@Service
public class ChangeService {


    private static final String MM_DD_YYYY = "MM/dd/yyyy HH:mm:ss";

    @Resource
    private KvmMapper kvmMapper;

    @Resource
    private ChangeMapper changeMapper;

    /**
     * 保存变更信息
     *
     * @param dto dto
     */
    public void saveChange(SaveChangeDto dto) {
        Integer ownerId = dto.getOwnerId();
        String branchName = dto.getBranchName();
        String changeName = dto.getChangeName();
        long startTime = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String endDate = dto.getEndDate();
        endDate += " 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(MM_DD_YYYY);
        LocalDateTime dateTime = LocalDateTime.parse(endDate, formatter);
        Instant instant = dateTime.toInstant(ZoneOffset.UTC);
        long endTime = instant.toEpochMilli();

        // 先保存变更 ， 获取变更的 change_id
        Changes changes = new Changes();
        changes.setChangeName(changeName);
        changes.setBranchName(branchName);
        changes.setCreateDate(startTime);
        changes.setExpireDate(endTime);
        changeMapper.save(changes);
        Integer changeId = changes.getChangeId();


        List<Project> projects = dto.getProjects();
        HashSet<Integer> userIds = Sets.newHashSet();
        // 保存KVM的相关信息
        for (Project project : projects) {
            String projectName = project.getProjectName();
            Kvm kvm = new Kvm();
            kvm.setHostname(projectName + "-" + branchName);
            kvm.setProjectId(project.getProjectId());
            kvm.setProjectName(projectName);
            kvm.setIp("");
            kvm.setDevStatus(DevStatusEnums.INSTALLING.getStatus());
            kvm.setChangeId(changeId);
            kvm.setCreateDate(startTime);
            kvm.setExpireDate(endTime);
            kvmMapper.save(kvm);
            List<Integer> developers = project.getDevelopers();
            // 保存kvm用户
            for (Integer userId : developers) {
                SaveUserKvmCondition saveUserKvm = new SaveUserKvmCondition(userId, kvm.getKvmId(), "all");
                kvmMapper.saveUserKvm(saveUserKvm);
                userIds.add(userId);
            }
        }


        // 保存变更中的用户信息
        for (Integer userId : userIds) {
            SaveUserChangeCondition saveUserChangeCondition = new SaveUserChangeCondition(userId, changeId, 0);
            if (userId.equals(ownerId)) {
                saveUserChangeCondition.setIsOwner(1);
            }
            changeMapper.saveUserChange(saveUserChangeCondition);
        }

    }


    public Changes findChangeWithKvmIds(Integer changeId) {
        Changes byChangeId = changeMapper.findByChangeId(changeId);
        return byChangeId;
    }

    public void deleteChange(Integer changeId) {
        changeMapper.deleteChangeByChangeId(changeId);
    }


    /**
     * 查询所有的用户
     *
     * @param changeId
     * @return
     */
    public ChangeKvmsDO changeDetail(Integer changeId) {
        Changes byChangeId = changeMapper.findByChangeId(changeId);
        User owner = changeMapper.findOwner(changeId);
        ChangeKvmsDO changeKvmsDO = new ChangeKvmsDO();
        changeKvmsDO.setChange(byChangeId);
        List<Integer> kvmIds = byChangeId.getKvmIds();
        for (Integer kvmId : kvmIds) {
            Kvm kvmWithUser = kvmMapper.findKvmWithUser(kvmId);
            changeKvmsDO.getKvms().add(kvmWithUser);
        }
        changeKvmsDO.setOwnerId(owner.getUserId());
        changeKvmsDO.setOwnerName(owner.getRealName());
        return changeKvmsDO;
    }


    /**
     * 查找OwnerId
     *
     * @return OwnerId
     */
    public User findOwner(Integer changeId) {

        User user = changeMapper.findOwner(changeId);
        return user;

    }


    public boolean hasProjectPublishPermission(Integer projectId, Integer changeId) {
        QueryChangeProjectPermCondition condition = new QueryChangeProjectPermCondition();
        condition.setProjectId(projectId);
        condition.setChangeId(changeId);
        int i = changeMapper.hasProjectPublishPermission(condition);
        return i > 0;
    }

    public void updateProjectStatus(Integer changeId, Integer projectId, Integer status) {
        UpdatePubStatusCondition condition = new UpdatePubStatusCondition();
        condition.setChangeId(changeId);
        condition.setProjectId(projectId);
        condition.setPubStatus(status);
        changeMapper.updateProjectStatus(condition);
    }

    public String getPublishProjectIp(Integer changeId, Integer projectId) {
        return changeMapper.findPublishProjectIP(changeId, projectId);
    }

    public Integer findHoldPublishChangeId(String projectName) {
        return changeMapper.findHoldPublishChangeId(projectName);
    }

    public void holdPublish(String projectName, Integer changeId) {

        HoldPublishCondition condition = new HoldPublishCondition(projectName, changeId);
        changeMapper.holdPublish(condition);
    }

    public void savePubCheck(PubCheck pubCheck) {
        changeMapper.savePubCheck(pubCheck);
    }

    public void denyPubcheck(Integer checkId) {
        changeMapper.denyPubCheck(checkId);
    }

    public void accessPubCheck(Integer checkId, String projectName, Integer changeId) {
        // 改变状态
        changeMapper.accessPubCheck(checkId);
        // 修改线上 占用的工程ID
        HoldPublishCondition condition = new HoldPublishCondition(projectName, changeId);
        changeMapper.holdPublish(condition);
    }

    public List<ProjectDO> findAllPublishProjects(List<String> projectNames) {
        List<ProjectDO> projects = projectNames.stream().map(n -> changeMapper.findPublishProjectByName(n)).collect(Collectors.toList());
        return projects;
    }

    public List<PubCheck> findMySendPubChecks(Integer changeId, Integer userId) {

        QueryUserSendPubCheckCondition condition = new QueryUserSendPubCheckCondition(changeId, userId);
        return changeMapper.findUserSendPubCheck(condition);
    }

    public List<PubCheck> findMyReceivePubChecks(Integer userId) {
        return changeMapper.findUserReceivePubChecks(userId);
    }

    public String findChangeNameByChangeId(Integer changeId) {
        Changes byChangeId = changeMapper.findByChangeId(changeId);
        if (byChangeId != null) {
            return byChangeId.getChangeName();
        }
        return "";
    }

    /**
     * 更新 变更信息
     *
     * @param dto
     */
    public void updateChange(UpdateChangeDto dto) {

        int changeId = dto.getChangeId();
        int ownerId = dto.getOwnerId();
        Long endTime = null;
        Changes changes = new Changes();
        changes.setChangeId(changeId);

        String endDate = dto.getExpireDate();
        if (!StringUtils.isEmpty(endDate)) {
            endDate += " 00:00:00";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(endDate, formatter);
            Instant instant = dateTime.toInstant(ZoneOffset.UTC);
            endTime = instant.toEpochMilli();
        }
        changes.setExpireDate(endTime);
        changeMapper.updateChange(changes);
        // 获取新加入变更的人员信息
        List<UpdateChangeDto.ProjectsBean> projects = dto.getProjects();
        List<Integer> exitsUser = changeMapper.findChangUsers(changeId);
        log.info(String.format("{exitsUsers: %s}", JSON.toJSONString(exitsUser)));
        Set<Integer> newUserIds = projects.stream().flatMap(p -> p.getUsers().stream()).collect(Collectors.toSet());
        newUserIds.removeAll(exitsUser);
        log.info(String.format("{addUsers: %s}", JSON.toJSONString(newUserIds)));
        newUserIds.forEach(id -> {
            SaveUserChangeCondition condition = new SaveUserChangeCondition();
            condition.setUserId(id);
            condition.setChangeId(changeId);
            condition.setIsOwner(ownerId == id ? 1 : 0);
            changeMapper.saveUserChange(condition);
        });


        projects.forEach(p -> {
            List<Integer> users = p.getUsers();
            for (Integer user : users) {
                if (newUserIds.contains(user)) {
                    Integer kid = kvmMapper.finKvmIdByProjectId(p.getProjectId());
                    SaveUserKvmCondition saveUserKvmCondition = new SaveUserKvmCondition();
                    saveUserKvmCondition.setUserId(user);
                    saveUserKvmCondition.setKvmId(kid);
                    saveUserKvmCondition.setPermission("all");
                    kvmMapper.saveUserKvm(saveUserKvmCondition);
                }
            }
        });
    }


}
