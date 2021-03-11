package com.smartcity.naolifang.entity.enumEntity;

public enum LeaveStatusEnum {
    LEAVE(0, "未离营"),
    UNLEAVE(1, "已离营");

    private int code;
    private String name;

    LeaveStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static LeaveStatusEnum getDataByCode(int code) {
        for (LeaveStatusEnum item : LeaveStatusEnum.values()) {
            int itemCode = item.getCode();
            if (code == itemCode) {
                return item;
            }
        }
        return null;
    }

    public static LeaveStatusEnum getDataByName(String name) {
        for (LeaveStatusEnum item : LeaveStatusEnum.values()) {
            String itemName = item.getName();
            if (name.equals(itemName)) {
                return item;
            }
        }
        return null;
    }
}
