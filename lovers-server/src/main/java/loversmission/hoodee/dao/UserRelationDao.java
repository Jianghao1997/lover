package loversmission.hoodee.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import loversmission.hoodee.dao.pre.UserRelationMapper;
import loversmission.hoodee.entity.UserRelation;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Package: loversmission.hoodee.dao.pre
 *
 * @Description：
 * @Author: 蒋豪
 * @Date: 2022.07.25 20:57
 * @Modified By:
 */
@Repository
@AllArgsConstructor
public class UserRelationDao extends ServiceImpl<UserRelationMapper, UserRelation> {

    public UserRelation getRelations(String openId) {
        return super.getOne(Wrappers.<UserRelation>lambdaQuery()
                .eq(UserRelation::getUserOpenId, openId)
                .or()
                .eq(UserRelation::getUserCpOpenId, openId));
    }

    public List<UserRelation> getRelationList(String openId) {
        return super.list(Wrappers.<UserRelation>lambdaQuery()
                .eq(UserRelation::getUserOpenId, openId)
                .or()
                .eq(UserRelation::getUserCpOpenId, openId));
    }

    /**
     * 获取两人有无绑定关系
     * @param openId_1
     * @param openId_2
     * @return
     */
    public UserRelation getTwoPeopleRelation(String openId_1 , String openId_2) {
        List<String> openIds = new ArrayList<>();
        openIds.add(openId_1);
        openIds.add(openId_2);
        return super.getOne(Wrappers.<UserRelation>lambdaQuery()
                .in(UserRelation::getUserOpenId, openIds)
                .in(UserRelation::getUserCpOpenId, openIds));
    }
}
