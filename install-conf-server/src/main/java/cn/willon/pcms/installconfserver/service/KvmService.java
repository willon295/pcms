package cn.willon.pcms.installconfserver.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    void finishCreateKvm(@RequestParam(name = "hostname") String hostname, @RequestParam(name = "ip") String ip);

    /**
     * 开启kvm
     *
     * @param hostname 主机名
     */
    @GetMapping("/start/{hostname}")
    void startKvm(@PathVariable(name = "hostname") String hostname);


    /**
     * 判断是否存在
     *
     * @param hostname 主机名
     * @return 是否存在
     */
    @GetMapping("/kvm/exist/{hostname}")
    boolean isCreateKvmSuccess(@PathVariable @RequestParam(name = "hostname") String hostname);
}
