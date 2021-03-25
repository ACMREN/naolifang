package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 人脸图库信息表
 * </p>
 *
 * @author karl
 * @since 2021-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FaceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 人脸图片
     */
    private String photoUrl;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号码
     */
    private String idCard;


}
