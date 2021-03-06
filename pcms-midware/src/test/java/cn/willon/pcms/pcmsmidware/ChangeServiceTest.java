package cn.willon.pcms.pcmsmidware;

import cn.willon.pcms.pcmsmidware.domain.bo.ChangeKvmsDO;
import cn.willon.pcms.pcmsmidware.service.ChangeService;
import cn.willon.pcms.pcmsmidware.util.Result;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * ChangeServiceTest
 *
 * @author Willon
 * @since 2019-04-06
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ChangeServiceTest {


    @Resource
    ChangeService changeService;





    @Test
    public void test() {

        ChangeKvmsDO changeWithKvms = changeService.changeDetail(28);
        Result result = Result.successResult(changeWithKvms);
        System.out.println(JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect));
    }

}
