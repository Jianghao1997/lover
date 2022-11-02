package loversmission.hoodee.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum BusinessExceptionEnum {


    BUSINESS_NORMAL_EXCEPTION(10001, "未绑定");

    @Getter
    private Integer code;

    @Getter
    private String desc;

    private BusinessExceptionEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BusinessExceptionEnum getEnum(Integer code) {
        if (code == null) {
            return null;
        }
        Optional<BusinessExceptionEnum> optional = Arrays.stream(BusinessExceptionEnum.values())
                .filter(e -> e.getCode().compareTo(code)==0 ).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }
}
