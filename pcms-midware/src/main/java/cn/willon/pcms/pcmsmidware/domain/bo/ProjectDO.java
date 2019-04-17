package cn.willon.pcms.pcmsmidware.domain.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * ProjectDO
 *
 * @author Willon
 * @since 2019-04-17
 */
@Data
public class ProjectDO implements Serializable {

    private Integer id;
    private Integer projectId;
    private String  projectName;
    private Integer curChangeId;
    private Integer pubStatus;
    private String   serverIp;

}
