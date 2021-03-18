package com.smartcity.naolifang.entity.enumEntity;

public enum VisitStatusEnum {
    NO_VISIT(0, "未到访"),
    SIGN_IN(1, "已签入"),
    REJECT(2, "拒绝签入"),
    SIGN_OUT(3, "已签离"),
    TIME_OUT(4, "访问超时")
    ;


    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    VisitStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static VisitStatusEnum getDataByCode(int code) {
        for (VisitStatusEnum item : VisitStatusEnum.values()) {
            int itemCode = item.getCode();
            if (code == itemCode) {
                return item;
            }
        }
        return null;
    }

    public static VisitStatusEnum getDataByName(String name) {
        for (VisitStatusEnum item : VisitStatusEnum.values()) {
            String itemName = item.getName();
            if (name.equals(itemName)) {
                return item;
            }
        }
        return null;
    }
}
