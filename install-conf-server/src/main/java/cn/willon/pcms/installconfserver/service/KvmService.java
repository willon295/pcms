package cn.willon.pcms.installconfserver.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * KvmService
 *
 * @author Willon
 * @since 2019-04-06
 */
@Service
@FeignClient(value = "PCMS-MIDWARE")
public interface KvmService {

    /**
     * 远程调用更新ip
     *
     * @param hostname 主机名
     * @param ip       ip
     */
    @PutMapping("/kvm")
    void updateKvmIp(@RequestParam(name = "hostname") String hostname, @RequestParam(name = "ip") String ip);
}
