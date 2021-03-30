package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.enumEntity.GenderEnum;
import com.smartcity.naolifang.entity.vo.InsiderInfoVo;
import com.smartcity.naolifang.entity.vo.UserVo;
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
     * 军衔号码
     */
    private String rankNum;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 部门
     */
    private String department;

    /**
     * 称谓
     */
    private String nickName;

    /**
     * 军衔
     */
    private String rankName;

    /**
     * 住址
     */
    private String address;

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
        this.rankNum = insiderInfoVo.getRankNum();
        this.phone = insiderInfoVo.getPhone();
        this.department = insiderInfoVo.getDepartment();
        this.nickName = insiderInfoVo.getNickName();
        this.rankName = insiderInfoVo.getRankName();
        this.address = insiderInfoVo.getAddress();
        this.imageUri = insiderInfoVo.getImageUri();
        if (StringUtils.isNotBlank(insiderInfoVo.getCreateTime())) {
            this.createTime = DateTimeUtil.stringToLocalDateTime(insiderInfoVo.getCreateTime());
        }
    }

    public void updateInsiderInfo(UserVo userVo) {
        this.name = userVo.getName();
        this.gender = GenderEnum.getDataByName(userVo.getGender()).getCode();
        this.idCard = userVo.getIdCard();
        this.rankNum = userVo.getRankNum();
        this.phone = userVo.getPhone();
        this.department = userVo.getDepartment();
        this.nickName = userVo.getNickName();
        this.rankName = userVo.getRankName();
        this.address = userVo.getAddress();
        this.isAccount = 1;
    }
}
