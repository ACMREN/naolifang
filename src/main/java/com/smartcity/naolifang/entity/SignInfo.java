package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 访客签入信息表
 * </p>
 *
 * @author karl
 * @since 2021-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SignInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 访客id
     */
    private Integer visitorId;

    /**
     * 体温
     */
    private String temperature;

    /**
     * 健康码颜色
     */
    private String healthCode;

    /**
     * 出发地
     */
    private String originalPlace;

    /**
     * 核酸检测时间
     */
    private LocalDate checkTime;

    /**
     * 检测医院
     */
    private String checkHospital;


}
