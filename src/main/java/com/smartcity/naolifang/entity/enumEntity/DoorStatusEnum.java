package com.smartcity.naolifang.entity.enumEntity;

public enum DoorStatusEnum {
    NORMAL(0, "正常开关"),
    CLOSE(1, "常闭"),
    OPEN(2, "常开")
    ;


    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    DoorStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static DoorStatusEnum getDataByCode(int code) {
        for (DoorStatusEnum item : DoorStatusEnum.values()) {
            int itemCode = item.getCode();
            if (code == itemCode) {
                return item;
            }
        }
        return null;
    }

    public static DoorStatusEnum getDataByName(String name) {
        for (DoorStatusEnum item : DoorStatusEnum.values()) {
            String itemName = item.getName();
            if (name.equals(itemName)) {
                return item;
            }
        }
        return null;
    }
}
