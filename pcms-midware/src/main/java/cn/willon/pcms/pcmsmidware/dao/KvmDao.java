package cn.willon.pcms.pcmsmidware.dao;

import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * KvmDao
 *
 * @author Willon
 * @since 2019-04-05
 */
@Repository
public interface KvmDao extends JpaRepository<Kvm, Integer> {
}
