package cn.willon.pcms.pcmsmidware.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * UpdateKvmDto
 *
 * @author Willon
 * @since 2019-04-19
 */
@Data
public class UpdateKvmDto implements Serializable {

    private Integer projectId;
    private String projectName;
    private String branchName;
    private String expireDate;
    private Integer changeId;
    private Set<Integer> users;

}
