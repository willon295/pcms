package cn.willon.pcms.pcmsmidware.domain.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * UserChangeDO
 *
 * @author Willon
 * @since 2019-04-07
 */
@Data
public class UserChangeDO implements Serializable {

    private Integer userId;
    private String username;
    private String realName;
    private String password;
    private Integer position;
    private List<ChangeDO> changes;
    private Integer totalCount;
}


@Data
class ChangeDO implements Serializable {
    private Integer changeId;
    private Integer isOwner;
    private String changeName;
    private String branchName;
    private String expireDate;
}