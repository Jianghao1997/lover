package loversmission.hoodee.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import loversmission.hoodee.common.TimeUtils;
import loversmission.hoodee.common.UserInfoUtil;
import loversmission.hoodee.dao.MissionClockDao;
import loversmission.hoodee.dao.MissionInfoDao;
import loversmission.hoodee.dao.UserRelationDao;
import loversmission.hoodee.dao.WxUserDao;
import loversmission.hoodee.entity.MissionClock;
import loversmission.hoodee.entity.MissionInfo;
import loversmission.hoodee.entity.UserRelation;
import loversmission.hoodee.entity.WxUser;
import loversmission.hoodee.enums.BusinessExceptionEnum;
import loversmission.hoodee.enums.MissionStatusEnum;
import loversmission.hoodee.enums.MissionTypeEnum;
import loversmission.hoodee.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 任务相关
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年07月26日 9:39
 */
@Service
public class MissionInfoService {

    Logger logger = LoggerFactory.getLogger(MissionInfoService.class);

    @Resource
    private MissionInfoDao missionInfoDao;

    @Resource
    private WxUserDao userDao;

    @Resource
    private UserRelationDao userRelationDao;

    @Resource
    private UserInfoUtil infoUtil;

    @Resource
    private MissionClockDao missionClockDao;

    @Resource
    private WxUserService wxUserService;

    /**
     * 创建或修改任务
     * @param missionInfo
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean saveOrUpdateInfo(MissionInfo missionInfo) {
        boolean res;
        MissionInfo info = missionInfoDao.getMissionInfo(missionInfo.getMissionID());
        if (ObjectUtil.isNull(info)) {
            Snowflake snowflake = IdUtil.getSnowflake(1, 1);
            long id = snowflake.nextId();
            missionInfo.setMissionID(Convert.toStr(id));
            missionInfo.setCreateTime(new Date());
            res = missionInfoDao.save(missionInfo);
            if (!res) {
                logger.error("保存任务明细至数据库中失败！");
                throw new RuntimeException("任务保存失败啦！");
            }
            // 判断是否为长期任务
            res = insertMissionClock(missionInfo, res, Convert.toStr(id));
            if (res) {
                wxUserService.sendMessage(missionInfo.getCreateUserOpenID(), "## 给你创建了新的任务，快去看看吧！");
            }
        } else {
            if (info.getMissionStatus().compareTo(MissionStatusEnum.FINISHED.getCode()) == 0) {
                throw new RuntimeException("任务已经完成了哦，不能修改啦！");
            }
            MissionClock missionClock = missionClockDao.getMissionClock(missionInfo.getMissionID());
            info.setMissionDesc(missionInfo.getMissionDesc());
            info.setMissionName(missionInfo.getMissionName());
            info.setMissionClassify(missionInfo.getMissionClassify());
            info.setMissionCredit(missionInfo.getMissionCredit());
            info.setMissionType(missionInfo.getMissionType());
            info.setEndTime(missionInfo.getEndTime());
            info.setUpdateTime(new Date());
            res = missionInfoDao.updateById(info);
            if (ObjectUtil.isNotNull(missionClock)) {
                missionClock.setEndTime(missionInfo.getEndTime());
                res = missionClockDao.updateById(missionClock);
            } else {
                res = insertMissionClock(info, res, info.getMissionID());
            }
            if (!res) {
                logger.error("更新任务明细至数据库中失败！");
                throw new RuntimeException("任务更新失败啦！");
            }
        }
        return res;
    }

    /**
     * 添加打卡记录
     * @param missionInfo
     * @param res
     * @param missionId
     * @return
     */
    private boolean insertMissionClock(MissionInfo missionInfo, boolean res, String missionId) {
        if (missionInfo.getMissionType().compareTo(MissionTypeEnum.LONG_TIME.getCode()) == 0) {
            if (missionInfo.getEndTime() == null) {
                throw new RuntimeException("长期任务必须输入任务截止时间哦！");
            }
            // 计算时间差
            Date beginOfDay = DateUtil.beginOfDay(new Date());
            DateTime finalEndTime = DateUtil.offsetDay(missionInfo.getEndTime(), 1);
            missionInfo.setEndTime(finalEndTime);
            long betweenDay = DateUtil.between(beginOfDay, missionInfo.getEndTime(), DateUnit.DAY);
            WxUser userInfo = infoUtil.getCpUserInfo(missionInfo.getCreateUserOpenID(), false);
            if (ObjectUtil.isNull(userInfo)) {

            }
            // 插入打卡表
            MissionClock missionClock = MissionClock.builder()
                    .missionID(missionId)
                    .missionDay((int) betweenDay)
                    .missionUser(userInfo.getWxOpenId())
                    .clockDay(0)
                    .createTime(beginOfDay)
                    .endTime(missionInfo.getEndTime())
                    .build();
            res = missionClockDao.save(missionClock);
            if (!res) {
                logger.error("保存任务打卡明细至数据库中失败！");
                throw new RuntimeException("任务保存失败啦！");
            }
        }
        return res;
    }

    /**
     * 完成任务
     * @param missionId
     * @param openID
     * @param bForce 是否强制完成
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean completeMission(String missionId, String openID, Boolean bForce) {
        MissionInfo info = missionInfoDao.getMissionInfo(missionId);
        if (ObjectUtil.isNull(info)) {
            throw new RuntimeException("该任务不存在哦！");
        }
        if (!openID.equals(info.getCreateUserOpenID())) {
            throw new RuntimeException("不能偷偷完成对方创建的哦！");
        }
        if (info.getMissionStatus().compareTo(MissionStatusEnum.FINISHED.getCode()) == 0) {
            throw new RuntimeException("任务已经完成了哦！");
        }
        // 查看是否为长期任务
        if (info.getMissionType().compareTo(MissionTypeEnum.LONG_TIME.getCode()) == 0) {
            // 获取打卡信息
            MissionClock missionClock = missionClockDao.getMissionClock(info.getMissionID());
            if (ObjectUtil.isNull(missionClock) && !bForce) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_NORMAL_EXCEPTION.getCode(), "没有查到对方打卡信息哦！");
            }
            if (missionClock.getClockDay() != missionClock.getMissionDay() && !bForce) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_NORMAL_EXCEPTION.getCode(), "对方还没有完成打卡哦！");
            }
        }
        WxUser user = infoUtil.getCpUserInfo(openID, false);
        Integer credit = user.getWxUserCredit();
        credit = credit + info.getMissionCredit();
        user.setWxUserCredit(credit);
        boolean res = userDao.updateById(user);
        if (!res) {
            throw new RuntimeException("oh! 添加积分出错了啦！");
        }
        info.setMissionStatus(MissionStatusEnum.FINISHED.getCode());
        info.setCompleteUserOpenID(openID);
        info.setCompleteTime(new Date());
        res = missionInfoDao.updateById(info);
        if (!res) {
            throw new RuntimeException("oh! 完成任务出错了啦！");
        }
        wxUserService.sendMessage(openID, String.format("任务：[%s]已经完成啦！成功获得：%s 积分，当前一共有：%s 积分，## 觉得你真的很棒棒，后面的任务要继续加油哦！", info.getMissionName(), info.getMissionCredit() + "", user.getWxUserCredit() + ""));
        return true;
    }

    /**
     * 获取两个人之间的任务 （可根据状态）
     * @param openID
     * @param status
     * @param searchValue
     * @return
     */
    public List<MissionInfo> getMissionInfoList(String openID, Integer status, String searchValue) {
        List<String> openIds = new ArrayList<>(2);
        UserRelation relation = userRelationDao.getRelations(openID);
        if (ObjectUtil.isNotNull(relation)) {
            if (relation.getStatus().compareTo(1) != 0) {
                openIds.add(openID);
            } else {
                openIds.add(relation.getUserOpenId());
                openIds.add(relation.getUserCpOpenId());
            }
        } else {
            openIds.add(openID);
        }
        List<MissionInfo> missionInfos = missionInfoDao.getMissionInfoList(openIds, status, searchValue);
        Optional.ofNullable(missionInfos).orElse(new ArrayList<>()).stream().forEach(p -> {
            if (p.getMissionType().compareTo(MissionTypeEnum.LONG_TIME.getCode()) == 0) {
                // 获取打卡信息
                MissionClock missionClock = missionClockDao.getMissionClock( p.getMissionID());
                if (ObjectUtil.isNotNull(missionClock)) {
                    p.setEndTime(missionClock.getEndTime());
                    p.setClockDay(missionClock.getClockDay());
                }
            }
            fillUserName(p);
        });
        return missionInfos;
    }


    /**
     * 获取具体任务详情
     * @param missionId
     * @return
     */
    public MissionInfo getMissionInfoById(String missionId) {
        MissionInfo missionInfo = missionInfoDao.getMissionInfo(missionId);
        if (ObjectUtil.isNotNull(missionInfo)) {
            // 查看是否为长期任务
            if (missionInfo.getMissionType().compareTo(MissionTypeEnum.LONG_TIME.getCode()) == 0) {
                // 获取打卡信息
                MissionClock missionClock = missionClockDao.getMissionClock(missionInfo.getMissionID());
                if (ObjectUtil.isNotNull(missionClock)) {
                    missionInfo.setEndTime(missionClock.getEndTime());
                    missionInfo.setClockDay(missionClock.getClockDay());
                } else {
                    logger.error("没有查到任务ID：{},打卡信息！", missionInfo.getMissionID());
                }
            }
            fillUserName(missionInfo);
            return missionInfo;
        } else {
            throw new RuntimeException("oh! 没找到这个任务！");
        }
    }

    /**
     * 填充用户名称
     * @param missionInfo
     */
    private void fillUserName(MissionInfo missionInfo) {
        if (StringUtils.isNotBlank(missionInfo.getCompleteUserOpenID())) {
            String completeUserName = infoUtil.getUserName(missionInfo.getCompleteUserOpenID());
            missionInfo.setCompleteUserName(completeUserName);
        }
        if (StringUtils.isNotBlank(missionInfo.getCreateUserOpenID())) {
            String createUserName = infoUtil.getUserName(missionInfo.getCreateUserOpenID());
            missionInfo.setCreateUserName(createUserName);
        }
    }

    /**
     * 长期任务打卡
     * @param missionId
     * @param openId
     * @return
     */
    public boolean clockMission(String missionId, String openId) {
        MissionInfo missionInfo = missionInfoDao.getOne(Wrappers.<MissionInfo>lambdaQuery()
                .eq(MissionInfo::getMissionID, missionId));
        if (ObjectUtil.isNotNull(missionInfo)) {
            if (missionInfo.getCreateUserOpenID().equals(openId)) {
                throw new RuntimeException("oh! 你不可以为对方打卡哦！");
            }
            // 查看是否为长期任务
            if (missionInfo.getMissionType().compareTo(MissionTypeEnum.LONG_TIME.getCode()) == 0) {
                boolean res = false;
                // 获取打卡信息
                MissionClock missionClock = missionClockDao.getMissionClock(missionInfo.getMissionID());
                Date endTime = missionClock.getEndTime();
                DateTime endOfDay = DateUtil.endOfDay(endTime);
                if (ObjectUtil.isNotNull(missionClock)) {
                    int clockCount = missionClock.getClockDay();
                    clockCount++;
                    if (clockCount > missionClock.getMissionDay()) {
                        throw new RuntimeException("oh! 这个任务打卡已完成哦！");
                    } else if (new Date().compareTo(endOfDay) > 0) {
                        throw new RuntimeException("oh! 已超过打卡截止时间哦！");
                    } else if (missionClock.getUpdateTime() != null && TimeUtils.judgeTimeIsToday(missionClock.getUpdateTime())) {
                        throw new RuntimeException("oh! 今天已经打卡哦！");
                    } else {
                        missionClock.setClockDay(clockCount);
                        missionClock.setUpdateTime(new Date());
                        res = missionClockDao.updateById(missionClock);
                    }
                } else {
                    logger.error("没有查到任务ID：{},打卡信息！", missionInfo.getMissionID());
                }
                if (res) {
                    WxUser userInfo = infoUtil.getMyselfUserInfo(openId);
                    wxUserService.sendMessage(openId, String.format("[%s] 任务对方今日打卡完成咯！已连续打卡：%s 天，快去好好鼓励一下%s吧！后续也要加油哦！", missionInfo.getMissionName(), missionClock.getClockDay() + "", userInfo.getWxUserNickName()));
                }
                return res;
            } else {
                throw new RuntimeException("oh! 这个任务不需要打卡哦！");
            }
        } else {
            throw new RuntimeException("oh! 没找到这个任务！");
        }
    }

    /**
     * 删除商品
     * @param missionId
     * @param openID
     * @return
     */
    public boolean deleteMission(String missionId, String openID) {
        MissionInfo missionInfo = missionInfoDao.getMissionInfo(missionId);
        if (ObjectUtil.isNotNull(missionInfo)) {
            if (!openID.equals(missionInfo.getCreateUserOpenID())) {
                throw new RuntimeException("不能删除对方的任务哦！");
            }
            missionInfo.setUpdateTime(new Date());
            missionInfo.setMissionStatus(MissionStatusEnum.CANCEL.getCode());
            return missionInfoDao.updateById(missionInfo);
        } else {
            throw new RuntimeException("oh! 没找到这个任务！");
        }
    }
}
