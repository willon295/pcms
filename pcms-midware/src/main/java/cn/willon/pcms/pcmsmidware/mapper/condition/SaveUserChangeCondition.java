package cn.willon.pcms.pcmsmidware.mapper.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SaveUserChangeCondition
 *
 * @author Willon
 * @since 2019-04-06
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaveUserChangeCondition {
    private Integer userId;
    private Integer changeId;
    private int isOwner;
}
