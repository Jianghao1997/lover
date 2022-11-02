package loversmission.hoodee.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum ProductStatusEnum {


    SALE(1, "上架"),
    UN_SALE(-1, "下架");

    @Getter
    private Integer code;

    @Getter
    private String desc;

    private ProductStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ProductStatusEnum getEnum(Integer code) {
        if (code == null) {
            return null;
        }
        Optional<ProductStatusEnum> optional = Arrays.stream(ProductStatusEnum.values())
                .filter(e -> e.getCode().compareTo(code)==0 ).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }
}
