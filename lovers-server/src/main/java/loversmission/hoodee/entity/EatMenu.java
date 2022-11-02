package loversmission.hoodee.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年08月24日 17:19
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("eat_menu")
public class EatMenu {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String text;
}
