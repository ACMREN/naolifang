package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.enumEntity.GenderEnum;
import com.smartcity.naolifang.entity.vo.DependantInfoVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 营区家属人员表
 * </p>
 *
 * @author karl
 * @since 2021-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DependantInfo implements Serializable {

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
     * 关联人员
     */
    private String relation;

    /**
     * 关联关系
     */
    private String relationship;

    /**
     * 工作单位
     */
    private String institution;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 住址
     */
    private String address;

    /**
     * 个人照片
     */
    private String imageUri;

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

    public DependantInfo() {
    }

    public DependantInfo(DependantInfoVo dependantInfoVo) {
        this.id = dependantInfoVo.getId();
        this.name = dependantInfoVo.getName();
        this.gender = GenderEnum.getDataByName(dependantInfoVo.getGender()).getCode();
        this.relation = dependantInfoVo.getRelation();
        this.phone = dependantInfoVo.getPhone();
        this.relationship = dependantInfoVo.getRelationship();
        this.institution = dependantInfoVo.getInstitution();
        this.address = dependantInfoVo.getAddress();
        this.imageUri = dependantInfoVo.getImageUri();
        if (StringUtils.isNotBlank(dependantInfoVo.getCreateTime())) {
            this.createTime = DateTimeUtil.stringToLocalDateTime(dependantInfoVo.getCreateTime());
        }
    }
}
