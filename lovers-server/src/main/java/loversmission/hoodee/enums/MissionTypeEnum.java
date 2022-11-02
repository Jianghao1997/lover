package loversmission.hoodee.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum MissionTypeEnum {


    SHORT_TIME(0, "短期"),
    LONG_TIME(1, "长期");

    @Getter
    private Integer code;

    @Getter
    private String desc;

    private MissionTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MissionTypeEnum getEnum(Integer code) {
        if (code == null) {
            return null;
        }
        Optional<MissionTypeEnum> optional = Arrays.stream(MissionTypeEnum.values())
                .filter(e -> e.getCode().compareTo(code)==0 ).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }
}
