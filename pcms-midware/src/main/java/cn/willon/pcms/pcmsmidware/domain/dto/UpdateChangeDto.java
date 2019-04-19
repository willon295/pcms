package cn.willon.pcms.pcmsmidware.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * UpdateChangeCondition
 *
 * @author Willon
 * @since 2019-04-19
 */
@Data
public class UpdateChangeDto implements Serializable {


    /**
     * ownerId : 2
     * changeName : 变更测试7
     * changeId : 31
     * branchName : br7
     * expireDate : 2019-05-24
     * projects : [{"projectId":3,"projectName":"member-info","users":[3]},{"projectId":2,"projectName":"interest","users":[3,5,6]},{"projectId":2,"projectName":"interest","users":[3,5,6]}]
     */
    private int ownerId;
    private String changeName;
    private int changeId;
    private String branchName;
    private String expireDate;
    private Set<Integer> users;

}
