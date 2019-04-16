package cn.willon.pcms.pcmsmidware.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * KvmVO
 *
 * @author Willon
 * @since 2019-04-16
 */

@Data
public class KvmVO implements Serializable {
    private Integer kvmId;
    private String hostname;
    private Integer projectId;
    private String projectName;
    private String ip;
    private Integer devStatus;
    private String createDate;
    private String expireDate;
    private String permission;
}