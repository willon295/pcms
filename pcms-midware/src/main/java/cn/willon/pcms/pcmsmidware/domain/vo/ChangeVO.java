package cn.willon.pcms.pcmsmidware.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * ChangeVO
 *
 * @author Willon
 * @since 2019-04-07
 */
@Data
public class ChangeVO implements Serializable {

    private Integer changeId;
    private String changeName;
    private String branchName;
    private String attribute;
    private String createDate;
    private String expireDate;

}
