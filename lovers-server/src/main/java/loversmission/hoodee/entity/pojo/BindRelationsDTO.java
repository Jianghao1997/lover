package loversmission.hoodee.entity.pojo;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年08月23日 15:06
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BindRelationsDTO {

    @NotBlank(message = "主要参数缺失")
    private String openID;

    @NotBlank(message = "请输入用户UID")
    private String userUid;

    @JsonFormat(locale="zh", timezone="GMT+8", pattern = "yyyy-MM-dd")
    @NotNull(message = "请输入在一起的第一天")
    private Date bindTime;

    private String userNickName;

    public Date getBindTime() {
        // 传过来莫名少了一天
        return DateUtil.offsetDay(bindTime, 1);
    }
}
