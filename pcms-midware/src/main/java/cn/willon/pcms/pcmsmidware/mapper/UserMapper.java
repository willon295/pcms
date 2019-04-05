package cn.willon.pcms.pcmsmidware.mapper;

import cn.willon.pcms.pcmsmidware.domain.bean.User;

import java.util.List;

/**
 * UserMapper
 *
 * @author Willon
 * @since 2019-04-05
 */
public interface UserMapper {

    User findByUserId(Integer userId);

    List<User> findAll();

    void save(User user);
}
