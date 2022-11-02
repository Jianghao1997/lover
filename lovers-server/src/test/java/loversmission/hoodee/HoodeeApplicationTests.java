package loversmission.hoodee;

import loversmission.hoodee.service.ApiService;
import loversmission.hoodee.service.LoveSentenceService;
import loversmission.hoodee.service.WxUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class HoodeeApplicationTests {

    @Resource
    private LoveSentenceService loveSentenceService;

    @Resource
    private WxUserService wxUserService;

    @Resource
    private ApiService apiService;

    @Test
    void contextLoads() {
    }

    @Test
    void test_getOneLoveSentence() {
        for (int i = 0; i < 10; i++) {
            String str = loveSentenceService.getOneLoveSentence("oBQOW5AK8POUIrtYFDarST2tW69o");
            System.out.println(str);
        }
    }

    @Test
    void test_sendMessage() {
        wxUserService.sendMessage("oBQOW5KwwI7AslPXYeV8gjjFkM1A", "## 给你创建任务咯！");
    }

}
