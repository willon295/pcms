package cn.willon.pcms.pcmsmidware.executor;

import cn.willon.pcms.pcmsmidware.domain.constrains.KvmStatus;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;

/**
 * KvmBashExecutor
 *
 * @author Willon
 * @since 2019-04-06
 */
@Slf4j
@Component
public class KvmBashExecutor {

    @Value("${kvm.binPath}")
    private String kvmShell;

    @Value("${kvm.cmd.install}")
    private String installCmd;

    @Value("${kvm.cmd.restart}")
    private String restartCmd;

    @Value("${kvm.cmd.delete}")
    private String deleteCmd;

    @Value("${kvm.cmd.start}")
    private String startCmd;

    @Value("${kvm.cmd.status}")
    private String status;

    private boolean exec(String cmd, String hostname) {
        ArrayList<String> cmds = new ArrayList<>();
        cmds.add("sh");
        cmds.add("-c");
        cmds.add(kvmShell + " " + cmd + " " + hostname);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(cmds);
        try {
            processBuilder.start();
            log.info("执行shell命令： " + JSON.toJSONString(cmds));
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * 销毁KVM
     *
     * @param hostname 主机名
     * @return 是否成功
     */
    public boolean deleteKvm(String hostname) {
        return exec(deleteCmd, hostname);
    }

    /**
     * 创建KVM
     *
     * @param hostname 主机名
     * @return 是否成功
     */
    public boolean installKvm(String hostname) {
        return exec(installCmd, hostname);
    }

    /**
     * 重启KVM
     *
     * @param hostname 主机名
     * @return 是否成功
     */
    public boolean restartKvm(String hostname) {
        return exec(restartCmd, hostname);
    }

    /**
     * 开启KVM
     *
     * @param hostname 主机名
     * @return 是否成功
     */
    public boolean startKvm(String hostname) {
        return exec(startCmd, hostname);
    }

    public Integer checkKvmStatus(String hostname) {

        ArrayList<String> cmds = new ArrayList<>();
        cmds.add("sh");
        cmds.add("-c");
        cmds.add(kvmShell + " " + status + " " + hostname);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(cmds);
        try {
            Process process = processBuilder.start();
            InputStream in = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String s = reader.readLine().trim();
            if (StringUtils.isEmpty(s)) {
                return KvmStatus.UNKNOW;
            }
            int status = Integer.parseInt(s);
            if (KvmStatus.DOWN == status) {
                return KvmStatus.DOWN;
            }
            if (KvmStatus.UP == status) {
                return KvmStatus.UP;
            }
        } catch (IOException e) {
            log.error(String.format("执行命令错误: {cmd: %s}", JSON.toJSONString(cmds)));
            return KvmStatus.UNKNOW;
        }
        return KvmStatus.UNKNOW;
    }
}
