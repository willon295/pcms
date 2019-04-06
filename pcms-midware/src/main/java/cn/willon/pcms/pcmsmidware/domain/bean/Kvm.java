package cn.willon.pcms.pcmsmidware.domain.bean;

import cn.willon.pcms.pcmsmidware.mapper.domain.KvmUser;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author Willon
 */
@Data

public class Kvm implements Serializable {

    private static final long serialVersionUID = 1554444261306L;

    /**
     * 主键
     * <p>
     * isNullAble:0
     */

    private Integer kvmId;

    /**
     * 主机名
     * isNullAble:1
     */
    @NotBlank(message = "hostname 不能为空")
    private String hostname;

    /**
     * 对应工程id
     * isNullAble:1
     */
    @NotBlank(message = "hostname 不能为空")
    private Integer projectId;

    /**
     * 对应工程名
     * isNullAble:1
     */
    @NotBlank(message = "projectName 不能为空")
    private String projectName;

    /**
     * 对应ip
     * isNullAble:1
     */
    private String ip;

    /**
     * 创建时间(时间戳)
     * isNullAble:1
     */
    private Long createDate;

    /**
     * 销毁时间(时间戳)
     * isNullAble:1
     */
    private Long expireDate;

    /**
     * 项目环境工程状态
     * isNullAble:1
     */
    private Integer devStatus;

    /**
     * 所属变更id
     * isNullAble:1
     */
    private Integer changeId;

    /**
     * 扩展字段
     * isNullAble:1
     */
    private String attribute = Strings.EMPTY;

    /**
     * 数据是否有效(0: 无效 ; 1：默认有效 )
     * isNullAble:1,defaultVal:1
     */
    private Integer isValid = 1;

    /**
     * 拥有变更查看和操作权限的用户
     */
    private List<KvmUser> users;
}
