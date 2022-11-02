package loversmission.hoodee.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年08月18日 10:22
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiniProgramPageDTO {

    private String title;

    private String pagepath;

    private String thumb_media_id;
}