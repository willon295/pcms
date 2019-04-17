package cn.willon.pcms.pcmsmidware.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DevVO
 *
 * @author Willon
 * @since 2019-04-16
 */
@Data
public class DevVO implements Serializable {

    private String changeName;
    private List<KvmVO> kvms;
}
