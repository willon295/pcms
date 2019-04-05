package cn.willon.pcms.pcmsmidware.domain.bean;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * @author Willon
 */
@Data
@Entity
@Table(name = "kvm")
public class Kvm implements Serializable {

    private static final long serialVersionUID = 1554444261306L;

    /**
     * 主键
     * <p>
     * isNullAble:0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer kvmId;

    /**
     * 主机名
     * isNullAble:1
     */
    @NotBlank(message = "hostname 不能为空")
    @Column
    private String hostname;

    /**
     * 对应工程id
     * isNullAble:1
     */
    @NotBlank(message = "hostname 不能为空")
    @Column
    private Integer projectId;

    /**
     * 对应工程名
     * isNullAble:1
     */
    @NotBlank(message = "projectName 不能为空")
    @Column
    private String projectName;

    /**
     * 对应ip
     * isNullAble:1
     */
    @NotBlank(message = "ip 不能为空")
    @Column
    private String ip;

    /**
     * 创建时间(时间戳)
     * isNullAble:1
     */
    @Column
    private Long createDate;

    /**
     * 销毁时间(时间戳)
     * isNullAble:1
     */
    @Column
    private Long expireDate;

    /**
     * 项目环境工程状态
     * isNullAble:1
     */
    @Column
    private Integer devStatus;

    /**
     * 所属变更id
     * isNullAble:1
     */
    @Column
    private Integer changeId;

    /**
     * 扩展字段
     * isNullAble:1
     */
    @Column
    private String attribute = Strings.EMPTY;

    /**
     * 数据是否有效(0: 无效 ; 1：默认有效 )
     * isNullAble:1,defaultVal:1
     */
    @Column
    private Integer isValid;


}
