package cn.willon.pcms.pcmsmidware.controller.kvm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

/**
 * KvmManageController
 *
 * @author Willon
 * @since 2019-01-13
 */
@RestController
public class KvmManageController {


    @Value("${kvm.binPath}")
    private String kvmShell;

    @Value("${kvm.cmd.install}")
    private String installCmd;

    @Value("${kvm.cmd.restart}")
    private String restartCmd;

    @Value("${kvm.cmd.delete}")
    private String deleteCmd;

    @GetMapping("/install/{hostname}")
    public boolean install(@PathVariable(name = "hostname") String hostname) {
        return exec(installCmd, hostname);
    }

    @GetMapping("/delete/{hostname}")
    public boolean delete(@PathVariable(name = "hostname") String hostname) {
        return exec(deleteCmd, hostname);
    }


    @GetMapping("/restart/{hostname}")
    public boolean restart(@PathVariable(name = "hostname") String hostname) {
        return exec(restartCmd, hostname);
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
            System.out.println(cmds.toString());
            return true;
        } catch (IOException e) {
            return false;
        }
    }


}
