package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.smartcity.naolifang.entity.enumEntity.GenderEnum;
import com.smartcity.naolifang.entity.vo.DutyInfoVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.tomcat.jni.Local;

/**
 * <p>
 * 值班信息表
 * </p>
 *
 * @author karl
 * @since 2021-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DutyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 所属连队
     */
    private String groupName;

    /**
     * 职位
     */
    private String position;

    /**
     * 值班开始时间
     */
    private LocalDateTime startTime;

    /**
     * 值班结束时间
     */
    private LocalDateTime endTime;

    public DutyInfo(DutyInfoVo dutyInfoVo) {
        this.id = dutyInfoVo.getId();
        this.name = dutyInfoVo.getName();
        this.gender = GenderEnum.getDataByName(dutyInfoVo.getGender()).getCode();
        this.groupName = dutyInfoVo.getGroupName();
        this.position = dutyInfoVo.getPosition();
        this.startTime = LocalDateTime.parse(dutyInfoVo.getStartTime());
        this.endTime = LocalDateTime.parse(dutyInfoVo.getEndTime());
    }

    public DutyInfo() {
    }
}