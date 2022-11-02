package loversmission.hoodee.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import loversmission.hoodee.dao.EatMenuDao;
import loversmission.hoodee.entity.EatMenu;
import loversmission.hoodee.entity.pojo.LocalDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年08月24日 16:49
 */
@Slf4j
@Service
public class ApiService {

    @Value("${todayEatApi}")
    private String todayEatApiUrl;

    @Value("${ipLocalApi}")
    private String ipLocalApiUrl;

    @Resource
    private EatMenuDao eatMenuDao;

    private static final String todayEatApi_Token = "947dna1jp1q9l56snv2wqedmd5w19bae";
    private static final String ipLocalApi_Token = "9dfa8b46004f1dca4fcf55f37784b075";
    private static final String Authorization_Type = "apikey";

    /**
     * 调用第三方今日吃什么接口
     * @return
     */
    public List<String> sendTodayEatApiRequest() {
        List<String> result;
        /*
        log.info("开始调用第三方api==>[今日吃什么]");
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("X-APISpace-Token", todayEatApi_Token);
        headersMap.put("Authorization-Type", Authorization_Type);
        String response = HttpRequest.get(todayEatApiUrl).addHeaders(headersMap).execute().body();
        log.info("第三方api==>[今日吃什么],返回结果为{}", response);
        JSONObject responseObject = JSON.parseObject(response);
        if ("200".equals(responseObject.getString("code"))) {
            String data = responseObject.getString("data");
            result = JSON.parseObject(data, List.class);
            if (!result.isEmpty()) {
                for (String item : result) {
                    if (!eatMenuDao.dataExist(item)) {
                        boolean res = eatMenuDao.save(EatMenu.builder().text(item).build());
                        log.info("保存第三方api==>[今日吃什么]数据至数据库中结果：{}", res ? "成功" : "失败");
                    }
                }
            }
        } else {
            log.info("第三方api==>[今日吃什么],超过限制开始调用数据库数据");
        }*/
            List<EatMenu> list = eatMenuDao.list();
            List<String> dataList = Optional.ofNullable(list).orElse(new ArrayList<>()).stream().map(EatMenu::getText).collect(Collectors.toList());
            result = RandomUtil.randomEleSet(dataList, 3).stream().collect(Collectors.toList());
        return result;
    }

    /**
     * 调用ip地址查询接口
     * @param ip
     * @return
     */
    public LocalDTO sendIpLocalApiRequest(String ip) {
        LocalDTO result = LocalDTO.builder().build();
        log.info("开始调用第三方api==>[IP地址查询]");
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("ip", ip);
        paramMap.put("key", ipLocalApi_Token);
        String response = HttpUtil.get(ipLocalApiUrl, paramMap);
        log.info("第三方api==>[IP地址查询],返回结果为{}", response);
        JSONObject responseObject = JSON.parseObject(response);
        if ("200".equals(responseObject.getString("resultcode"))) {
            String data = responseObject.getString("result");
            result = JSON.parseObject(data, LocalDTO.class);
        }
        return result;
    }
}
