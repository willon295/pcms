package cn.willon.pcms.pcmsmidware.thred;

import cn.willon.pcms.pcmsmidware.executor.KvmBashExecutor;
import cn.willon.pcms.pcmsmidware.service.InstallConfService;
import cn.willon.pcms.pcmsmidware.service.KvmService;

import javax.annotation.Resource;
import java.util.concurrent.Callable;

/**
 * CreateKvmThread
 *
 * @author Willon
 * @since 2019-04-06
 */
public class CreateKvmThread implements Callable<Boolean> {


    @Resource
    private KvmBashExecutor kvmBashExecutor;

    @Resource
    private KvmService kvmService;

    @Resource
    private InstallConfService installConfService;

    private String hostname;

    public CreateKvmThread(String hostname) {
        this.hostname = hostname;
    }


    @Override
    public Boolean call() {

        boolean lock = false;
        while (!lock) {
            lock = installConfService.tryLock();
        }
        // 开始生成文件， 并且创建kvm
        boolean genFile = false;
        while (!genFile) {
            genFile = installConfService.generate(hostname);
        }
        kvmBashExecutor.installKvm(hostname);
        return true;
    }
}
