package cn.willon.pcms.pcmsmidware.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PublishVO
 *
 * @author Willon
 * @since 2019-04-17
 */
@Data
public class PublishVO implements Serializable {


    /**
     * 变更相关
     */
    private Integer changeId;
    private String changeName;

    /**
     * 服务器相关
     */
    private List<ServerVO> servers;


    /**
     * 我收到的审核
     */
    private List<PubCheckVO> receives;


    /**
     * 我发出的审核
     */
    private List<PubCheckVO> sends;
}
