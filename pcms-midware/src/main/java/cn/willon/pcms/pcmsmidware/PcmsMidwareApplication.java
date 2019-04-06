package cn.willon.pcms.pcmsmidware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * PcmsMidwareApplication
 *
 * @author Willon
 * @since 2019-04-04
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "cn.willon.pcms.pcmsmidware.mapper")
public class PcmsMidwareApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcmsMidwareApplication.class, args);
    }
}
