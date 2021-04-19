package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 出入记录信息表
 * </p>
 *
 * @author karl
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InsideOutRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 人员id
     */
    private Integer personId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 个人照片
     */
    private String imageUri;

    /**
     * 人员类型
     */
    private Integer personType;

    /**
     * 设备id
     */
    private Integer deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 位置信息
     */
    private String positionInfo;

    /**
     * 出入类型：0-离营，1-回营
     */
    private Integer type;

    /**
     * 出入时间
     */
    private LocalDateTime time;


    public InsideOutRecord() {
    }
}
