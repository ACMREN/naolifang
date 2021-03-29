package com.smartcity.naolifang.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.DeviceInfo;
import com.smartcity.naolifang.entity.VacationInfo;
import com.smartcity.naolifang.entity.VisitorInfo;
import com.smartcity.naolifang.entity.enumEntity.StatusEnum;
import com.smartcity.naolifang.entity.enumEntity.VisitStatusEnum;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.*;
import org.bytedeco.opencv.opencv_core.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    @Autowired
    private VisitorInfoService visitorInfoService;

    @Autowired
    private InsiderInfoService insiderInfoService;

    @Autowired
    private DependantInfoService dependantInfoService;

    @Autowired
    private VacationInfoService vacationInfoService;

    @Autowired
    private DeviceInfoService deviceInfoService;

    @RequestMapping("/person")
    public Result statisticVisitor() {
        String todayStartTime = DateTimeUtil.localDateToString(LocalDate.now()) + " 00:00:00";
        String todayEndTime = DateTimeUtil.localDateToString(LocalDate.now()) + " 23:59:59";
        // 预约访客总数
        Integer totalVisitor = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .gt("visitor_start_time", todayStartTime)
                .lt("visitor_end_time", todayEndTime));
        // 内部人员总数
        Integer totalInsider = insiderInfoService.count();
        // 登记家属总数
        Integer totalDependant = dependantInfoService.count();
        // 在访人数
        Integer totalVisiting = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("status", VisitStatusEnum.SIGN_IN)
                .gt("visitor_start_time", todayStartTime)
                .lt("visitor_end_time", todayEndTime));
        // 超时人数
        Integer totalTimeout = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("status", VisitStatusEnum.TIME_OUT)
                .gt("visitor_start_time", todayStartTime)
                .lt("visitor_end_time", todayEndTime));
        // 签离人数
        Integer totalSignOut = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("status", VisitStatusEnum.SIGN_OUT)
                .gt("visitor_start_time", todayStartTime)
                .lt("visitor_end_time", todayEndTime));
        // 拒绝入营
        Integer totalReject = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("status", VisitStatusEnum.REJECT)
                .gt("visitor_start_time", todayStartTime)
                .lt("visitor_end_time", todayEndTime));
        // 请假人数
        Integer totalVacation = vacationInfoService.count(new QueryWrapper<VacationInfo>()
                .eq("cancel_vacation_status", 0));

        JSONObject resultJson = new JSONObject();
        resultJson.put("totalVisitor", totalVisitor);
        resultJson.put("totalInsider", totalInsider);
        resultJson.put("totalDependant", totalDependant);
        resultJson.put("totalVisiting", totalVisiting);
        resultJson.put("totalTimeout", totalTimeout);
        resultJson.put("totalSignOut", totalSignOut);
        resultJson.put("totalReject", totalReject);
        resultJson.put("totalVacation", totalVacation);

        return Result.ok(resultJson);
    }

    @RequestMapping("/device")
    public Result statisticDevice() {
        // 离线设备
        Integer offline = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("status", StatusEnum.OFFLINE));
        // 正常设备
        Integer normal = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("status", StatusEnum.OFFLINE));
        // 告警设备
        Integer alarm = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("status", StatusEnum.ALARM));

        JSONObject resultJson = new JSONObject();
        resultJson.put("offline", offline);
        resultJson.put("normal", normal);
        resultJson.put("alarm", alarm);

        return Result.ok(resultJson);
    }
}
