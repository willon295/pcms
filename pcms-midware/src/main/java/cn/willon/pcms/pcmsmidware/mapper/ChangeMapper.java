package cn.willon.pcms.pcmsmidware.mapper;

import cn.willon.pcms.pcmsmidware.domain.bean.Changes;

import java.util.List;

/**
 * UserMapper
 *
 * @author Willon
 * @since 2019-04-05
 */
public interface ChangeMapper {


    Changes findByChangeId(Integer changeId);

    List<Changes> findAll();

    void save(Changes change);
}
