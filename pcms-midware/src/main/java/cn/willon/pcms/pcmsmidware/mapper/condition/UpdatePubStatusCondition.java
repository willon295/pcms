package cn.willon.pcms.pcmsmidware.mapper.condition;

import lombok.Data;

import java.io.Serializable;

/**
 * UpdatePubStatusCondition
 *
 * @author Willon
 * @since 2019-04-14
 */
@Data
public class UpdatePubStatusCondition implements Serializable {

    private Integer changeId;
    private Integer projectId;
    private Integer pubStatus;

}
