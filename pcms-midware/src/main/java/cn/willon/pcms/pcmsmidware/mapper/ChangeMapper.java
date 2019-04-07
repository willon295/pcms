package cn.willon.pcms.pcmsmidware.mapper;

import cn.willon.pcms.pcmsmidware.domain.bean.Changes;
import cn.willon.pcms.pcmsmidware.mapper.condition.SaveUserChangeCondition;

import java.util.List;

/**
 * UserMapper
 *
 * @author Willon
 * @since 2019-04-05
 */
public interface ChangeMapper {


    /**
     * 查找变更
     *
     * @param changeId 变更id
     * @return 变更id， 带有所有的kvm信息
     */
    Changes findByChangeId(Integer changeId);

    /**
     * 查找所有的变更信息
     *
     * @return 变更列表
     */
    List<Changes> findAll();

    /**
     * 保存变更
     *
     * @param change 变更信息
     */
    void save(Changes change);

    /**
     * 保存用户的变更
     *
     * @param condition 条件
     */
    void saveUserChange(SaveUserChangeCondition condition);

    void deleteChangeByChangeId(Integer changeId);
}
