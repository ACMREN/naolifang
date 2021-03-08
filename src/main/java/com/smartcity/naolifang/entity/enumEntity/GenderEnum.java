package com.smartcity.naolifang.entity.enumEntity;

public enum GenderEnum {
    MALE(0, "男"),
    FEMALE(1, "女");

    private int code;
    private String name;

    GenderEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static GenderEnum getDataByCode(int code) {
        for (GenderEnum item : GenderEnum.values()) {
            int itemCode = item.getCode();
            if (code == itemCode) {
                return item;
            }
        }
        return null;
    }

    public static GenderEnum getDataByName(String name) {
        for (GenderEnum item : GenderEnum.values()) {
            String itemName = item.getName();
            if (name.equals(itemName)) {
                return item;
            }
        }
        return null;
    }
}
