package cn.willon.pcms.pcmsmidware.mapper.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * UpdateKvmDevStatusCondition
 *
 * @author Willon
 * @since 2019-04-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateKvmDevStatusCondition {

    @NotBlank(message = "hostname 不能为空")
    private String hostname;


    private Integer devStatus;
}
