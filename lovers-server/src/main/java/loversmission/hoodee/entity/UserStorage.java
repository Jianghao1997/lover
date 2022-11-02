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
 * @createTime: 2022年08月05日 16:36
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_storage")
public class UserStorage {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("storage_id")
    private String storageID;

    @TableField("product_id")
    private String productID;

    @TableField("storage_user")
    private String storageUser;

    @TableField("storage_status")
    private Integer storageStatus = 1;

    @TableField("storage_count")
    private Integer storageCount;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @TableField("create_time")
    private Date createTime;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @TableField("update_time")
    private Date updateTime;

}
