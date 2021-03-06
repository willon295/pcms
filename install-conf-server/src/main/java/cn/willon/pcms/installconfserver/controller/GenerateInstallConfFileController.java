package cn.willon.pcms.installconfserver.controller;

import cn.willon.pcms.installconfserver.service.GenerateFileService;
import cn.willon.pcms.installconfserver.service.KvmService;
import cn.willon.pcms.installconfserver.service.RedisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * GenerateInstallConfFileController
 * <p>
 * 生成新的配置文件
 *
 * @author Willon
 * @since 2019-01-13
 */
@RestController
public class GenerateInstallConfFileController {

    @Resource
    private RedisService redisService;

    @Resource
    private KvmService kvmService;

    @Resource
    private GenerateFileService generateFileService;


    @GetMapping("/tryLock")
    public boolean tryLock() {
        return redisService.tryLock();
    }


    /**
     * 生成新的配置文件
     *
     * @param hostname 主机名称
     * @return 是否成功生成主机配置
     */
    @GetMapping("/generate/{hostname}")
    public Boolean generate(@PathVariable(name = "hostname") String hostname) {
        try {
            boolean lock = redisService.lock(hostname);
            if (lock) {
                generateFileService.gernate(hostname);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    /**
     * kvm安装完成， 释放锁
     */
    @GetMapping("/finish")
    public void finish() {
        // 更新
        String hostname = generateFileService.readNewHostname();
        String ip = generateFileService.readNewIp();
        kvmService.finishCreateKvm(hostname, ip);
        // 释放锁
        redisService.unlock();
    }

}
