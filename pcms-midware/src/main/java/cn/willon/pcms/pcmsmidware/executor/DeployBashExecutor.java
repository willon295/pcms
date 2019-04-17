package cn.willon.pcms.pcmsmidware.executor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

/**
 * DevBashExecutor
 *
 * @author Willon
 * @since 2019-04-14
 */
@Slf4j
@Component
public class DeployBashExecutor {


    private static final String DEV_SHELL = "/root/tmp/deploy.sh";

    public void deploy(String projectName, String branchName, String ip) throws IOException {
        ArrayList<String> cmds = new ArrayList<>();
        cmds.add("sh");
        cmds.add("-c");
        String sb = DEV_SHELL + " " +
                projectName + " " +
                branchName + " " +
                ip + " ";
        cmds.add(sb);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(cmds);
        log.info(String.format("部署命令： {command: %s }", JSON.toJSONString(cmds)));

        processBuilder.start();


    }



}
