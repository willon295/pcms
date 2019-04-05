package cn.willon.pcms.pcmsmidware.dao;

import cn.willon.pcms.pcmsmidware.domain.bean.Changes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ChangeDao
 *
 * @author Willon
 * @since 2019-04-05
 */
@Repository
public interface ChangeDao extends JpaRepository<Changes, Integer> {
}
