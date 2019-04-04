package cn.willon.pcms.pcmsmidware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * PcmsMidwareApplication
 *
 * @author Willon
 * @since 2019-04-04
 */
@EnableEurekaClient
@SpringBootApplication
public class PcmsMidwareApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcmsMidwareApplication.class, args);
    }
}
