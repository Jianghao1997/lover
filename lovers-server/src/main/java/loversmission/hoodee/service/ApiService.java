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

    @Resource
    private EatMenuDao eatMenuDao;

    /**
     *
     * @return
     */
    public List<String> sendTodayEatApiRequest() {
        List<String> result;
            List<EatMenu> list = eatMenuDao.list();
            List<String> dataList = Optional.ofNullable(list).orElse(new ArrayList<>()).stream().map(EatMenu::getText).collect(Collectors.toList());
            result = RandomUtil.randomEleSet(dataList, 3).stream().collect(Collectors.toList());
        return result;
    }
}
