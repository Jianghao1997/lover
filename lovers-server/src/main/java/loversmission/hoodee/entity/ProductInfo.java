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
@TableName("product_info")
public class ProductInfo {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("product_id")
    private String productID;

    @NotBlank(message = "商品标题不能为空")
    @TableField("product_name")
    private String productName;

    @NotBlank(message = "商品描述不能为空")
    @TableField("product_desc")
    private String productDesc;

    @TableField("product_status")
    private Integer productStatus = 1;

    @NotBlank(message = "商品分类不能为空")
    @TableField("product_classify")
    private String productClassify;

    @Min(value = 0, message = "积分不能少于0")
    @TableField("product_credit")
    private Integer productCredit;

    @NotBlank(message = "创建人不能为空")
    @TableField("create_user_openId")
    private String createUserOpenID;

    @TableField(exist = false)
    private String createUserName;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @TableField("create_time")
    private Date createTime;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd HH:mm")
    @TableField("update_time")
    private Date updateTime;

    @TableField(exist = false)
    private String storageID;

    @TableField(exist = false)
    private Integer count;


    public boolean validatedCredit() {
        if (this.getProductCredit() == null || this.getProductCredit().compareTo(0) < 0) {
            return false;
        }
        return true;
    }

}
