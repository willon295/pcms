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

    DEPLOYING(2, "部署中"),

    RUNNING(3, "运行中"),

    PACK_FAIL(4, "打包失败"),

    DEPLOY_FAIL(5, "部署失败");

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
