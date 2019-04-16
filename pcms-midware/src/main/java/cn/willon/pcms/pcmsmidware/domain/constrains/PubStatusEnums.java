package cn.willon.pcms.pcmsmidware.domain.constrains;

/**
 * PubStatus
 *
 * @author Willon
 * @since 2019-04-14
 */
public enum PubStatusEnums {

    /**
     * 各种状态
     */
    PENDING(0, "待操作"),

    PACKGING(1, "打包中"),

    DEPLOYING(2, "打包失败"),

    RUNNING(3, "部署中"),

    PACK_FAIL(4, "部署失败"),

    DEPLOY_FAIL(5, "运行中");

    private Integer status;
    private String desc;

    PubStatusEnums(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }

}
