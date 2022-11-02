package loversmission.hoodee.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import loversmission.hoodee.dao.pre.WxUserMapper;
import loversmission.hoodee.entity.WxUser;
import org.springframework.stereotype.Repository;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年07月25日 11:36
 */
@Repository
@AllArgsConstructor
public class WxUserDao extends ServiceImpl<WxUserMapper, WxUser> {

    public WxUser getUserInfo(String openId) {
        return super.getOne(Wrappers.<WxUser>lambdaQuery().eq(WxUser::getWxOpenId, openId));
    }

    public WxUser getUserInfoByUserUid(String userUid) {
        return super.getOne(Wrappers.<WxUser>lambdaQuery().eq(WxUser::getWxUserUid, userUid));
    }
}
