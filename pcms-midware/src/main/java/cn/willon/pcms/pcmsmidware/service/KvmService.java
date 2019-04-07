package cn.willon.pcms.pcmsmidware.service;

import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import cn.willon.pcms.pcmsmidware.domain.constrains.DevStatusEnums;
import cn.willon.pcms.pcmsmidware.executor.KvmBashExecutor;
import cn.willon.pcms.pcmsmidware.mapper.KvmMapper;
import cn.willon.pcms.pcmsmidware.mapper.condition.UpdateKvmDevStatusCondition;
import cn.willon.pcms.pcmsmidware.mapper.condition.UpdateKvmIpCondition;
import cn.willon.pcms.pcmsmidware.thred.ThreadPoolManager;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * KvmService
 *
 * @author Willon
 * @since 2019-04-06
 */
@Slf4j
@Service
public class KvmService {

    public static final long SLEEP_TIME = 10000L;
    @Resource
    private KvmMapper kvmMapper;

    @Resource
    private KvmBashExecutor kvmBashExecutor;

    @Resource
    private InstallConfService installConfService;


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


    /**
     * 判断kvm是否创建成功
     *
     * @param hostname 主机名
     * @return 是否成功
     */
    public boolean isCreateKvmSuccess(String hostname) {
        return kvmMapper.isCreateKvmSuccess(hostname) > 0;
    }


    /**
     * 异步创建Kvm
     *
     * @param hostnames 主机名
     */
    public void createKvmAsync(List<String> hostnames) {

        for (String hostname : hostnames) {

            ThreadPoolManager.INSTANCE.addTask(() -> {
                int tryLockCount = 0;
                boolean lock = false;
                while (!lock) {
                    if (tryLockCount++ > 0) {
                        Thread.sleep(SLEEP_TIME);
                    }
                    lock = installConfService.tryLock();
                }
                // 开始生成文件， 并且创建kvm
                int tryGenfileCount = 0;
                boolean genFile = false;
                while (!genFile) {
                    if (tryGenfileCount++ > 0) {
                        Thread.sleep(SLEEP_TIME);
                    }
                    genFile = installConfService.generate(hostname);
                }
                kvmBashExecutor.installKvm(hostname);
                return null;
            });


        }
    }



    public void deleteKvmRecordByChangeId(Integer changeId) {
        kvmMapper.deleteKvmByChangeId(changeId);
    }

    /**
     * 异步删除Kvm
     *
     * @param hostnames 主机名
     */
    public void destroyKvmAsync(List<String> hostnames) {

        for (String hostname : hostnames) {
            ThreadPoolManager.INSTANCE.addTask(() -> {
                kvmBashExecutor.deleteKvm(hostname);
                return null;
            });
        }
    }

    public List<Kvm> findKvmByIds(List<Integer> kvmIds) {

        ArrayList<Kvm> kvms = Lists.newArrayList();
        for (Integer kvmId : kvmIds) {
            Kvm byKvmId = kvmMapper.findByKvmId(kvmId);
            kvms.add(byKvmId);
        }
        return kvms;
    }

}