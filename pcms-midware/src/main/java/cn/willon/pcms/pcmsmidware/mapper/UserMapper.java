package cn.willon.pcms.pcmsmidware.mapper;

import cn.willon.pcms.pcmsmidware.domain.bean.User;
import cn.willon.pcms.pcmsmidware.domain.bo.UserChangeDO;

import java.util.List;

/**
 * UserMapper
 *
 * @author Willon
 * @since 2019-04-05
 */
public interface UserMapper {

    /**
     * 通过用户id查找用户信息， 会查询用户所有变更信息
     *
     * @param userId 用户id
     * @return 用户信息，包含变更列表
     */
    UserChangeDO findByUserId(Integer userId);

    List<User> findAll();

    void save(User user);

    void deleteUserKvm(Integer kvmId);

    void deleteUserChange(Integer changeId);

    User findByUsernamePassword(User user);
}
