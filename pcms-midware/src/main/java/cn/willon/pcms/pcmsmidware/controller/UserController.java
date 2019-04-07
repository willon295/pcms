package cn.willon.pcms.pcmsmidware.controller;

import cn.willon.pcms.pcmsmidware.domain.bean.User;
import cn.willon.pcms.pcmsmidware.domain.bo.UserChangeDO;
import cn.willon.pcms.pcmsmidware.service.UserService;
import cn.willon.pcms.pcmsmidware.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * UserController
 *
 * @author Willon
 * @since 2019-04-06
 */
@CrossOrigin(allowedHeaders = "*", maxAge = 3600)
@RestController
public class UserController {

    @Resource
    private UserService userService;

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
        User exists = userService.exists(user);
        return Result.successResult(exists);
    }

}