package com.smartcity.naolifang.entity.enumEntity;

public enum StatusEnum {
    OFFLINE(0, "离线"),
    ONLINE(1, "在线"),
    FORBID(2, "已禁止"),
    INACTIVE(3, "未激活")
    ;


    private int code;
    private String name;

    StatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static StatusEnum getDataByCode(int code) {
        for (StatusEnum item : StatusEnum.values()) {
            int itemCode = item.getCode();
            if (code == itemCode) {
                return item;
            }
        }
        return null;
    }

    public static StatusEnum getDataByName(String name) {
        for (StatusEnum item : StatusEnum.values()) {
            String itemName = item.getName();
            if (name.equals(itemName)) {
                return item;
            }
        }
        return null;
    }
}
