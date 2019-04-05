package cn.willon.pcms.pcmsmidware.domain.bean;

import cn.willon.pcms.pcmsmidware.domain.constrains.UserPositionEnum;
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
@Table(name = "user")
public class User implements Serializable {

    /**
     * 主键
     * <p>
     * isNullAble:0
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    /**
     * 用户名,不可重复
     * isNullAble:1
     */
    @NotBlank(message = "用户名username不能为空")
    @Column(unique = true)
    private String username;

    /**
     * 真实姓名
     * isNullAble:1
     */
    @Column
    @NotBlank(message = "真实姓名realName不能为空")
    private String realName;

    /**
     * 密码
     * isNullAble:1
     */
    @NotBlank(message = "密码password不能为空")
    @Column
    private String password;

    /**
     * 职位类别: (1: 开发，2：测试)
     * isNullAble:1
     *
     * @see UserPositionEnum
     */
    @Column
    private Integer position;

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
