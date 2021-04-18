package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 门禁权限下发记录信息表
 * </p>
 *
 * @author karl
 * @since 2021-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DoorPermissionInfo implements Serializable {

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
     * 人员唯一标识
     */
    private String personIndexCode;

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
     * 设备唯一标识
     */
    private String deviceIndexCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 位置信息
     */
    private String positionInfo;

    /**
     * 下发状态
     */
    private Integer status;


}
