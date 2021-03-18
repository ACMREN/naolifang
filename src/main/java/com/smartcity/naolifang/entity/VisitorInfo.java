package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.enumEntity.VisitStatusEnum;
import com.smartcity.naolifang.entity.vo.VisitorInfoVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 访客信息表
 * </p>
 *
 * @author karl
 * @since 2021-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VisitorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 访客姓名
     */
    private String name;

    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 访问对象
     */
    private String visitPerson;

    /**
     * 访问理由
     */
    private String reason;

    /**
     * 访问开始时间
     */
    private LocalDateTime visitStartTime;

    /**
     * 访问结束时间
     */
    private LocalDateTime visitEndTime;

    /**
     * 签离时间
     */
    private LocalDateTime leaveTime;

    /**
     * 出发地
     */
    private String originalPlace;

    /**
     * 到访状态
     */
    private Integer status;

    /**
     * 是否已经删除：0-否，1-是
     */
    private Integer isDelete;

    public VisitorInfo() {
    }

    public VisitorInfo(VisitorInfoVo visitorInfoVo) {
        this.name = visitorInfoVo.getName();
        this.idCard = visitorInfoVo.getIdCard();
        this.phone = visitorInfoVo.getPhone();
        this.reason = visitorInfoVo.getReason();
        this.visitPerson = visitorInfoVo.getVisitPerson();
        this.visitStartTime = DateTimeUtil.stringToLocalDateTime(visitorInfoVo.getVisitStartTime());
        this.visitEndTime = DateTimeUtil.stringToLocalDateTime(visitorInfoVo.getVisitEndTime());
        this.status = VisitStatusEnum.NO_VISIT.getCode();
    }

    public void updateVisitorInfo(VisitorInfoVo visitorInfoVo) {
        this.id = visitorInfoVo.getId();
        this.name = visitorInfoVo.getName();
        this.idCard = visitorInfoVo.getIdCard();
        this.phone = visitorInfoVo.getPhone();
        this.reason = visitorInfoVo.getReason();
        this.visitPerson = visitorInfoVo.getVisitPerson();
    }
}
