package cn.willon.pcms.pcmseureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * PcmsEurekaApplication
 *
 * @author Willon
 * @since 2019-04-04
 */
@SpringBootApplication
@EnableEurekaServer
public class PcmsEurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(PcmsEurekaApplication.class, args);
    }
}
