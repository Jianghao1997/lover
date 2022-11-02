package loversmission.hoodee.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import loversmission.hoodee.dao.pre.MissionClockMapper;
import loversmission.hoodee.entity.MissionClock;
import org.springframework.stereotype.Repository;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年07月26日 9:37
 */
@Repository
@AllArgsConstructor
public class MissionClockDao extends ServiceImpl<MissionClockMapper, MissionClock> {

    public MissionClock getMissionClock(String missionId) {
        return super.getOne(Wrappers.<MissionClock>lambdaQuery().eq(MissionClock::getMissionID, missionId));
    }
}
