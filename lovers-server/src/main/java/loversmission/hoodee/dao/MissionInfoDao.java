package loversmission.hoodee.dao;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import loversmission.hoodee.dao.pre.MissionInfoMapper;
import loversmission.hoodee.entity.MissionInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年07月26日 9:37
 */
@Repository
@AllArgsConstructor
public class MissionInfoDao extends ServiceImpl<MissionInfoMapper, MissionInfo> {

    public MissionInfo getMissionInfo(String missionId) {
        return super.getOne(Wrappers.<MissionInfo>lambdaQuery()
                .eq(MissionInfo::getMissionID, missionId));
    }

    public List<MissionInfo> getMissionInfoList(List<String> openIDs, Integer status, String searchValue) {
        return super.list(Wrappers.<MissionInfo>lambdaQuery()
                .in(MissionInfo::getCreateUserOpenID, openIDs)
                .eq(ObjectUtil.isNotNull(status), MissionInfo::getMissionStatus, status)
                .like(StringUtils.isNotBlank(searchValue), MissionInfo::getMissionName, searchValue)
                .orderByDesc(status == 0 ? MissionInfo::getCreateTime : MissionInfo::getCompleteTime));
    }
}
