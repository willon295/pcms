package cn.willon.pcms.pcmsmidware.controller.change;

import cn.willon.pcms.pcmsmidware.domain.dto.SaveChangeDto;
import cn.willon.pcms.pcmsmidware.mapper.ChangeMapper;
import cn.willon.pcms.pcmsmidware.service.ChangeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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


    @PostMapping(value = "/change")
    public void saveChange(@RequestBody SaveChangeDto dto) {


        changeService.saveChange(dto);

    }

    public static void main(String[] args) {


    }
}
