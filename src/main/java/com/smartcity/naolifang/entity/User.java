package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.common.util.MD5Util;
import com.smartcity.naolifang.entity.vo.UserVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 登录用户表
 * </p>
 *
 * @author karl
 * @since 2021-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 花名册id
     */
    private Integer registerId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否开启：0-否，1-是
     */
    private Integer isEnable;

    /**
     * 是否 已经删除：0-否，1-是
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

    public User() {
    }

    public User(UserVo userVo) {
        this.id = userVo.getId();
        this.username = userVo.getUsername();
        this.salt = MD5Util.generateSalt();
        this.password = MD5Util.passwordMd5Encode(userVo.getPassword(), salt);
        this.registerId = userVo.getRegisterId();
        this.remark = userVo.getRemark();
        if (StringUtils.isNotBlank(userVo.getCreateTime())) {
            this.createTime = DateTimeUtil.stringToLocalDateTime(userVo.getCreateTime());
        }
    }
}
