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
    WATING_CHECK(-1, "等待审核"),

    PENDING(0, "待操作"),

    PACKGING(1, "打包中"),

    PACK_FAIL(2, "打包失败"),

    DEPLOYING(3, "部署中"),

    DEPLOY_FAIL(4, "部署失败"),

    RUNNING(5, "运行中");

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
