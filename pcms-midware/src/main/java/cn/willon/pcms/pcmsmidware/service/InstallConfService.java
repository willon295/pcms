package cn.willon.pcms.pcmsmidware.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * InstallConfService
 *
 * @author Willon
 * @since 2019-04-06
 */
@Service
@FeignClient(value = "install-conf-server")
public interface InstallConfService {


    @GetMapping("/tryLock")
    boolean tryLock();

    @GetMapping("/generate/{hostname}")
    Boolean generate(@PathVariable(name = "hostname") String hostname);

}
