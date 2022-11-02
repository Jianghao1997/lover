package loversmission.hoodee.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年09月15日 16:53
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalDTO {

    private String Country;

    private String Province;

    private String City;

    private String District;

    /**
     * 运营商信息
     */
    private String Isp;
}
