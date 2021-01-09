package com.pingan.life.micromall.product.enums;

public enum CategoryEnum {

    LEVEL_1(1), LEVEL_2(2), LEVEL_3(3)
    ;

    private final int value;

    CategoryEnum(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int getValue() {
        return value;
    }
}
