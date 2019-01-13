package cn.willon.pcms.gitlabapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * GitlabApiApplication
 *
 * @author Willon
 * @since 2019-01-13
 */


@SpringBootApplication
@EnableOAuth2Client
public class GitlabApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitlabApiApplication.class, args);

    }
}
