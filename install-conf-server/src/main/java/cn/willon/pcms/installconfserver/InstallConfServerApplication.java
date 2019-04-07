package cn.willon.pcms.installconfserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * InstallConfServerApplication
 *
 * @author Willon
 * @since 2019-01-13
 */

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class InstallConfServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(InstallConfServerApplication.class, args);
    }
}
