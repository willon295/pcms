package cn.willon.pcms.pcmsmidware.domain.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * PubCheck
 *
 * @author Willon
 * @since 2019-04-17
 */
@Data
public class PubCheck implements Serializable {

    private Integer checkId;
    private Integer checkApplyUserId;
    private Integer checkReceiveUserId;
    private Integer checkChangeId;

    private String checkProjectName;
    /**
     * 审核状态：{-1： 待审核，0：审核不通过，1：审核通过 }
     **/
    private Integer checkStatus;
}
