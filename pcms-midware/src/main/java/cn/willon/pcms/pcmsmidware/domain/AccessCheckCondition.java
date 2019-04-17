package cn.willon.pcms.pcmsmidware.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * AccessCheckCondition
 *
 * @author Willon
 * @since 2019-04-17
 */
@Data
public class AccessCheckCondition implements Serializable {
    private Integer checkId;
    private String projectName;
    private Integer changeId;
}
