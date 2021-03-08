package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

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
     * 配偶姓名
     */
    private String coupleName;

    /**
     * 工作单位
     */
    private String institution;

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
        this.coupleName = dependantInfoVo.getCoupleName();
        this.institution = dependantInfoVo.getInstitution();
        this.imageUri = dependantInfoVo.getImageUri();
        this.createTime = LocalDateTime.parse(dependantInfoVo.getCreateTime());
        this.updateTime = LocalDateTime.parse(dependantInfoVo.getUpdateTime());
    }
}
