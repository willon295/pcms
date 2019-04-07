package cn.willon.pcms.pcmsmidware.service;

import cn.willon.pcms.pcmsmidware.domain.bean.User;
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

    public User findUserWithChange(Integer userId) {
        return userMapper.findByUserId(userId);
    }

    public void deleteUserKvm(List<Integer> kvmIds) {
        kvmIds.forEach(i -> userMapper.deleteUserKvm(i));
    }

    public void deleteUserChange(Integer changeId) {
        userMapper.deleteUserChange(changeId);
    }
}
