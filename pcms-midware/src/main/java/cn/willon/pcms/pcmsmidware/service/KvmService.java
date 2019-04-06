package cn.willon.pcms.pcmsmidware.service;

import cn.willon.pcms.pcmsmidware.domain.constrains.DevStatusEnums;
import cn.willon.pcms.pcmsmidware.mapper.KvmMapper;
import cn.willon.pcms.pcmsmidware.mapper.condition.UpdateKvmDevStatusCondition;
import cn.willon.pcms.pcmsmidware.mapper.condition.UpdateKvmIpCondition;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * KvmService
 *
 * @author Willon
 * @since 2019-04-06
 */
@Slf4j
@Service
public class KvmService {

    @Resource
    private KvmMapper kvmMapper;

    /**
     * 更新kvm的ip
     *
     * @param hostname 主机名
     * @param ip       新的ip
     */
    public void finishCreateKvm(String hostname, String ip) {

        if (Strings.isNullOrEmpty(hostname) || Strings.isNullOrEmpty(ip)) {
            return;
        }
        log.info(String.format("更新主机ip {hostname: %s , ip:%s}", hostname, ip));
        kvmMapper.updateKvmIpByHostname(new UpdateKvmIpCondition(hostname, ip));
        kvmMapper.updateKvmDevStatusByHostname(new UpdateKvmDevStatusCondition(hostname, DevStatusEnums.PENDING.getStatus()));
    }


}
