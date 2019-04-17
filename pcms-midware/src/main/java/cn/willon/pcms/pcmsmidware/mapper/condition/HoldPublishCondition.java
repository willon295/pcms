package cn.willon.pcms.pcmsmidware.mapper.condition;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * HoldPublishCondition
 *
 * @author Willon
 * @since 2019-04-17
 */
@Data
@AllArgsConstructor
public class HoldPublishCondition implements Serializable {

    private String projectName;
    private Integer changeId;
}
