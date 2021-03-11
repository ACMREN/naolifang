package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.enumEntity.CancelVacationStatusEnum;
import com.smartcity.naolifang.entity.enumEntity.GenderEnum;
import com.smartcity.naolifang.entity.enumEntity.LeaveStatusEnum;
import com.smartcity.naolifang.entity.vo.VacationInfoVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 请假信息表
 * </p>
 *
 * @author karl
 * @since 2021-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class VacationInfo implements Serializable {

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
     * 请假原因
     */
    private String reason;

    /**
     * 离营状态：0-未离营，1-已离营
     */
    private Integer leaveStatus;

    /**
     * 销假状态：0-未销假，1-已销假
     */
    private Integer cancelVacationStatus;

    /**
     * 销假时间
     */
    private LocalDateTime cancelVacationTime;

    /**
     * 批准人
     */
    private String approver;

    /**
     * 是否已经删除：0-否，1-是
     */
    private Integer isDelete;

    public VacationInfo() {
    }

    public VacationInfo(VacationInfoVo vacationInfoVo) {
        this.id = vacationInfoVo.getId();
        this.name = vacationInfoVo.getName();
        this.gender  = GenderEnum.getDataByName(vacationInfoVo.getGender()).getCode();
        this.reason = vacationInfoVo.getReason();
        this.leaveStatus = LeaveStatusEnum.getDataByName(vacationInfoVo.getLeaveStatus()).getCode();
        this.cancelVacationStatus = CancelVacationStatusEnum.getDataByName(vacationInfoVo.getCancelVacationStatus()).getCode();
        if (StringUtils.isNotBlank(vacationInfoVo.getCancelVacationTime())) {
            this.cancelVacationTime = DateTimeUtil.stringToLocalDateTime(vacationInfoVo.getCancelVacationTime());
        }
        this.approver = vacationInfoVo.getApprover();
    }
}
