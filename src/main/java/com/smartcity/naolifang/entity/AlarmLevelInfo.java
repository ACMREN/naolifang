package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 告警等级信息表
 * </p>
 *
 * @author karl
 * @since 2021-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmLevelInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 告警级别：0-一般。1-严重，2-非常严重
     */
    private Integer alarmLevel;

    /**
     * 区域：0-防护区，1-监控区，2-限制区
     */
    private Integer region;

    /**
     * 告警类型：0-设备告警，1-事件告警
     */
    private Integer alarmType;

    /**
     * 故障类型：0-离线，其它参照海康
     */
    private Integer malfunctionType;

}
