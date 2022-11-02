package loversmission.hoodee.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum MissionStatusEnum {


    NOT_FINISHED(0, "未完成"),
    FINISHED(1, "完成"),
    CANCEL(-1, "作废");

    @Getter
    private Integer code;

    @Getter
    private String desc;

    private MissionStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MissionStatusEnum getEnum(Integer code) {
        if (code == null) {
            return null;
        }
        Optional<MissionStatusEnum> optional = Arrays.stream(MissionStatusEnum.values())
                .filter(e -> e.getCode().compareTo(code)==0 ).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }
}
