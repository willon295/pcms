package cn.willon.pcms.pcmsmidware.domain.constrains;

/**
 * UserPositionEnum
 *
 * @author Willon
 * @since 2019-04-05
 */
public enum UserPositionEnum {

    /**
     * 开发
     */
    DEVELOPER(1, "developer"),

    /**
     * 测试
     */
    TEST(2, "test");

    private Integer type;
    private String desc;

    UserPositionEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
