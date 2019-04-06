package cn.willon.pcms.pcmsmidware.domain.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Project
 *
 * @author Willon
 * @since 2019-04-06
 */
@Data
public class Project implements Serializable {

    /**
     * 工程名称
     */
    private Integer projectId;


    private String projectName;
    /**
     * 开发者id
     */
    private List<Integer> developers;
}
