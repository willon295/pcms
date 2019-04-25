package cn.willon.pcms.pcmsmidware.controller;

import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import cn.willon.pcms.pcmsmidware.domain.bean.User;
import cn.willon.pcms.pcmsmidware.domain.bo.UserChangeDO;
import cn.willon.pcms.pcmsmidware.domain.constrains.KvmStatus;
import cn.willon.pcms.pcmsmidware.domain.vo.KvmVO;
import cn.willon.pcms.pcmsmidware.service.KvmService;
import cn.willon.pcms.pcmsmidware.service.UserService;
import cn.willon.pcms.pcmsmidware.util.Result;
import cn.willon.pcms.pcmsmidware.util.ServerUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * UserController
 *
 * @author Willon
 * @since 2019-04-06
 */
@CrossOrigin(allowedHeaders = "*", maxAge = 3600)
@RestController
public class UserController {

    private static final int PORT = 8080;
    private static final int TIMEOUT = 10;
    @Resource
    private UserService userService;

    @Resource
    private KvmService kvmService;

    /**
     * 查看用户的变更列表
     *
     * @param userId 用户Id
     * @return 用户变更
     */
    @GetMapping("/user/{userId}/changes")
    public Result userChange(@PathVariable(name = "userId") Integer userId) {
        UserChangeDO byUserId = userService.findUserWithChange(userId);
        return Result.successResult(byUserId);
    }

    /**
     * 登陆
     *
     * @param user 用户信息
     * @return 返回登陆结果
     */
    @PostMapping("/user/login")
    public Result login(@RequestBody User user) {
        if (Objects.isNull(user)) {
            return Result.failResult("用户信息不能为空");
        }
        User exists = userService.exists(user);
        return Result.successResult(exists);
    }


    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("/user/register")
    public Result register(@RequestBody User user) {
        User valid = userService.checkUser(user);
        if (valid != null) {
            return Result.successResult(valid);
        }
        return Result.failResult("信息不合法");
    }


    @GetMapping("/users")
    public Result users() {
        List<User> users = userService.findAll();
        List<User> collect = users.stream().filter(r -> r.getUserId() > 1).sorted(Comparator.comparing(User::getUsername)).collect(Collectors.toList());
        return Result.successResult(collect);
    }


    @GetMapping("/user/{userId}/kvm")
    public Result viewUserKvms(@PathVariable Integer userId) {

        List<Kvm> kvms = userService.findUserKvms(userId);
        List<KvmVO> collect = kvms.stream().map(k -> {
            String ip = k.getIp();
            String hostname = k.getHostname();
            Integer kvmId = k.getKvmId();
            KvmVO kvmVO = new KvmVO();
            kvmVO.setKvmId(kvmId);
            kvmVO.setHostname(hostname);
            kvmVO.setIp(ip);
            Integer status = kvmService.checkKvmStatus(hostname);
            kvmVO.setStatus(status);
            // 开机， 判断是否应用运行
            if (status == KvmStatus.UP) {
                boolean reachable = ServerUtil.isReachable(ip, PORT, TIMEOUT);
                // 判断虚拟机运行状态
                if (reachable) {
                    kvmVO.setStatus(KvmStatus.RUNNING);
                }
            }
            return kvmVO;
        }).collect(Collectors.toList());
        return Result.successResult(collect);
    }
}
