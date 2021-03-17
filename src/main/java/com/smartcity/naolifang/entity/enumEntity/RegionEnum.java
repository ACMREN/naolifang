package com.smartcity.naolifang.entity.enumEntity;

public enum RegionEnum {
    PROTECT_REGION(0, "防护区"),
    MONITOR_REGION(1, "监控区"),
    LIMIT_REGION(2, "限制区")
    ;


    private int code;
    private String name;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    RegionEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static RegionEnum getDataByCode(int code) {
        for (RegionEnum item : RegionEnum.values()) {
            int itemCode = item.getCode();
            if (code == itemCode) {
                return item;
            }
        }
        return null;
    }

    public static RegionEnum getDataByName(String name) {
        for (RegionEnum item : RegionEnum.values()) {
            String itemName = item.getName();
            if (name.equals(itemName)) {
                return item;
            }
        }
        return null;
    }
}
