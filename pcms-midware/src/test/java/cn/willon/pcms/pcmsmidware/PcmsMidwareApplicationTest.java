package cn.willon.pcms.pcmsmidware;

import cn.willon.pcms.pcmsmidware.domain.bean.Changes;
import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import cn.willon.pcms.pcmsmidware.mapper.ChangeMapper;
import cn.willon.pcms.pcmsmidware.mapper.KvmMapper;
import cn.willon.pcms.pcmsmidware.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * PcmsMidwareApplicationTest
 *
 * @author Willon
 * @since 2019-04-05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PcmsMidwareApplicationTest {


    @Resource
    UserMapper userMapper;

    @Resource
    ChangeMapper changeMapper;

    @Resource
    KvmMapper kvmMapper;


    @Test
    public void testUserMapper() {


    }

    @Test
    public void testChangeMapper() {

        List<Changes> all = changeMapper.findAll();

        log.info(all.toString());

    }

    @Test
    public void testKvmMapper() {
        List<Kvm> all = kvmMapper.findAll();
        System.out.println(all);
    }

}
