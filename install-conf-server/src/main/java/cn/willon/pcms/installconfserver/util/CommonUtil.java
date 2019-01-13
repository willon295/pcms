package cn.willon.pcms.installconfserver.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CommonUtil
 *
 * @author Willon
 * @since 2019-01-13
 */
@Component
public class CommonUtil {

    private static final int MAX_IP = 254;

    public String increIp(String ip) {
        List<Integer> collect = Arrays.stream(ip.split("\\.")).map(Integer::valueOf).collect(Collectors.toList());
        Integer[] ints = new Integer[collect.size()];
        Integer[] integers = collect.toArray(ints);
        return increIp(integers, 3);
    }

    private String increIp(Integer[] originIp, int index) {
        // 10.0.0.1
        Integer integer = originIp[index];
        if (integer >= MAX_IP) {
            originIp[index] = 0;
            increIp(originIp, --index);
        }
        originIp[index]++;
        StringBuffer sb = new StringBuffer();
        Arrays.stream(originIp).forEach(r -> sb.append(r.toString()).append("."));
        return sb.substring(0, sb.length() - 1);
    }

}

