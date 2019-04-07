package cn.willon.pcms.pcmsmidware.service;

import cn.willon.pcms.pcmsmidware.domain.bean.User;
import cn.willon.pcms.pcmsmidware.domain.bo.UserChangeDO;
import cn.willon.pcms.pcmsmidware.mapper.UserMapper;
import cn.willon.pcms.pcmsmidware.service.rollback.UserServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * UserService
 *
 * @author Willon
 * @since 2019-04-06
 */
@Transactional(rollbackFor = UserServiceException.class)
@Service
public class UserService {


    @Resource
    private UserMapper userMapper;

    /**
     * 查找用户变更，且查找出适用与首页
     *
     * @param userId 用户Id
     * @return
     */
    public UserChangeDO findUserWithChange(Integer userId) {
        UserChangeDO userChangeDO = userMapper.findByUserId(userId);
        userChangeDO.setTotalCount(userChangeDO.getChanges().size());
        return userChangeDO;
    }

    public void deleteUserKvm(List<Integer> kvmIds) {
        kvmIds.forEach(i -> userMapper.deleteUserKvm(i));
    }

    public void deleteUserChange(Integer changeId) {
        userMapper.deleteUserChange(changeId);
    }

    public User exists(User user) {
        User loginUser = userMapper.findByUsernamePassword(user);
        return loginUser;
    }
}
