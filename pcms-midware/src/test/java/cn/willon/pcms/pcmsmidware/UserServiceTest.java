package cn.willon.pcms.pcmsmidware;

import cn.willon.pcms.pcmsmidware.domain.bo.UserChangeDO;
import cn.willon.pcms.pcmsmidware.service.UserService;
import cn.willon.pcms.pcmsmidware.util.Result;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * UserServiceTest
 *
 * @author Willon
 * @since 2019-04-07
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {


    @Resource
    UserService userService;


    @Test
    public void test() {

        UserChangeDO userWithChange = userService.findUserWithChange(2);
        Result result = Result.successResult(userWithChange);
        System.out.println(JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect));
    }
}
