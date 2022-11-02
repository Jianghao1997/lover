package loversmission.hoodee.common;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import loversmission.hoodee.dao.UserRelationDao;
import loversmission.hoodee.dao.WxUserDao;
import loversmission.hoodee.entity.UserRelation;
import loversmission.hoodee.entity.WxUser;
import loversmission.hoodee.enums.BusinessExceptionEnum;
import loversmission.hoodee.exception.BusinessException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年08月03日 14:51
 */
@Component
public class UserInfoUtil {

    @Resource
    private UserRelationDao userRelationDao;

    @Resource
    private WxUserDao userDao;

    /**
     * 根据自己的openID获取cp的用户信息
     * @param openID
     * @param bForce 强制获取
     * @return
     */
    public WxUser getCpUserInfo(String openID, Boolean bForce) {
        UserRelation relation = userRelationDao.getOne(Wrappers.<UserRelation>lambdaQuery()
                .eq(UserRelation::getUserOpenId, openID)
                .or()
                .eq(UserRelation::getUserCpOpenId, openID));
        if (ObjectUtil.isNull(relation)) {
            throw new BusinessException(BusinessExceptionEnum.BUSINESS_NORMAL_EXCEPTION.getCode(), "你暂未绑定对象哦，快去绑定一下吧！");
        }
        if (relation.getStatus().compareTo(1) != 0 && !bForce) {
            throw new BusinessException(BusinessExceptionEnum.BUSINESS_NORMAL_EXCEPTION.getCode(), "对方暂未同意绑定关系,或者Broken Up了哦！！");
        }

        String cpUserId;
        if(openID.equals(relation.getUserOpenId())) {
            cpUserId = relation.getUserCpOpenId();
        } else {
            cpUserId = relation.getUserOpenId();
        }
        WxUser user = userDao.getOne(Wrappers.<WxUser>lambdaQuery().eq(WxUser::getWxOpenId, cpUserId));
        if (ObjectUtil.isNull(user)) {
            throw new RuntimeException("oh! 获取对方信息出错了啦！");
        }
        return user;
    }

    /**
     * 获取自己用户信息
     * @param openID
     * @return
     */
    public WxUser getMyselfUserInfo(String openID) {
        WxUser user = userDao.getOne(Wrappers.<WxUser>lambdaQuery().eq(WxUser::getWxOpenId, openID));
        if (ObjectUtil.isNull(user)) {
            throw new RuntimeException("oh! 获取自己信息出错了啦！");
        }
        return user;
    }


    /**
     * 获取用户名称
     * @param openID
     * @return
     */
    public String getUserName(String openID) {
        WxUser user = userDao.getOne(Wrappers.<WxUser>lambdaQuery().select(WxUser::getWxUserName).eq(WxUser::getWxOpenId, openID));
        if (ObjectUtil.isNull(user)) {
            throw new RuntimeException("oh! 系统出错了啦！");
        }
        return user.getWxUserName();
    }
}
