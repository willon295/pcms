package cn.willon.pcms.pcmsmidware.executor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
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


    public   boolean isReachable(String ip, String port, int timeout) {
        boolean reachable;
        // 如果端口为空，使用 isReachable 检测，非空使用 socket 检测
        if (port == null) {
            try {
                InetAddress address = InetAddress.getByName(ip);
                reachable = address.isReachable(timeout);
            } catch (Exception e) {
                log.error(e.getMessage());
                reachable = false;
            }
        } else {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ip, Integer.parseInt(port)), timeout);
                reachable = true;
            } catch (Exception e) {
                log.error(e.getMessage());
                reachable = false;
            }
        }
        return reachable;
    }
}
