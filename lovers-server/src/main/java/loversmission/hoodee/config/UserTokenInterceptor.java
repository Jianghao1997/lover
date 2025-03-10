package loversmission.hoodee.config;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import loversmission.hoodee.common.CommonUtils;
import loversmission.hoodee.dao.WxUserDao;
import loversmission.hoodee.entity.WxUser;
import loversmission.hoodee.entity.pojo.LocalDTO;
import loversmission.hoodee.service.ApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年08月25日 14:25
 */
@Service
@Slf4j
public class UserTokenInterceptor implements HandlerInterceptor {

    @Resource
    private WxUserDao wxUserDao;

    private static ExecutorService executor = ExecutorBuilder.create()
            .setCorePoolSize(10)
            .setMaxPoolSize(20)
            .setWorkQueue(new LinkedBlockingQueue<>(100))
            .build();

    /**
     * 简单记录一下最后使用时间
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String openID = request.getHeader("openID");
        if (StringUtils.isNotBlank(openID)) {
            executor.execute(() -> {
                updateUserInfo(openID);
            });
        }
        return true;
    }

    private void updateUserInfo(String openID) {
        WxUser userInfo = wxUserDao.getUserInfo(openID);
        if (ObjectUtil.isNotNull(userInfo)) {
            userInfo.setLastLoginTime(new Date());
            wxUserDao.updateById(userInfo);
        }
    }
}
