package cn.willon.pcms.pcmsmidware.mapper.condition;

import lombok.Data;

import java.io.Serializable;

/**
 * QueryDevUserKvmCondition
 *
 * @author Willon
 * @since 2019-04-16
 */
@Data
public class QueryHasPermissionKvmCondition implements Serializable {

    private Integer userId;
    private Integer changeId;
}
