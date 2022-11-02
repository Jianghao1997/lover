package loversmission.hoodee.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 响应编码
 */
public enum SystemCodeEnum {
    DEFULT_ERROR(-1, "默认返回失败信息"),
    NORAMAL(0, "正常"),
    EXCEPTION(500,"系统异常(未知错误)"),
    WARNING(501,"警告信息(无需处理)"),
    SUCCESS(200, "操作成功");




    @Getter
    private Integer code;
    @Getter
    private String desc;

    private SystemCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SystemCodeEnum getEnum(Integer code) {
        if (code == null) {
            return null;
        }
        Optional<SystemCodeEnum> optional = Arrays.stream(SystemCodeEnum.values())
                .filter(e -> e.getCode().compareTo(code)==0 ).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }
}
