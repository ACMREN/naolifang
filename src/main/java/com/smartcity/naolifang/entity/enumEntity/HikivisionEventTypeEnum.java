package com.smartcity.naolifang.entity.enumEntity;

public enum HikivisionEventTypeEnum {
    VISITOR_SIGN_IN(1392513025, "访客登记"),
    VISITOR_SIGN_OUT(1392513026, "访客签离"),
    GUEST_CARD_PASS(196868, "来宾卡通过"),
    FACE_PASS(196893, "人脸通过认证")
    ;

    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    HikivisionEventTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static HikivisionEventTypeEnum getDataByCode(int code) {
        for (HikivisionEventTypeEnum item : HikivisionEventTypeEnum.values()) {
            int itemCode = item.getCode();
            if (code == itemCode) {
                return item;
            }
        }
        return null;
    }

    public static HikivisionEventTypeEnum getDataByName(String name) {
        for (HikivisionEventTypeEnum item : HikivisionEventTypeEnum.values()) {
            String itemName = item.getName();
            if (name.equals(itemName)) {
                return item;
            }
        }
        return null;
    }
}
