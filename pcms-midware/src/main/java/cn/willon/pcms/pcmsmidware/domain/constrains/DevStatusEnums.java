package cn.willon.pcms.pcmsmidware.domain.constrains;

/**
 * DevStatus
 * 开发的Kvm应用状态
 *
 * @author Willon
 * @since 2019-04-06
 */
public enum DevStatusEnums {


    /**
     * 各种状态
     */
    INSTALLING(-1, "环境创建中"),

    // DEPLOY_FAIL
    PENDING(0, "待操作"),

    PACKGING(1, "打包中"),

    PACK_FAIL(2, "打包失败"),

    DEPLOYING(3, "部署中"),

    DEPLOY_FAIL(4, "部署失败"),

    RUNNING(5, "运行中"),

    TEST_PASS(6, "测试通过"),

    MASTER_UPDATE(7, "master代码更新");

    private Integer status;
    private String desc;

    DevStatusEnums(Integer status, String desc) {
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
