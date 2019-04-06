package cn.willon.pcms.pcmsmidware.domain.dto;

import cn.willon.pcms.pcmsmidware.domain.bean.Project;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * SaveChangeDto
 *
 * @author Willon
 * @since 2019-04-06
 */
@Data
public class SaveChangeDto implements Serializable {

    /**
     * 变更名称
     */
    private String changeName;
    /**
     * 分支名称
     */
    private String branchName;

    /**
     * 结束日期
     */
    private String endDate;


    /**
     * 工程
     */
    private List<Project> projects;

}
