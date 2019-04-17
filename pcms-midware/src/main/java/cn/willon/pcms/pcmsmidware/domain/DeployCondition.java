package cn.willon.pcms.pcmsmidware.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * DeployCondition
 *
 * @author Willon
 * @since 2019-04-17
 */
@Data
public class DeployCondition implements Serializable {

    /**
     * publish  , dev
     */
    private String env;


    /**
     * 主机名
     */
    private String hostname;

    /**
     * 分支
     */
    private String branchName;


    /**
     * 打包的用户
     */
    private Integer userId;


    /**
     * 变更
     */
    private Integer changeId;
}
