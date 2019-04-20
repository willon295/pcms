package cn.willon.pcms.pcmsmidware.mapper;

import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import cn.willon.pcms.pcmsmidware.mapper.condition.QueryHasPermissionKvmCondition;
import cn.willon.pcms.pcmsmidware.mapper.condition.SaveUserKvmCondition;
import cn.willon.pcms.pcmsmidware.mapper.condition.UpdateKvmDevStatusCondition;
import cn.willon.pcms.pcmsmidware.mapper.condition.UpdateKvmIpCondition;

import java.util.List;

/**
 * UserMapper
 *
 * @author Willon
 * @since 2019-04-05
 */
public interface KvmMapper {

    Kvm findByKvmId(Integer kvmId);

    Kvm findByHostname(String hostname);

    List<Kvm> findAll();

    void save(Kvm kvm);

    void saveUserKvm(SaveUserKvmCondition condition);

    /**
     * 查找该kvm对应的用户
     *
     * @param kvmId kvmId
     * @return kvm信息，包含其有权限的用户
     */
    Kvm findKvmWithUser(int kvmId);


    /**
     * 更新kvm的ip
     *
     * @param condition 参数条件
     */
    void updateKvmIpByHostname(UpdateKvmIpCondition condition);


    /**
     * 修改kvm的状态
     *
     * @param condition 参数条件
     */
    void updateKvmDevStatusByHostname(UpdateKvmDevStatusCondition condition);

    /**
     * 判断kvm是否创建成功
     *
     * @param hostname 主机名
     * @return 是否成功
     */
    int isCreateKvmSuccess(String hostname);

    /**
     * 根据
     *
     * @param changeId 参数条件
     */
    void deleteKvmByChangeId(Integer changeId);

    /**
     * 更新kvm状态
     *
     * @param condition 参数条件
     */
    void updateDevStatus(UpdateKvmDevStatusCondition condition);

    List<Kvm> findHasPermissionKvm(QueryHasPermissionKvmCondition condition);

    Integer findKvmIdByProjectId(int projectId);

    Integer findPublishProjectIdByHostname(String hostname);
}
