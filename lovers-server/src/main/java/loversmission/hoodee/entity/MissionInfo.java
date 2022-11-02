package loversmission.hoodee.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年07月26日 9:31
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("mission_info")
public class MissionInfo {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("mission_id")
    private String missionID;

    @NotBlank(message = "任务标题不能为空")
    @TableField("mission_name")
    private String missionName;

    @NotBlank(message = "任务描述不能为空")
    @TableField("mission_desc")
    private String missionDesc;

    @TableField("mission_status")
    private Integer missionStatus = 0;

    @NotBlank(message = "任务分类不能为空")
    @TableField("mission_classify")
    private String missionClassify;

    @Min(value = 0, message = "积分不能少于0")
    @TableField("mission_credit")
    private Integer missionCredit;

    @NotBlank(message = "创建人不能为空")
    @TableField("create_user_openId")
    private String createUserOpenID;

    @TableField(exist = false)
    private String createUserName;

    @TableField("complete_user_openId")
    private String completeUserOpenID;

    @TableField(exist = false)
    private String completeUserName;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @TableField("create_time")
    private Date createTime;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @TableField("update_time")
    private Date updateTime;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @TableField("complete_time")
    private Date completeTime;

    /**
     * 任务类型 : 0：短期任务，1：长期任务
     */
    @TableField("mission_type")
    private Integer missionType = 0;

    /**
     * 任务持续多少天
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd")
    @TableField(exist = false)
    private Date endTime;
    /**
     * 已打卡多少天
     */
    @TableField(exist = false)
    private Integer clockDay;

    public boolean validatedCredit() {
        if (this.getMissionCredit() == null || this.getMissionCredit().compareTo(0) < 0) {
            return false;
        }
        return true;
    }
}
