package com.smartcity.naolifang.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.entity.VacationInfo;
import com.smartcity.naolifang.entity.enumEntity.CancelVacationStatusEnum;
import com.smartcity.naolifang.entity.enumEntity.GenderEnum;
import com.smartcity.naolifang.entity.enumEntity.LeaveStatusEnum;
import com.smartcity.naolifang.entity.searchCondition.VacationCondition;
import com.smartcity.naolifang.entity.vo.PageListVo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.entity.vo.VacationInfoVo;
import com.smartcity.naolifang.service.VacationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/leaveOfficier")
public class VacationController {

    @Autowired
    private VacationInfoService vacationInfoService;

    @RequestMapping("/save")
    public Result saveVacationInfo(@RequestBody VacationInfoVo vacationInfoVo) {
        VacationInfo vacationInfo = new VacationInfo(vacationInfoVo);

        vacationInfoService.saveOrUpdate(vacationInfo);

        return Result.ok();
    }

    @RequestMapping("/list")
    public Result listVacationInfos(@RequestBody VacationCondition vacationCondition) {
        Integer pageNo = vacationCondition.getPageNo();
        Integer pageSize = vacationCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取请假信息失败，信息：传入的页码或数据条数为空");
        }
        Integer offset = (pageNo - 1) * pageSize;
        String name = vacationCondition.getName();
        String gender = vacationCondition.getGender();
        Integer genderInt = GenderEnum.getDataByName(vacationCondition.getGender()).getCode();
        String leaveStatus = vacationCondition.getLeaveStatus();
        Integer leaveStatusInt = LeaveStatusEnum.getDataByName(vacationCondition.getLeaveStatus()).getCode();
        String cancelVacationStatus = vacationCondition.getCancelVacationStatus();
        Integer cancelVacationStatusInt = CancelVacationStatusEnum.getDataByName(vacationCondition.getCancelVacationStatus()).getCode();

        List<VacationInfo> vacationInfos = vacationInfoService.list(new QueryWrapper<VacationInfo>()
                .eq("is_delete", 0)
                .eq(StringUtils.isNotBlank(cancelVacationStatus), "cancel_vacation_status", cancelVacationStatusInt)
                .eq(StringUtils.isNotBlank(leaveStatus), "leave_status", leaveStatusInt)
                .eq(StringUtils.isNotBlank(gender), "gender", genderInt)
                .like(StringUtils.isNotBlank(name), "name", name)
                .last("limit " + offset + ", " + pageSize));
        Integer totalCount = vacationInfoService.count(new QueryWrapper<VacationInfo>()
                .eq("is_delete", 0)
                .eq(StringUtils.isNotBlank(cancelVacationStatus), "cancel_vacation_status", cancelVacationStatusInt)
                .eq(StringUtils.isNotBlank(leaveStatus), "leave_status", leaveStatusInt)
                .eq(StringUtils.isNotBlank(gender), "gender", genderInt)
                .like(StringUtils.isNotBlank(name), "name", name));

        List<VacationInfoVo> resultList = new ArrayList<>();
        for (VacationInfo item : vacationInfos) {
            VacationInfoVo data = new VacationInfoVo(item);
            resultList.add(data);
        }

        PageListVo pageListVo = new PageListVo(resultList, pageNo, pageSize, totalCount);
        return Result.ok(pageListVo);
    }

    @RequestMapping("/remove")
    public Result deleteVacationInfo(@RequestBody VacationCondition vacationCondition) {
        List<Integer> ids = vacationCondition.getIds();
        List<VacationInfo> vacationInfos = vacationInfoService.listByIds(ids);

        for (VacationInfo item : vacationInfos) {
            item.setIsDelete(1);
        }

        vacationInfoService.saveOrUpdateBatch(vacationInfos);

        return Result.ok();
    }

}
