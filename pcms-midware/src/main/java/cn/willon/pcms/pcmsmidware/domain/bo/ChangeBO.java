package cn.willon.pcms.pcmsmidware.domain.bo;

import cn.willon.pcms.pcmsmidware.domain.bean.Changes;
import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ChangeBO
 *
 * @author Willon
 * @since 2019-04-06
 */
@Data
public class ChangeBO implements Serializable {
    private Changes change;
    private List<Kvm> kvms = new ArrayList<>();
}
