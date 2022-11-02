package loversmission.hoodee.controller;

import lombok.extern.slf4j.Slf4j;
import loversmission.hoodee.common.Result;
import loversmission.hoodee.service.ApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description：第三方api
 * Author: jianghao
 * Date:  2022.04.09 17:27
 * Modified By:
 */
@Slf4j
@RestController
@RequestMapping("api")
public class OrtherApiController {

    @Resource
    private ApiService apiService;

    @GetMapping(value = "/getTodayEat")
    public Result<List<String>> getTodayEat() {
        List<String> result = apiService.sendTodayEatApiRequest();
        return Result.success(result);
    }

}
