package cn.willon.pcms.installconfserver.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * RedisServiceTest
 *
 * @author Willon
 * @since 2019-01-13
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisServiceTest {


    @Resource
    private RedisService redisService;

    @Test
    public void lock() {
        boolean hhhhhh = redisService.lock("hhhhhh");
        System.out.println(hhhhhh);
    }
}