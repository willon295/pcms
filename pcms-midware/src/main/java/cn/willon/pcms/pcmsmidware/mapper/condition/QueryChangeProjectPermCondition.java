package cn.willon.pcms.pcmsmidware.mapper.condition;

import lombok.Data;

import java.io.Serializable;

/**
 * QueryChangeProjectPermCondition
 *
 * @author Willon
 * @since 2019-04-14
 */
@Data
public class QueryChangeProjectPermCondition implements Serializable {

    private Integer projectId;
    private Integer changeId;
}
