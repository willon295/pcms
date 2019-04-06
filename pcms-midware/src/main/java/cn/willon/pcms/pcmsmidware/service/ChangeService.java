package cn.willon.pcms.pcmsmidware.service;

import cn.willon.pcms.pcmsmidware.domain.constrains.DevStatusEnums;

import cn.willon.pcms.pcmsmidware.domain.bean.Changes;
import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import cn.willon.pcms.pcmsmidware.domain.bean.Project;
import cn.willon.pcms.pcmsmidware.domain.dto.SaveChangeDto;
import cn.willon.pcms.pcmsmidware.mapper.ChangeMapper;
import cn.willon.pcms.pcmsmidware.mapper.KvmMapper;
import cn.willon.pcms.pcmsmidware.mapper.condition.SaveUserChangeCondition;
import cn.willon.pcms.pcmsmidware.mapper.condition.SaveUserKvmCondition;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;

/**
 * ChangeService
 *
 * @author Willon
 * @since 2019-04-06
 */
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
            kvm.setCreateDate(startTime);
            kvm.setExpireDate(endTime);
            kvm.setDevStatus(DevStatusEnums.INSTALLING.getStatus());
            kvm.setChangeId(changeId);
            kvmMapper.save(kvm);
            List<Integer> developers = project.getDevelopers();
            // 保存kvm用户
            for (Integer userId : developers) {
                SaveUserKvmCondition saveUserKvm = new SaveUserKvmCondition(userId, kvm.getKvmId(), "select");
                kvmMapper.saveUserKvm(saveUserKvm);
                userIds.add(userId);
            }
        }

        // 保存变更中的用户信息
        for (Integer userId : userIds) {
            SaveUserChangeCondition saveUserChangeCondition = new SaveUserChangeCondition(userId, changeId, 0);
            changeMapper.saveUserChange(saveUserChangeCondition);
        }


    }


    /**
     * 保存变更
     * 1. 保存 变更基本信息
     * 2. 保存 对应的工程 kvm 基本信息
     * 3. 保存 kvm 对应的用户基本信息
     * 4. 异步创建 kvm， 创建成功之后更新kvm的IP
     *
     * @param change               变更基本信息
     * @param userChangeConditions 用户变更的基本信息
     * @param userKvmConditions    用户kvm的基本信息
     */
    public void saveChange(Changes change, List<Kvm> kvms, List<SaveUserChangeCondition> userChangeConditions, List<SaveUserKvmCondition> userKvmConditions) {

        // 保存后返回新的变更id
        changeMapper.save(change);
        Integer changeId = change.getChangeId();
        // 保存变更对应的所有用户
        userChangeConditions.forEach(r -> {
            r.setChangeId(changeId);
            changeMapper.saveUserChange(r);
        });
        long currentTimeMillis = System.currentTimeMillis();
        kvms.forEach(k -> {
            k.setChangeId(changeId);
            k.setCreateDate(currentTimeMillis);
            kvmMapper.save(k);
        });

    }
}
