package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 摄像头轮询信息表
 * </p>
 *
 * @author karl
 * @since 2021-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CameraPollingInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 摄像头id
     */
    private Integer cameraId;


}
