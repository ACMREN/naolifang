package com.smartcity.naolifang.entity.enumEntity;

public enum HandleStatusEnum {
    UN_HANDLE(0, "未处理"),
    HANDLING(1, "处理中"),
    HANDLED(2, "已处理")
    ;


    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    HandleStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static HandleStatusEnum getDataByCode(int code) {
        for (HandleStatusEnum item : HandleStatusEnum.values()) {
            int itemCode = item.getCode();
            if (code == itemCode) {
                return item;
            }
        }
        return null;
    }

    public static HandleStatusEnum getDataByName(String name) {
        for (HandleStatusEnum item : HandleStatusEnum.values()) {
            String itemName = item.getName();
            if (name.equals(itemName)) {
                return item;
            }
        }
        return null;
    }
}
