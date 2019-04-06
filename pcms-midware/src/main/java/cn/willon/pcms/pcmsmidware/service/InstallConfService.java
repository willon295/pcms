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
@FeignClient
public interface InstallConfService {


    @GetMapping("/tryLock")
    public boolean tryLock();

    @GetMapping("/generate/{hostname}")
    public Boolean generate(@PathVariable(name = "hostname") String hostname);

}
