package cn.willon.pcms.pcmsmidware.mapper;

import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;

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
}
