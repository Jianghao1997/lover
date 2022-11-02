package loversmission.hoodee.controller;

import cn.hutool.core.util.ObjectUtil;
import loversmission.hoodee.common.Result;
import loversmission.hoodee.entity.MissionInfo;
import loversmission.hoodee.service.MissionInfoService;
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
@RequestMapping("mission")
public class MissionController {

    @Resource
    private MissionInfoService missionInfoService;

    @PostMapping("/saveInfo")
    public Result<Boolean> saveMissionInfo(@Validated @RequestBody MissionInfo info) {
        if (!info.validatedCredit()) {
            return Result.error("必须要设点积分哦！");
        }
        Boolean res = missionInfoService.saveOrUpdateInfo(info);
        return res ? Result.success(true) : Result.error();
    }

    @GetMapping("/get/{missionId}")
    public Result<MissionInfo> getMissionInfo(@PathVariable("missionId") String missionId) {
        MissionInfo info = missionInfoService.getMissionInfoById(missionId);
        return ObjectUtil.isNotNull(info) ? Result.success(info) : Result.error();
    }

    @GetMapping("/get")
    public Result<List<MissionInfo>> getMissionInfoList(String openID, Integer status, String searchValue) {
        List<MissionInfo> info = missionInfoService.getMissionInfoList(openID, status, searchValue);
        return Result.success(info);
    }

    @PostMapping("/complete")
    public Result<Boolean> complete(String missionID, String openID, Boolean bForce) {
        Boolean res = missionInfoService.completeMission(missionID, openID, bForce);
        return res ? Result.success(true) : Result.error();
    }

    @PostMapping("/clock")
    public Result<MissionInfo> clockMission(String missionId, String openID) {
        boolean res = missionInfoService.clockMission(missionId, openID);
        return res ? Result.success("打卡成功！") : Result.error("打卡失败");
    }

    @PostMapping("/delete")
    public Result<Boolean> delete(String missionID, String openID) {
        Boolean res = missionInfoService.deleteMission(missionID, openID);
        return res ? Result.success(true) : Result.error();
    }
}
