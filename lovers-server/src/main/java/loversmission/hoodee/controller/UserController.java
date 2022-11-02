package loversmission.hoodee.controller;

import cn.hutool.core.util.ObjectUtil;
import loversmission.hoodee.common.Result;
import loversmission.hoodee.entity.UserRelation;
import loversmission.hoodee.entity.WxUser;
import loversmission.hoodee.entity.pojo.BindRelationsDTO;
import loversmission.hoodee.service.LoveSentenceService;
import loversmission.hoodee.service.WxUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年07月25日 11:26
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private WxUserService wxUserService;

    @Resource
    private LoveSentenceService loveSentenceService;

    @PostMapping("/login")
    public Result<String> loginByWx(String code) {
        String res = wxUserService.loginByWx(code);
        return StringUtils.isNotBlank(res) ? Result.success("操作成功！", res) : Result.error();
    }

    @PostMapping("/saveInfo")
    public Result<Boolean> saveWxUserInfo(@Validated @RequestBody WxUser user) {
        Boolean res = wxUserService.saveUserInfo(user);
        return res ? Result.success(true) : Result.error();
    }

    @GetMapping("/getRelations/{openID}")
    public Result<List<WxUser>> getRelations(@PathVariable("openID") String openID) {
        List<WxUser> info = wxUserService.getUserRelationsInfoByOpenID(openID);
        return Result.success(info);
    }

    @GetMapping("/get/{openID}")
    public Result<WxUser> getUserInfo(@PathVariable("openID") String openID) {
        WxUser info = wxUserService.getUserInfoByOpenID(openID);
        return ObjectUtil.isNotNull(info) ? Result.success(info) : Result.error();
    }

    @GetMapping("/getByUid/{uid}")
    public Result<WxUser> getUserInfoByUid(@PathVariable("uid") String uid) {
        WxUser info = wxUserService.getUserByUid(uid);
        return ObjectUtil.isNotNull(info) ? Result.success(info) : Result.error();
    }

    @GetMapping("/isBind/{openID}")
    public Result<Boolean> isBind(@PathVariable("openID") String openID) {
        Boolean res = wxUserService.isBind(openID);
        return Result.success(res);
    }

    @GetMapping("/get/day/{openID}")
    public Result<Long> getDay(@PathVariable("openID") String openID) {
        Long day = wxUserService.getTogetherDay(openID);
        return Result.success(day);
    }

    @GetMapping("/get/loveSentences/{openID}")
    public Result<String> getLoveSentences(@PathVariable("openID") String openID) {
        String sentence = loveSentenceService.getOneLoveSentence(openID);
        return Result.success(null, sentence);
    }

    @PostMapping("/save/loveSentences")
    public Result<Boolean> saveLoveSentences(String openID, String msg) {
        loveSentenceService.saveLoveSentence(openID, msg);
        return Result.success();
    }

    @PostMapping("/create/relation")
    public Result<Boolean> createRelation(@Validated @RequestBody BindRelationsDTO bindRelationsDTO) {
        Boolean res = wxUserService.creatRelations(bindRelationsDTO);
        return res ? Result.success(true) : Result.error();
    }

    @PostMapping("/update/relationStatus")
    public Result<Boolean> updateRelationStatus(String openID, Integer status) {
        if (StringUtils.isBlank(openID) || status == null) {
            return Result.error("缺少参数！");
        }
        Boolean res = wxUserService.updateRelationsStatus(openID, status);
        return res ? Result.success(true) : Result.error();
    }

    @PostMapping("/update/nickName")
    public Result<Boolean> updateNickName(String openID, String nickName) {
        if (StringUtils.isBlank(openID) || StringUtils.isBlank(nickName)) {
            return Result.error("缺少参数！");
        }
        Boolean res = wxUserService.updateCpNickName(openID, nickName);
        return res ? Result.success(true) : Result.error();
    }

    @PostMapping("/get/relation")
    public Result<UserRelation> getNeedAgreeRelationsStatus(String openID) {
        if (StringUtils.isBlank(openID)) {
            return Result.error("缺少参数！");
        }
        UserRelation res = wxUserService.getNeedAgreeRelationsStatus(openID);
        return Result.success(res);
    }
}

