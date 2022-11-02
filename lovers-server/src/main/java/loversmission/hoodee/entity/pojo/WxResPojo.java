package loversmission.hoodee.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年08月04日 11:50
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxResPojo {

    private String session_key;

    private String openid;
}
