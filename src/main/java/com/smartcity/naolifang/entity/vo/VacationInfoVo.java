package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.VacationInfo;
import com.smartcity.naolifang.entity.enumEntity.CancelVacationStatusEnum;
import com.smartcity.naolifang.entity.enumEntity.GenderEnum;
import com.smartcity.naolifang.entity.enumEntity.LeaveStatusEnum;
import lombok.Data;

@Data
public class VacationInfoVo {
    private Integer id;
    private String name;
    private String gender;
    private String reason;
    private String leaveStatus;
    private String cancelVacationStatus;
    private String cancelVacationTime;
    private String approver;

    public VacationInfoVo() {
    }

    public VacationInfoVo(VacationInfo vacationInfo) {
        this.id = vacationInfo.getId();
        this.name = vacationInfo.getName();
        this.gender = GenderEnum.getDataByCode(vacationInfo.getGender()).getName();
        this.reason =  vacationInfo.getReason();
        this.leaveStatus = LeaveStatusEnum.getDataByCode(vacationInfo.getLeaveStatus()).getName();
        this.cancelVacationStatus = CancelVacationStatusEnum.getDataByCode(vacationInfo.getCancelVacationStatus()).getName();
        this.cancelVacationTime = DateTimeUtil.localDateTimeToString(vacationInfo.getCancelVacationTime());
        this.approver = vacationInfo.getApprover();
    }
}
