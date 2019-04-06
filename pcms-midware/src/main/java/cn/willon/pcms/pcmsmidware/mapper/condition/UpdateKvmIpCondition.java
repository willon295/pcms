package cn.willon.pcms.pcmsmidware.mapper.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * UpdateKvmIpCondition
 *
 * @author Willon
 * @since 2019-04-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateKvmIpCondition {

    @NotBlank(message = "hostname 不能为空")
    private String hostname;

    @NotBlank(message = "ip 不能为空")
    private String ip;
}
