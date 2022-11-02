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

import java.util.Date;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年08月03日 14:58
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("mission_clock")
public class MissionClock {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("mission_id")
    private String missionID;

    @TableField("mission_day")
    private Integer missionDay;

    @TableField("clock_day")
    private Integer clockDay;

    @TableField("mission_user")
    private String missionUser;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd")
    @TableField("end_time")
    private Date endTime;
}
