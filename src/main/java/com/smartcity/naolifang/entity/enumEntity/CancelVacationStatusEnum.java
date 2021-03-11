package com.smartcity.naolifang.entity.enumEntity;

public enum CancelVacationStatusEnum {
    NOT_CANCEL(0, "未销假"),
    CANCEL(1, "已销假");


    private int code;
    private String name;

    CancelVacationStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static CancelVacationStatusEnum getDataByCode(int code) {
        for (CancelVacationStatusEnum item : CancelVacationStatusEnum.values()) {
            int itemCode = item.getCode();
            if (code == itemCode) {
                return item;
            }
        }
        return null;
    }

    public static CancelVacationStatusEnum getDataByName(String name) {
        for (CancelVacationStatusEnum item : CancelVacationStatusEnum.values()) {
            String itemName = item.getName();
            if (name.equals(itemName)) {
                return item;
            }
        }
        return null;
    }
}
