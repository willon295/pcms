package cn.willon.pcms.pcmsmidware;

import cn.willon.pcms.pcmsmidware.domain.bean.Changes;
import cn.willon.pcms.pcmsmidware.domain.bean.Kvm;
import cn.willon.pcms.pcmsmidware.domain.bean.User;
import cn.willon.pcms.pcmsmidware.domain.bo.ChangeBO;
import cn.willon.pcms.pcmsmidware.mapper.ChangeMapper;
import cn.willon.pcms.pcmsmidware.mapper.KvmMapper;
import cn.willon.pcms.pcmsmidware.mapper.UserMapper;
import cn.willon.pcms.pcmsmidware.mapper.condition.SaveUserKvmCondition;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
public class PcmsMidwareApplicationTest {


    @Resource
    UserMapper userMapper;

    @Resource
    ChangeMapper changeMapper;

    @Resource
    KvmMapper kvmMapper;


    @Test
    public void testUserMapper() {

        User byUserId = userMapper.findByUserId(2);

        System.out.println(byUserId);
    }

    @Test
    public void testChangeMapper() {
        Changes byChangeId = changeMapper.findByChangeId(1);
        ChangeBO changeBO = new ChangeBO();
        changeBO.setChange(byChangeId);
        List<Integer> kvmIds = byChangeId.getKvmIds();
        for (Integer kvmId : kvmIds) {
            Kvm kvmWithUser = kvmMapper.findKvmWithUser(kvmId);
            changeBO.getKvms().add(kvmWithUser);
        }
        System.out.println(JSON.toJSONString(changeBO, SerializerFeature.DisableCircularReferenceDetect));
    }

    @Test
    public void testKvmMapper() {
        Kvm kvmWithUser = kvmMapper.findKvmWithUser(1);
        System.out.println(JSON.toJSONString(kvmWithUser));
    }


    @Test
    public void saveUserChangesAndSaveUserKvm() {
        // 变更参与的用户
/*        changeMapper.saveUserChange(new SaveUserChangeCondition(1, 1, 1));
        changeMapper.saveUserChange(new SaveUserChangeCondition(3, 1, 0));
        changeMapper.saveUserChange(new SaveUserChangeCondition(4, 1, 0));*/

        // 变更所有的KVM，以及对应的kvm负责人
        kvmMapper.saveUserKvm(new SaveUserKvmCondition(1, 1, "kvm:*:a-br1"));
        kvmMapper.saveUserKvm(new SaveUserKvmCondition(1, 2, "kvm:*:b-br1"));
        kvmMapper.saveUserKvm(new SaveUserKvmCondition(1, 3, "kvm:query:c-br1"));

        kvmMapper.saveUserKvm(new SaveUserKvmCondition(3, 1, "kvm:*:a-br1"));
        kvmMapper.saveUserKvm(new SaveUserKvmCondition(3, 2, "kvm:*:b-br1"));
        kvmMapper.saveUserKvm(new SaveUserKvmCondition(3, 3, "kvm:*:c-br1"));

        kvmMapper.saveUserKvm(new SaveUserKvmCondition(4, 1, "kvm:query:a-br1"));
        kvmMapper.saveUserKvm(new SaveUserKvmCondition(4, 2, "kvm:query:b-br1"));
        kvmMapper.saveUserKvm(new SaveUserKvmCondition(4, 3, "kvm:query:c-br1"));

    }


}
