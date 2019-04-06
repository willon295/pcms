package cn.willon.pcms.pcmsmidware.thred;

import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * CreateKvmThread
 *
 * @author Willon
 * @since 2019-04-06
 */
public class CreateKvmThread implements Callable<Boolean> {


    @Value("${kvm.binPath}")
    private String kvmShell;

    @Value("${kvm.cmd.install}")
    private String installCmd;

    @Value("${kvm.cmd.restart}")
    private String restartCmd;

    private String hostname;

    public CreateKvmThread(String hostname) {
        this.hostname = hostname;
    }

    private boolean exec(String cmd, String hostname) {
        ArrayList<String> cmds = new ArrayList<>();
        cmds.add("sh");
        cmds.add("-c");
        cmds.add(kvmShell + " " + cmd + " " + hostname);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(cmds);
        try {
            processBuilder.start();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Boolean call() throws Exception {
        boolean exec = exec(installCmd, hostname);
        return exec;
    }
}
