package cn.willon.pcms.installconfserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * InstallConfServerApplication
 *
 * @author Willon
 * @since 2019-01-13
 */

@EnableEurekaClient
@SpringBootApplication
public class InstallConfServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(InstallConfServerApplication.class, args);
    }
}
