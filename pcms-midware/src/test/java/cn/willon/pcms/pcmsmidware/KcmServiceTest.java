package cn.willon.pcms.pcmsmidware;

import cn.willon.pcms.pcmsmidware.service.KvmService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * KcmServiceTest
 *
 * @author Willon
 * @since 2019-04-06
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KcmServiceTest {


    @Resource
    KvmService kvmService;

    @Test
    public void testFinishCreateKvm() {
        kvmService.finishCreateKvm("a-br1","10.0.1.1");
    }
}
