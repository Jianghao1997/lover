package loversmission.hoodee.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
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
@TableName("wx_user")
public class WxUser {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "ID不能为空")
    @TableField("wx_openId")
    private String wxOpenId;

    @TableField("wx_avatarUrl")
    private String wxAvatarUrl;

    @TableField("wx_userName")
    private String wxUserName;

    /**
     * 给与对方的昵称
     */
    @TableField("wx_userNickName")
    private String wxUserNickName;

    @TableField("wx_userSex")
    private Integer wxUserSex;

    /**
     * 积分
     */
    @TableField("wx_UserCredit")
    private Integer wxUserCredit = 0;

    @TableField("wx_UserUid")
    private String wxUserUid;

    @TableField("last_login_time")
    private Date lastLoginTime;
}
