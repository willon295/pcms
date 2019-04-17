package cn.willon.pcms.pcmsmidware.mapper.condition;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * QueryUserSendPubCheckCnndition
 *
 * @author Willon
 * @since 2019-04-17
 */
@Data
@AllArgsConstructor
public class QueryUserSendPubCheckCondition implements Serializable {


    private Integer changeId;
    private Integer userId;
}
