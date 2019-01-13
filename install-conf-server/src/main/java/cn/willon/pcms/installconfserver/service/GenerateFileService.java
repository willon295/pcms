package cn.willon.pcms.installconfserver.service;

import cn.willon.pcms.installconfserver.util.CommonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * GenerateFileService
 * 生成新文件的service
 *
 * @author Willon
 * @since 2019-01-13
 */
@Service
public class GenerateFileService {

    @Value("${generate.filePath.ip}")
    private String ipPath;

    @Value("${generate.filePath.originIfcfg}")
    private String originIfcfgPath;

    @Value("${generate.filePath.newIfcfg}")
    private String newIfcfgPath;

    @Value("${generate.filePath.hostname}")
    private String hostnamePath;


    @Resource
    private CommonUtil commonUtil;

    /**
     * 生成新的主机名文件，ip，网卡配置文件
     *
     * @param hostname 主机名
     */
    public void gernate(String hostname) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(hostnamePath);
            byte[] bytes = hostname.getBytes(Charset.forName("UTF-8"));
            fos.write(bytes);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        generateNetConf();
    }

    private void generateNetConf() {
        // 读取已有的ip
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(ipPath);

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String originIp = bufferedReader.readLine().trim();
            fileReader.close();
            //生成新的IP
            String newIP = commonUtil.increIp(originIp);
            FileWriter fileWriter = new FileWriter(ipPath);
            fileWriter.write(newIP);
            fileWriter.flush();
            // 生成新的 ifcfg文件
            ProcessBuilder processBuilder = new ProcessBuilder();
            String command = "sed  -e  's/NEW_IP/" + newIP + "/g' " + originIfcfgPath + " | cat > " + newIfcfgPath;
            ArrayList<String> cmds = new ArrayList<>();
            cmds.add("sh");
            cmds.add("-c");
            cmds.add(command);
            processBuilder.command(cmds);
            processBuilder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
