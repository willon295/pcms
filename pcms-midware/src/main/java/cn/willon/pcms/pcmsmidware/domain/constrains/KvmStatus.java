package cn.willon.pcms.pcmsmidware.domain.constrains;

/**
 * KvmStatus
 *
 * @author Willon
 * @since 2019-04-19
 */
public interface KvmStatus {

    /**
     * 关机状态
     */
    int DOWN = 0;

    /**
     * 开机状态
     */
    int UP = 1;

    /**
     * App运行状态
     */
    int RUNNING = 2;
}
