package cn.willon.pcms.pcmsmidware.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
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


    private static final String TMP_DIR = "/root/tmp/";

    /**
     * 执行scp命令， 并且执行相关的部署命令
     *
     * @param ip          ip
     * @param projectName 工程名
     */
    public void scpFile(String ip, String projectName) {
        String gzFile = TMP_DIR + projectName + ".tar.gz";
        File file = new File(gzFile);
        if (!file.exists()) {
            log.error(String.format("部署： {ip: %s ,projectName: %s, error: %s 文件不存在}", ip, projectName, gzFile));
        }
        ArrayList<String> cmds = new ArrayList<>();
        cmds.add("sh");
        cmds.add("-c");
        cmds.add("/usr/bin/scp  " + gzFile + "  root@" + ip + ":/opt/app/ ");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(cmds);
        try {
            processBuilder.start();
        } catch (IOException ignored) {
        }
    }


    /**
     * 远程部署
     *
     * @param ip          ip
     * @param projectName 工程名称
     */
    public void remoteDeploy(String ip, String projectName) {
        ArrayList<String> cmds = new ArrayList<>();
        cmds.add("sh");
        cmds.add("-c");
        cmds.add("/usr/bin/ssh  root@" + ip + " 'tar zxf  *.gz -C /opt/app/;  cd  /opt/app/" + projectName + "   ;  nohup java -server -Xms256m -Xmx256m -XX:PermSize=64m -XX:MaxPermSize=128m -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar  /opt/app/" + projectName + "/*.jar  >> `date +%Y%m%d`.log & '");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(cmds);
        try {
            processBuilder.start();
        } catch (IOException ignored) {
        }
    }
}
