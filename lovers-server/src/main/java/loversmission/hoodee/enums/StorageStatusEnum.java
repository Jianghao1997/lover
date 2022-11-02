package loversmission.hoodee.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum StorageStatusEnum {


    UN_USE(1, "未使用"),
    USED(-1, "已使用");

    @Getter
    private Integer code;

    @Getter
    private String desc;

    private StorageStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static StorageStatusEnum getEnum(Integer code) {
        if (code == null) {
            return null;
        }
        Optional<StorageStatusEnum> optional = Arrays.stream(StorageStatusEnum.values())
                .filter(e -> e.getCode().compareTo(code)==0 ).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }
}
