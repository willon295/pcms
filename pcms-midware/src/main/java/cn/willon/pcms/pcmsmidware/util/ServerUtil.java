package cn.willon.pcms.pcmsmidware.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * ServerUtil
 *
 * @author Willon
 * @since 2019-04-17
 */
@Slf4j
public class ServerUtil {


    public static boolean isReachable(String ip, String port, int timeout) {
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
