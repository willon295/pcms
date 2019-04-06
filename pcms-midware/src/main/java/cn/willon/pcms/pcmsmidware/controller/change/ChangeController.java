package cn.willon.pcms.pcmsmidware.controller.change;

import cn.willon.pcms.pcmsmidware.domain.bean.Project;
import cn.willon.pcms.pcmsmidware.domain.dto.SaveChangeDto;
import cn.willon.pcms.pcmsmidware.service.ChangeService;
import cn.willon.pcms.pcmsmidware.service.KvmService;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * ChangeController
 *
 * @author Willon
 * @since 2019-04-06
 */
@RestController
public class ChangeController {


    @Resource
    private ChangeService changeService;

    @Resource
    private KvmService kvmService;

    @PostMapping(value = "/change")
    public void saveChange(@RequestBody SaveChangeDto dto) {
        changeService.saveChange(dto);
        // 开始异步创建Kvm
        String branchName = dto.getBranchName();
        ArrayList<String> hostnames = Lists.newArrayList();
        List<Project> projects = dto.getProjects();
        for (Project project : projects) {
            String hostname = project.getProjectName();
            hostname = hostname + "-" + branchName;
            hostnames.add(hostname);
        }
        kvmService.createKvmAsync(hostnames);
    }


}
