package cn.willon.pcms.pcmsmidware.mapper;

import cn.willon.pcms.pcmsmidware.domain.bean.Changes;
import cn.willon.pcms.pcmsmidware.domain.bean.PubCheck;
import cn.willon.pcms.pcmsmidware.domain.bean.User;
import cn.willon.pcms.pcmsmidware.domain.bo.ProjectDO;
import cn.willon.pcms.pcmsmidware.mapper.condition.*;

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

    /**
     * 删除变更
     *
     * @param changeId 变更ID
     */
    void deleteChangeByChangeId(Integer changeId);

    Integer findOwnerId(Integer changeId);

    User findOwner(Integer changeId);

    /**
     * 当前变更，是否拥有当前工程的操作权限
     */
    int hasProjectPublishPermission(QueryChangeProjectPermCondition condition);


    /**
     * 更新线上服务器状态
     *
     * @param condition 条件
     */
    void updateProjectStatus(UpdatePubStatusCondition condition);

    /**
     * 获取线上服务器IP
     *
     * @param changeId  变更Id
     * @param projectId 工程ID
     * @return ip
     */
    String findPublishProjectIP(Integer changeId, Integer projectId);

    /**
     * 获取占用线上环境的变更
     *
     * @param projectName 工程名
     * @return 变更Id
     */
    Integer findHoldPublishChangeId(String projectName);

    /**
     * 占有工程的线上操作权限
     *
     * @param condition 条件
     */
    void holdPublish(HoldPublishCondition condition);

    /**
     * 保存线上审核
     *
     * @param pubCheck 审核
     */
    void savePubCheck(PubCheck pubCheck);

    /**
     * 拒绝线上环境审核
     * @param checkId id
     */
    void denyPubCheck(Integer checkId);

    /**
     * 拒绝线上环境审核
     * @param checkId id
     */
    void accessPubCheck(Integer checkId);

    ProjectDO findPublishProjectByName(String projectName);

    List<PubCheck> findUserSendPubCheck(QueryUserSendPubCheckCondition condition);

    List<PubCheck> findUserReceivePubChecks(Integer userId);

    List<Integer> findChangUsers(int changeId);

    void updateChange(Changes changes);
}
