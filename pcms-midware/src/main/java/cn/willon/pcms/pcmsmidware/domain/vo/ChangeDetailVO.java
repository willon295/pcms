package cn.willon.pcms.pcmsmidware.domain.vo;

import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ChangeDetailVO
 *
 * @author Willon
 * @since 2019-04-07
 */
@Data
public class ChangeDetailVO implements Serializable {

    private ChangeVO change;
    private Integer ownerId;
    private String ownerName;
    private List<Kvm> kvms;
}
