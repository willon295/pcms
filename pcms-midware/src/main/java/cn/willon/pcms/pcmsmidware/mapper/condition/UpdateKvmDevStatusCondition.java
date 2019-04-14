package cn.willon.pcms.pcmsmidware.mapper.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * UpdateKvmDevStatusCondition
 *
 * @author Willon
 * @since 2019-04-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateKvmDevStatusCondition implements Serializable {

    private Integer kvmId;

    /**
     * @see cn.willon.pcms.pcmsmidware.domain.constrains.DevStatusEnums
     */
    private Integer devStatus;
}
