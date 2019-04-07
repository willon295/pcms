package cn.willon.pcms.pcmsmidware.controller.kvm;

import cn.willon.pcms.pcmsmidware.executor.KvmBashExecutor;
import cn.willon.pcms.pcmsmidware.service.KvmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * KvmManageController
 *
 * @author Willon
 * @since 2019-01-13
 */
@CrossOrigin(allowedHeaders = "*", maxAge = 3600)
@RestController
@Slf4j
public class KvmManageController {


    private static final long SLEEP_TIME = 10000L;

    @Resource
    private KvmService kvmService;


    @Resource
    private KvmBashExecutor kvmBashExecutor;

    /**
     * 创建新的kvm虚拟机， 在此之前确保已经生成新的文件， 并且获取到锁
     *
     * @param hostname 主机名
     * @return 是否成功执行
     */
    @GetMapping("/install/{hostname}")
    public boolean install(@PathVariable(name = "hostname") String hostname) {
        return kvmBashExecutor.installKvm(hostname);
    }

    @GetMapping("/delete/{hostname}")
    public boolean delete(@PathVariable(name = "hostname") String hostname) {
        return kvmBashExecutor.deleteKvm(hostname);
    }

    @GetMapping("/restart/{hostname}")
    public boolean restart(@PathVariable(name = "hostname") String hostname) {
        return kvmBashExecutor.restartKvm(hostname);
    }

    @GetMapping("/start/{hostname}")
    public boolean start(@PathVariable(name = "hostname") String hostname) {
        return kvmBashExecutor.startKvm(hostname);
    }

    /**
     * 判断是否存在
     *
     * @param hostname 主机名
     * @return 是否存在
     */
    @GetMapping("/kvm/exist/{hostname}")
    public boolean isCreateKvmSuccess(@PathVariable String hostname) {
        return kvmService.isCreateKvmSuccess(hostname);
    }

    /**
     * 修改主机ip, 并且运行主机
     *
     * @param hostname 主机名称
     * @param ip       ip
     */
    @PutMapping("/kvm")
    public void finishCreateKvm(@RequestParam(name = "hostname") String hostname, @RequestParam(name = "ip") String ip) {
        kvmService.finishCreateKvm(hostname, ip);
        try {
            // 由于安装完成之后 kvm处于完成配置状态，尚未关机，所以等待20S
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            log.info(String.format("创建线程失败：  {hostname:%s , ip:%s", hostname, ip));
        }
        kvmBashExecutor.startKvm(hostname);
    }


}
