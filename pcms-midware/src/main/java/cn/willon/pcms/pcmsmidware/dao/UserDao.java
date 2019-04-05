package cn.willon.pcms.pcmsmidware.dao;

import cn.willon.pcms.pcmsmidware.domain.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserDao
 *
 * @author Willon
 * @since 2019-04-05
 */
@Repository
public interface UserDao extends JpaRepository<User, Integer> {

}
