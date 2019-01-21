package cn.willon.pcms.gitlabapi.conf;

import lombok.Data;
import org.gitlab.api.AuthMethod;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.TokenType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GitlabConf
 *
 * @author Willon
 * @since 2019-01-20
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "gitlab")
public class GitlabConf {

    private String privateToken;
    private String host;

    @Bean
    public GitlabAPI gitlabAPI() {
        GitlabAPI connect = GitlabAPI.connect(host, privateToken, TokenType.PRIVATE_TOKEN, AuthMethod.HEADER);
        return connect;
    }

}
