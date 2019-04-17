package cn.willon.pcms.pcmsmidware.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * PubCheckVO
 *
 * @author Willon
 * @since 2019-04-17
 */
@Data
public class PubCheckVO implements Serializable {


    private Integer checkId;
    private Integer checkApplyUserId;
    private String checkApplyUsername;

    private Integer checkReceiveUserId;
    private String checkReceiveUsername;


    private Integer checkChangeId;
    private String checkChangeName;


    private Integer checkProjectName;
    /**
     * 审核状态：{-1： 待审核，0：审核不通过，1：审核通过 }
     **/
    private Integer checkStatus;

}
