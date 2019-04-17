package cn.willon.pcms.pcmsmidware.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * ServerVO
 *
 * @author Willon
 * @since 2019-04-17
 */
@Data
public class ServerVO implements Serializable {

    private Integer projectId;
    private String projectName;
    private String serverIp;
    private Integer pubStatus;
    private String permission;
}
