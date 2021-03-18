package com.smartcity.naolifang.entity.enumEntity;

public enum DeviceTypeEnum {
    ENCODE_DEVICE(0, "编码设备", "encodeDevice"),
    DOOR(1, "门禁设备", "door"),
    CAMERA(2, "摄像头", "camera")
    ;


    private int code;
    private String name;
    private String nameEn;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameEn() {
        return nameEn;
    }

    DeviceTypeEnum(int code, String name, String nameEn) {
        this.code = code;
        this.name = name;
        this.nameEn = nameEn;
    }

    public static DeviceTypeEnum getDataByCode(int code) {
        for (DeviceTypeEnum item : DeviceTypeEnum.values()) {
            int itemCode = item.getCode();
            if (code == itemCode) {
                return item;
            }
        }
        return null;
    }

    public static DeviceTypeEnum getDataByName(String name) {
        for (DeviceTypeEnum item : DeviceTypeEnum.values()) {
            String itemName = item.getName();
            if (name.equals(itemName)) {
                return item;
            }
        }
        return null;
    }

    public static DeviceTypeEnum getDataByNameEn(String nameEn) {
        for (DeviceTypeEnum item : DeviceTypeEnum.values()) {
            String itemNameEn = item.getNameEn();
            if (nameEn.equals(itemNameEn)) {
                return item;
            }
        }
        return null;
    }
}
