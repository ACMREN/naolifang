package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.entity.enumEntity.GenderEnum;
import com.smartcity.naolifang.entity.vo.InsiderInfoVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 营区内部人员表
 * </p>
 *
 * @author karl
 * @since 2021-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class InsiderInfo implements Serializable {

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
     * 身份证号码
     */
    private String idCard;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 所属连队
     */
    private String groupName;

    /**
     * 职位
     */
    private String position;

    /**
     * 直属上级
     */
    private String superior;

    /**
     * 个人照片
     */
    private String imageUri;

    /**
     * 是否拥有后台账号：0-否，1-是
     */
    private Integer isAccount;

    /**
     * 是否已经删除：0-否，1-是
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public InsiderInfo() {
    }

    public InsiderInfo(InsiderInfoVo insiderInfoVo) {
        this.id = insiderInfoVo.getId();
        this.name = insiderInfoVo.getName();
        this.gender = GenderEnum.getDataByName(insiderInfoVo.getGender()).getCode();
        this.idCard = insiderInfoVo.getIdCard();
        this.phone = insiderInfoVo.getPhone();
        this.groupName = insiderInfoVo.getGroupName();
        this.position = insiderInfoVo.getPosition();
        this.superior = insiderInfoVo.getSuperior();
        this.imageUri = insiderInfoVo.getImageUri();
        this.isDelete = insiderInfoVo.getIsDelete();
        if (StringUtils.isNotBlank(insiderInfoVo.getCreateTime())) {
            this.createTime = LocalDateTime.parse(insiderInfoVo.getCreateTime());
        }
    }
}
