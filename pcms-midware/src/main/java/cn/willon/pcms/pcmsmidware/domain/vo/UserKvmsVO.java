package cn.willon.pcms.pcmsmidware.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * UserKvmVO
 *
 * @author Willon
 * @since 2019-04-16
 */
@Data
public class UserKvmsVO implements Serializable {

    private Integer userId;
    private List<KvmVO> kvms;
}
