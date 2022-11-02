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
@TableName("love_sentence")
public class LoveSentence {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("relation_id")
    private Integer relationId;

    @TableField("create_user")
    private String createUser;

    @TableField("love_info")
    private String loveInfo;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    /**
     * 积分
     */
    @TableField("like_count")
    private Integer likeCount = 0;
}
