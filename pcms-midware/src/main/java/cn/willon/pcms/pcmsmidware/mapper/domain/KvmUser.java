package cn.willon.pcms.pcmsmidware.mapper.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * KvmUserDO
 *
 * @author Willon
 * @since 2019-04-06
 */
@Data
public class KvmUser implements Serializable {
    private Integer userId;
    private String username;
    private String realName;
    private String permission;
}
