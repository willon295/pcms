package cn.willon.pcms.pcmsmidware;

import cn.willon.pcms.pcmsmidware.dao.UserDao;
import cn.willon.pcms.pcmsmidware.domain.bean.User;
import cn.willon.pcms.pcmsmidware.domain.constrains.UserPositionEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * PcmsMidwareApplicationTest
 *
 * @author Willon
 * @since 2019-04-05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PcmsMidwareApplicationTest {


    @Resource
    UserDao userDao;

    @Test
    public void test() {
        User user = new User();
        user.setUsername("tiangua");
        user.setRealName("甜瓜");
        user.setPassword("tiangua");
        user.setPosition(UserPositionEnum.TEST.getType());
        User save = userDao.save(user);
        System.out.println(save);
    }
}
