package cn.willon.pcms.pcmsmidware.domain.bean;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Willon
 */
@Data
@Entity
@Table(name = "changes")
public class Changes implements Serializable {

    private static final long serialVersionUID = 1554444084050L;

    /**
     * 主键
     * <p>
     * isNullAble:0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer changeId;

    /**
     * 变更名称
     * isNullAble:1
     */
    @NotBlank(message = "changeName 不能为空")
    @Column(unique = true)
    private String changeName;

    /**
     * 变更所在分支
     * isNullAble:1
     */
    @Column
    @NotBlank(message = "branchName 不能为空")
    private String branchName;

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
    private Integer isValid = 1;


}
