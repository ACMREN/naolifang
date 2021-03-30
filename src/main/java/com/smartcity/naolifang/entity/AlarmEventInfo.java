package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 事件告警信息表
 * </p>
 *
 * @author karl
 * @since 2021-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmEventInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 告警类型：0-设备告警，1-事件告警
     */
    private Integer alarmType;

    /**
     * 告警内容
     */
    private String content;

    /**
     * 告警级别
     */
    private Integer alarmLevel;

    /**
     * 告警事件标识
     */
    private String eventId;

    /**
     * 设备id
     */
    private Integer deviceId;

    /**
     * 维修人员
     */
    private String fixPerson;

    /**
     * 处理状态
     */
    private Integer status;

    /**
     * 处理人员
     */
    private String handlePerson;

    /**
     * 告警时间
     */
    private LocalDateTime alarmTime;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 处理内容
     */
    private String handleContent;

    /**
     * 发生开始时间
     */
    private String happenStartTime;

    /**
     * 发生结束时间
     */
    private String happenEndTime;

    /**
     * 是否已经删除：0-否，1-是
     */
    private Integer isDelete;


}
