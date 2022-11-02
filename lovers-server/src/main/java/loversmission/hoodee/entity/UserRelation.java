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
 * @createTime: 2022年07月25日 11:23
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_relation")
public class UserRelation {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("user_openId")
    private String userOpenId;

    @TableField("user_cp_openId")
    private String userCpOpenId;

    @TableField("status")
    private Integer status;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("bind_time")
    private Date bindTime;

    @TableField(exist = false)
    private String CreateUserName;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;
}
