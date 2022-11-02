package loversmission.hoodee.service;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import loversmission.hoodee.common.UserInfoUtil;
import loversmission.hoodee.common.ids.IIdGenerator;
import loversmission.hoodee.dao.UserImageDao;
import loversmission.hoodee.dao.UserRelationDao;
import loversmission.hoodee.dao.WxUserDao;
import loversmission.hoodee.entity.UserImage;
import loversmission.hoodee.entity.UserRelation;
import loversmission.hoodee.entity.WxUser;
import loversmission.hoodee.entity.pojo.*;
import loversmission.hoodee.enums.Ids;
import loversmission.hoodee.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户相关
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年07月25日 11:30
 */
@Service
public class WxUserService {

    static Logger logger = LoggerFactory.getLogger(WxUserService.class);

    @Resource
    private WxUserDao userDao;

    @Resource
    private UserRelationDao userRelationDao;

    @Resource
    private UserImageDao userImageDao;

    @Resource
    private UserInfoUtil userInfoUtil;

    @Resource
    private Map<Ids, IIdGenerator> idGeneratorMap;

    private static String APPID = "wx7cf98f884ad76df1";

    private static String SECRET = "8abc0c3935d952814488393f87455987";

    private static String TEMPALTE_ID = "6kI4R7rSu_BHPHd2TZDGHwqB-OhP7F3xMV9nv7swe70";

    private static String UID_PREFIX = "LOVERS";

    public static TimedCache<String, String> accessTokenCache = CacheUtil.newTimedCache(7000);


    /**
     * 获取微信openID
     * @param code
     * @return
     */
    public String loginByWx(String code) {
        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", APPID, SECRET, code);
        String res = HttpUtil.get(url);
        logger.info("请求微信api获取用户openID，微信返回结果为：{}", res);
        WxResPojo wxResPojo = JSON.parseObject(res, WxResPojo.class);
        if (ObjectUtil.isNull(wxResPojo.getOpenid())) {
            throw new RuntimeException("获取授权时出错啦！");
        }
        return wxResPojo.getOpenid();
    }

    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token,本地缓存7000ms
     * @return
     */
    public String getWxAccessToken() {
        try {
            logger.info("请求微信api获取用户access_token，流程开始=====>");
            if (StringUtils.isNotBlank(accessTokenCache.get("accessToken"))) {
                logger.info("读取本地缓存，流程正常结束=====>");
                return accessTokenCache.get("accessToken");
            }
            String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", APPID, SECRET);
            String res = HttpUtil.get(url);
            logger.info("请求微信api获取用户access_token，微信可返回结果：{}", res);
            JSONObject resObject = JSON.parseObject(res);
            String accessToken = resObject.getString("access_token");
            if (StringUtils.isNotBlank(accessToken)) {
                logger.info("存放本地缓存中...");
                accessTokenCache.put("accessToken", accessToken);
                logger.info("存放本地缓存结束，流程正常结束=====>");
            }
            return accessToken;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("请求微信api获取用户AccessToken异常，异常原因：{}", ex.getMessage());
            return null;
        }
    }

    /**
     * 发送订阅消息
     * @param openId
     * @param msg
     */
    public void sendMessage(String openId, String msg) {
        try {
            LocalDateTime now = LocalDateTime.now();
            String nowTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            WxUser user = getUserInfoByOpenID(openId);
            WxUser cpUserInfo = userInfoUtil.getCpUserInfo(openId, true);
            String cpOpenId = cpUserInfo.getWxOpenId();
            // 获取对方的昵称
            String cpNick = StringUtils.isNotBlank(cpUserInfo.getWxUserNickName()) ? cpUserInfo.getWxUserNickName() : "你的宝贝";
            String myselfNick = StringUtils.isNotBlank(user.getWxUserNickName()) ? user.getWxUserNickName() : "你的宝贝";
            String finalSendMessage = msg.replace("$$", cpNick).replace("##", myselfNick);

            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("touser", cpOpenId);
            messageMap.put("template_id", TEMPALTE_ID);
            Name2 name2 = Name2.builder().value(user.getWxUserNickName()).build();
            Thing3 thing3 = Thing3.builder().value(finalSendMessage).build();
            Time4 time4 = Time4.builder().value(nowTime).build();
            MsgData data = MsgData.builder().name2(name2).thing3(thing3).time4(time4).build();
            messageMap.put("data", data);
            logger.info("请求微信api发送订阅消息时，请求参数：{}", JSON.toJSONString(messageMap));
            String accessToken = getWxAccessToken();
            if (StringUtils.isNotBlank(accessToken)) {
                String url = String.format("https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=%s", accessToken);
                String res = HttpUtil.post(url, JSON.toJSONString(messageMap));
                JSONObject resObject = JSON.parseObject(res);
                logger.info("请求微信api发送订阅消息时，返回响应：{}", resObject.toJSONString());
                String msgInfo = resObject.getString("errmsg");
                String code = resObject.getString("errcode");
                if (StringUtils.isNotBlank(msgInfo) && !"0".equals(code)) {
                    logger.error("请求微信api发送订阅消息时，微信返回失败，失败原因：{}，开始发送客服消息！", msgInfo);
                    // 如果出错了发送客户消息
                    // sendCustomerMessage(cpOpenId, finalSendMessage); 发送客户消息比较鸡肋
                }
            }
        } catch (Exception ex) {
            logger.error("请求微信api发送订阅消息异常，异常原因：{}", ex.getMessage());
        }
    }

    /**
     * 发送客服消息
     * @param cpOpenId
     * @param msg
     */
    private void sendCustomerMessage(String cpOpenId, String msg) {
        try {
            // String mediaId = uploadMediaToWechat();
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("touser", cpOpenId);
            messageMap.put("msgtype", "text");
            Map<String, Object> textMap = new HashMap<>();
            textMap.put("content", msg);
            // MiniProgramPageDTO miniProgramPageDTO = MiniProgramPageDTO.builder().title(msg).pagepath("pages/mainPage/index").thumb_media_id(mediaId).build();
            messageMap.put("text", textMap);
            logger.info("请求微信api发送客服消息时，请求参数：{}", JSON.toJSONString(messageMap));
            String accessToken = getWxAccessToken();
            if (StringUtils.isNotBlank(accessToken)) {
                String url = String.format("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s", accessToken);
                String res = HttpUtil.post(url, JSON.toJSONString(messageMap));
                JSONObject resObject = JSON.parseObject(res);
                logger.info("请求微信api发送客服消息时，返回响应：{}", resObject.toJSONString());
                String msgInfo = resObject.getString("errmsg");
                String code = resObject.getString("errcode");
                if (StringUtils.isNotBlank(msgInfo) && !"0".equals(code)) {
                    logger.error("请求微信api发送客服消息时，微信返回失败，失败原因：{}",msgInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("请求微信api发送客服消息异常，异常原因：{}", ex.getMessage());
        }
    }

    /**
     * 保存微信用户的基本信息
     * @param user
     * @return
     */
    public Boolean saveUserInfo(WxUser user) {
        WxUser wxUser = userDao.getOne(Wrappers.<WxUser>lambdaQuery()
                .eq(WxUser::getWxOpenId, user.getWxOpenId()));
        if (ObjectUtil.isNotNull(wxUser)) {
            wxUser.setWxUserSex(user.getWxUserSex());
            wxUser.setWxAvatarUrl(user.getWxAvatarUrl());
            wxUser.setWxUserName(user.getWxUserName());
            wxUser.setWxOpenId(user.getWxOpenId());
            wxUser.setLastLoginTime(new Date());
            return userDao.updateById(wxUser);
        }
        String uid = makeUserUid();
        user.setWxUserUid(uid);
        user.setWxUserNickName(user.getWxUserName());
        user.setLastLoginTime(new Date());
        return userDao.save(user);
    }

    /**
     * 根据自己openID获取自己以及对象的用户信息
     * @param openID
     * @return
     */
    public List<WxUser> getUserRelationsInfoByOpenID(String openID) {
        UserRelation relation = userRelationDao.getRelations(openID);
        if (ObjectUtil.isNull(relation)) {
            return null;
        }
        if (relation.getStatus().compareTo(1) != 0) {
            throw new RuntimeException("对方暂未同意绑定关系,或者Broken Up了哦！");
        }
        List<String> openIds = new ArrayList<>(2);
        openIds.add(relation.getUserOpenId());
        openIds.add(relation.getUserCpOpenId());
        return userDao.list(Wrappers.<WxUser>lambdaQuery()
                .in(WxUser::getWxOpenId, openIds));
    }

    /**
     * 获取用户信息
     * @param openID
     * @return
     */
    public WxUser getUserInfoByOpenID(String openID) {
        return userDao.getOne(Wrappers.<WxUser>lambdaQuery()
                .eq(WxUser::getWxOpenId, openID));
    }

    /**
     * 获取情侣之间上传的照片 以及个人的上传的
     * @param openID
     * @return
     */
    public List<UserImage> getCpImages(String openID) {
        List<String> cpOpenIDs = new ArrayList<>();
        cpOpenIDs.add(openID);
        try {
            WxUser cpUserInfo = userInfoUtil.getCpUserInfo(openID, false);
            if (ObjectUtil.isNotNull(cpUserInfo)) {
                cpOpenIDs.add(cpUserInfo.getWxOpenId());
            }
        } catch (BusinessException businessException) {
            logger.info("用户[{}]==未查询到对象信息返回个人上传照片", openID);
        }
        List<UserImage> list = userImageDao.list(Wrappers.<UserImage>lambdaQuery()
                .eq(UserImage::getStatus, 1)
                .in(UserImage::getUploadUser, cpOpenIDs)
                .orderByDesc(UserImage::getCreateTime)
                .last("limit 16"));
        return Optional.ofNullable(list).orElse(new ArrayList<>());
    }

    /**
     * 删除照片 (逻辑删除)
     * @param id
     * @return
     */
    public Boolean deleteCpImages(String id) {
        return userImageDao.update(Wrappers.<UserImage>lambdaUpdate().set(UserImage::getStatus, -1).eq(UserImage::getId, id));
    }

    /**
     * 获取两人在一起的时间
     * @param openID
     * @return
     */
    public Long getTogetherDay(String openID) {
        UserRelation relation = userRelationDao.getRelations(openID);
        if (ObjectUtil.isNull(relation)) {
            return 0L;
        }
        if (relation.getStatus().compareTo(1) != 0) {
            throw new RuntimeException("对方暂未同意绑定关系,或者Broken Up了哦！");
        }
        Date bindTime = relation.getBindTime();
        // 计算时间差
        Date nowDay = DateUtil.endOfDay(new Date());
        DateTime finalEndTime = DateUtil.offsetDay(nowDay, 1);
        Date bindOfDay = DateUtil.beginOfDay(bindTime);
        return DateUtil.between(bindOfDay, finalEndTime, DateUnit.DAY);
    }

    /**
     * 创建情侣关系
     * @param bindRelationsDTO
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean creatRelations(BindRelationsDTO bindRelationsDTO) {

        boolean res;
        // 验证这个uid是否被绑定过
        WxUser bindUser = userDao.getUserInfoByUserUid(bindRelationsDTO.getUserUid());
        if (ObjectUtil.isNull(bindUser)) {
            throw new RuntimeException("未找到对方信息！");
        }

        WxUser myself = userDao.getUserInfo(bindRelationsDTO.getOpenID());
        if (bindRelationsDTO.getUserUid().equals(myself.getWxUserUid())) {
            throw new RuntimeException("无法绑定自己哦!");
        }

        // 判断两人之前有无绑定过，解绑过就更新状态
        UserRelation twoPeopleRelation = userRelationDao.getTwoPeopleRelation(bindUser.getWxOpenId(), myself.getWxOpenId());
        if (ObjectUtil.isNotNull(twoPeopleRelation)) {
            twoPeopleRelation.setBindTime(bindRelationsDTO.getBindTime());
            if (twoPeopleRelation.getStatus().compareTo(-1) == 0) {
                twoPeopleRelation.setStatus(0);
            }
            twoPeopleRelation.setUpdateTime(new Date());
            res = userRelationDao.updateById(twoPeopleRelation);
            if (!res) throw new RuntimeException("更新最新绑定信息失败！");
            return true;
        }

        // 先查对方是否绑定 （有可能被多个人绑定的记录）
        List<UserRelation> bindRelation = userRelationDao.getRelationList(bindUser.getWxOpenId());
        if (ObjectUtil.isNotNull(bindRelation) && !bindRelation.isEmpty()) {
            List<UserRelation> relations = bindRelation.stream().filter(p -> p.getStatus().compareTo(-1) != 0).collect(Collectors.toList());
            if (!relations.isEmpty()) {
                throw new RuntimeException("对方已经被绑定了或者被其他人申请绑定中哦！");
            }
        }

        // 查询自己绑定情况（有可能被多个人绑定的记录）
        List<UserRelation> myselfBindRelation = userRelationDao.getRelationList(myself.getWxOpenId());
        if (ObjectUtil.isNotNull(myselfBindRelation) && !myselfBindRelation.isEmpty()) {
            List<UserRelation> bindRelations = myselfBindRelation.stream().filter(p -> p.getStatus().compareTo(1) == 0).collect(Collectors.toList());
            if (!bindRelations.isEmpty()) {
                // 有一条绑定了就不能再绑定
                throw new RuntimeException("您已经绑定了对象不可以重复绑定哦！");
            }
            List<UserRelation> bindingRelations = myselfBindRelation.stream().filter(p -> p.getStatus().compareTo(0) == 0).collect(Collectors.toList());
            if (!bindingRelations.isEmpty()) {
                throw new RuntimeException("您已建立了绑定关系，快去通知对方同意吧！");
            } else {
                // 如果全是作废了的则先删除自己所有的绑定记录
                userRelationDao.removeByIds(myselfBindRelation.stream().filter(p -> p.getStatus().compareTo(-1) == 0).map(UserRelation::getId).collect(Collectors.toList()));
            }
        }

        // 修改一下最新的昵称
        if (StringUtils.isNotBlank(bindRelationsDTO.getUserNickName())) {
            bindUser.setWxUserNickName(bindRelationsDTO.getUserNickName());
            res = userDao.updateById(bindUser);
            if (!res) {
                throw new RuntimeException("修改对方昵称时出错！");
            }
        }

        UserRelation userRelation = UserRelation.builder()
                .bindTime(bindRelationsDTO.getBindTime())
                .status(0)
                .userOpenId(myself.getWxOpenId())
                .userCpOpenId(bindUser.getWxOpenId())
                .createTime(new Date())
                .build();
        res = userRelationDao.save(userRelation);
        return res;
    }

    /**
     * 根据uid获取用户
     * @param uid
     * @return
     */
    public WxUser getUserByUid(String uid) {
        return userDao.getUserInfoByUserUid(uid);
    }

    /**
     * 查看是否已经绑定
     * @param openID
     * @return
     */
    public Boolean isBind(String openID) {
        UserRelation relation = userRelationDao.getRelations(openID);
        if (relation != null) {
            return relation.getStatus() == 1 ? true : false;
        } else {
            return false;
        }
    }

    /**
     * 更新绑定关系状态
     * @param openID
     * @param status 状态 0：未同意， 1：正常 ， -1：解绑
     * @return
     */
    public Boolean updateRelationsStatus(String openID, Integer status) {
        UserRelation relation = userRelationDao.getRelations(openID);
        if (ObjectUtil.isNull(relation) && relation.getStatus().compareTo(status) == 0) {
            return true;
        }
        relation.setStatus(status);
        relation.setUpdateTime(new Date());
        boolean res = userRelationDao.updateById(relation);
        if (res) {
            WxUser info = userInfoUtil.getMyselfUserInfo(openID);
            String nickName = "你的宝贝";
            if (info != null && StringUtils.isNotBlank(info.getWxUserNickName())) {
                nickName = info.getWxUserNickName();
            }
            if (status.compareTo(1) == 0) {
                sendMessage(openID, String.format("%s同意你的申请绑定！树缠树绕树，相拥到整毫。举察齐眉生，扶携度终生。喜结连理日，恭祝幸福随。", nickName));
            } else if (status.compareTo(-1) == 0) {
                sendMessage(openID, String.format("%s解除了双方的关系！好聚好散终有时！", nickName) );
            }
        }
        return res;
    }

    /**
     * 获取需要审核的关系
     * @param openID*
     * @return
     */
    public UserRelation getNeedAgreeRelationsStatus(String openID) {
        UserRelation relation = userRelationDao.getRelations(openID);
        if (ObjectUtil.isNotNull(relation) && relation.getStatus().compareTo(0) != 0) {
            return null;
        }
        if (!relation.getUserCpOpenId().equals(openID)) {
            return null;
        }
        String name = userInfoUtil.getUserName(relation.getUserOpenId());
        relation.setCreateUserName(name);
        return relation;
    }

    /**
     * 修改对方的昵称
     * @param openID
     * @param nickName 新的昵称
     * @return
     */
    public Boolean updateCpNickName(String openID, String nickName) {
        WxUser cpUserInfo = userInfoUtil.getCpUserInfo(openID, false);
        cpUserInfo.setWxUserNickName(nickName);
        boolean res = userDao.updateById(cpUserInfo);
        if (res) {
            sendMessage(openID, String.format("## 给你起了一个新的称呼叫做：%s！", nickName));
        }
        return res;
    }

    /**
     * 生成用户内部的唯一标识
     * @return
     */
    public synchronized String makeUserUid() {
        String id = Convert.toStr(idGeneratorMap.get(Ids.ShortCode).nextId());
        String UID = String.format("%s-%s", UID_PREFIX, id);
        int count = userDao.count(Wrappers.<WxUser>lambdaQuery().eq(WxUser::getWxUserUid, UID));
        if (count > 0) {
            makeUserUid();
        }
        return UID;
    }
}
