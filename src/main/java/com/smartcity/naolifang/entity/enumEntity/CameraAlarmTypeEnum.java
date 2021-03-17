package com.smartcity.naolifang.entity.enumEntity;

public enum CameraAlarmTypeEnum {
    MISS_VIDEO("视频丢失",131329),
    KEEP_OUT_VIDEO("视频遮挡",131330),
    MOVE_DETECT("移动侦测",131331),
    CHANGE_SCENE("场景变更",131612),
    VIRTUAL_ZOOM("虚焦",131613),
    ALARM_INPUT("报警输入",589825),
    VISUAL_OBJECT("可视域事件",196355),
    GPS_COLLECT("GPS采集",851969),
    INVADE_ZONE("区域入侵",131588),
    CROSS_DETECT("越界侦测",131585),
    ENTER_ZONE("进入区域",131586),
    LEAVE_ZONE("离开区域",131587),
    LINGER_DETECT("徘徊侦测",131590),
    PERSON_GATHER("人员聚集",131593),
    QUICK_MOVE("快速移动",131592),
    STOP_CAR_DETECT("停车侦测",131591),
    ITEM_LEFT("物品遗留",131594),
    ITEM_GET("物品拿取",131595),
    PEOPLE_NUMBER_EXCEPTION("人数异常",131664),
    DISTANCE_EXCEPTION("间距异常",131665),
    SEVERE_MOVE("剧烈运动",131596),
    ABSENCES("离岗",131603),
    DOWN("倒地",131605),
    CLIMB_UP("攀高",131597),
    MAIN_PERSON_GET_UP("重点人员起身",131610),
    TOILET_TIMEOUT("如厕超时",131608),
    PERSON_STAND("人员站立",131666),
    SIT("静坐",131667),
    STRANDED("防风场滞留",131609),
    GET_UP("起身",131598),
    CLOSE_ATM("人靠近ATM",131599),
    OPERATION_TIMEOUT("操作超时",131600),
    STICK_NOTE("贴纸条",131601),
    SET_UP_READER("安装读卡器",131602),
    FOLLOWING("尾随",131604),
    VOICE_CHANGE("声强突变",131606),
    BROKEN_LINE_CLIMB_UP("折线攀高",131607),
    BROKEN_LINE_ALERT("折线警戒面",131611),
    TEMPERATURE_DIFF_ALARM("温差报警",192518),
    TEMPERATURE_ALARM("温度报警",192517),
    BOAT_CHECK("船只检测",192516),
    FIRE_POINT_CHECK("火点检测",192515),
    FIREWORK_CHECK("烟火检测",192514),
    SMOKE_CHECK("烟雾检测",192513)
    ;

    private int code;
    private String name;

    CameraAlarmTypeEnum(String name, int code) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
