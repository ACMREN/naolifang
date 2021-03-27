package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 告警等级故障类型表
 * </p>
 *
 * @author karl
 * @since 2021-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmMalfunctionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 告警等级id
     */
    private Integer alarmLevelId;

    /**
     * 故障类型：0-离线，其它参照海康
     */
    private Long malfunctionType;


}
