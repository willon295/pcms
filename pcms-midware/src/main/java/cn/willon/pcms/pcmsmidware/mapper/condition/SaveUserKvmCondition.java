package cn.willon.pcms.pcmsmidware.mapper.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SaveUserKvmCondition
 *
 * @author Willon
 * @since 2019-04-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserKvmCondition {
    private Integer userId;
    private Integer kvmId;
    private String permission;
}
