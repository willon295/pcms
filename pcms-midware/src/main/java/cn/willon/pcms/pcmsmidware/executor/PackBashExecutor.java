package cn.willon.pcms.pcmsmidware.executor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * PackBashExecutor
 * 打包命令执行器
 *
 * @author Willon
 * @since 2019-04-14
 */
@Slf4j
@Component
public class PackBashExecutor {


    private static final String TMP_DIR = "/root/tmp/";
    private static final String PACK = "/root/tmp/pack.sh";

    /**
     * ./pack.sh  br7  git@10.0.0.11:member/interest.git   interest  dev
     */
    public void pack(String branchName, String gitUrl, String projectName, String env) {

        String file = TMP_DIR + "/pkg/" + projectName + branchName + ".tar.gz";
        ArrayList<String> cmds = new ArrayList<>();
        cmds.add("sh");
        cmds.add("-c");
        String sb = PACK + " " +
                branchName + " " +
                gitUrl + " " +
                projectName + " " +
                env;
        cmds.add(sb);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(cmds);
        log.info(String.format("打包文件： {command: %s ， 输出文件：%s }", JSON.toJSONString(cmds), file));
    }


}
