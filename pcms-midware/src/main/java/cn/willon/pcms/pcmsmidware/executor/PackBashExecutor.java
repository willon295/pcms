package cn.willon.pcms.pcmsmidware.executor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
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


    private static final String GIT_SHELL = "/usr/bin/git";
    private static final String MVN_SHELL = "/usr/local/lib/maven-3.5/bin/mvn";
    private static final String TMP_DIR = "/root/tmp/";


    /**
     * 拉代码
     *
     * @param gitUrl     git地址
     * @param branchName 分支
     * @return 是否获取成功
     */
    public boolean gitClone(String gitUrl, String branchName, String projectName) {

        ArrayList<String> cmds = new ArrayList<>();
        cmds.add("sh");
        cmds.add("-c");
        cmds.add(GIT_SHELL + " clone -b " + branchName + " " + gitUrl + " " + TMP_DIR + projectName);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(cmds);
        try {
            processBuilder.start();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 打包文件
     *
     * @param projectName 工程名
     * @param env         环境
     * @return 是否成功
     */
    public boolean mvnPackage(String projectName, String env) {
        ArrayList<String> cmds = new ArrayList<>();
        cmds.add("sh");
        cmds.add("-c");
        cmds.add("cd " + TMP_DIR + projectName + ";" + MVN_SHELL + " package -P" + env);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(cmds);
        try {
            processBuilder.start();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


    /**
     * 合并分支
     *
     * @param projectName 工程名
     * @param branchName  分支
     * @return 是否成功
     */
    public boolean gitMerge(String projectName, String branchName) {
        ArrayList<String> cmds = new ArrayList<>();
        cmds.add("sh");
        cmds.add("-c");
        cmds.add("cd " + TMP_DIR + projectName + ";" + GIT_SHELL + " merge " + branchName);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(cmds);
        try {
            processBuilder.start();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 移动压缩包，并且清空缓存
     *
     * @param projectName
     * @return
     */
    public boolean movePackageAndRemoveTmp(String projectName) {
        ArrayList<String> cmds = new ArrayList<>();
        cmds.add("sh");
        cmds.add("-c");
        cmds.add("cd " + TMP_DIR + projectName + "; mv *.gz  " + TMP_DIR + "; rm -rf " + TMP_DIR + projectName);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(cmds);
        try {
            processBuilder.start();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
