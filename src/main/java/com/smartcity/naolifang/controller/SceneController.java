package com.smartcity.naolifang.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.*;
import com.smartcity.naolifang.entity.enumEntity.DeviceTypeEnum;
import com.smartcity.naolifang.entity.enumEntity.HandleStatusEnum;
import com.smartcity.naolifang.entity.enumEntity.StatusEnum;
import com.smartcity.naolifang.entity.enumEntity.VisitStatusEnum;
import com.smartcity.naolifang.entity.vo.*;
import com.smartcity.naolifang.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scene")
public class SceneController {

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

    @Autowired
    private AlarmEventInfoService alarmEventInfoService;

    @Autowired
    private DutyInfoService dutyInfoService;

    @Autowired
    private SignInfoService signInfoService;

    @Autowired
    private MessagePollingInfoService messagePollingInfoService;

    final String todayStartTime = DateTimeUtil.localDateToString(LocalDate.now()) + " 00:00:00";
    final String todayEndTime = DateTimeUtil.localDateToString(LocalDate.now()) + " 23:59:59";

    @RequestMapping("/camera/list")
    public Result listCamera() {
        List<DeviceInfo> deviceInfos = deviceInfoService.list(new QueryWrapper<DeviceInfo>()
                .eq("type", DeviceTypeEnum.CAMERA.getCode()));

        List<DeviceInfoVo> resultList = new ArrayList<>();
        for (DeviceInfo item : deviceInfos) {
            DeviceInfoVo data = new DeviceInfoVo(item);
            resultList.add(data);
        }

        return Result.ok(resultList);
    }

    @RequestMapping("/onCall")
    public Result onCall() {
        List<DutyInfo> dutyList = dutyInfoService.list(new QueryWrapper<DutyInfo>()
                .eq("is_delete", 0)
                .ge("start_time", todayStartTime)
                .le("start_time", todayEndTime)
                .or().ge("end_time", todayStartTime)
                .le("end_time", todayEndTime));
        List<DutyInfoVo> resultList = new ArrayList<>();
        for (DutyInfo item : dutyList) {
            DutyInfoVo data = new DutyInfoVo(item);
            Integer insiderId = item.getInsiderId();
            InsiderInfo insiderInfo = insiderInfoService.getById(insiderId);
            data.setImageUri(insiderInfo.getImageUri());
            resultList.add(data);
        }

        return Result.ok(resultList);
    }

    @RequestMapping("/message")
    public Result sceneMessageGet() {
        List<MessagePollingInfo> messageList = messagePollingInfoService.list(new QueryWrapper<MessagePollingInfo>()
                .eq("is_delete", 0)
                .eq("is_polling", 1)
                .orderByDesc("id"));

        File file = null;
        if (MessageController.pollingInterval.intValue() == 0) {
            try {
                file = ResourceUtils.getFile("classpath:application-dev.properties");
                FileInputStream inputStream = new FileInputStream(file);
                Properties properties = new Properties();
                properties.load(inputStream);

                MessageController.pollingInterval = Integer.valueOf(properties.getProperty("pollingInterval"));

                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("messageList", messageList);
        resultJson.put("interval", MessageController.pollingInterval);

        return Result.ok(resultJson);
    }

    @RequestMapping("/person/statistic")
    public Result statisticVisitor() {
        // 预约访客总数
        Integer totalVisitor = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .gt("visit_start_time", todayStartTime)
                .lt("visit_start_time", todayEndTime));
        // 内部人员总数
        Integer totalInsider = insiderInfoService.count();
        // 登记家属总数
        Integer totalDependant = dependantInfoService.count();
        // 在访人数
        Integer totalVisiting = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("status", VisitStatusEnum.SIGN_IN)
                .gt("visit_start_time", todayStartTime)
                .lt("visit_start_time", todayEndTime));
        // 超时人数
        Integer totalTimeout = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("status", VisitStatusEnum.TIME_OUT)
                .gt("visit_start_time", todayStartTime)
                .lt("visit_start_time", todayEndTime));
        // 签离人数
        Integer totalSignOut = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("status", VisitStatusEnum.SIGN_OUT)
                .gt("visit_start_time", todayStartTime)
                .lt("visit_start_time", todayEndTime));
        // 拒绝入营
        Integer totalReject = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("status", VisitStatusEnum.REJECT)
                .gt("visit_start_time", todayStartTime)
                .lt("visit_start_time", todayEndTime));
        // 请假人数
        Integer totalVacation = vacationInfoService.count(new QueryWrapper<VacationInfo>()
                .eq("cancel_vacation_status", 0));
        // 体温检测人数
        List<VisitorInfo> todayVisitor = visitorInfoService.list(new QueryWrapper<VisitorInfo>()
                .gt("visit_start_time", todayStartTime)
                .lt("visit_start_time", todayEndTime));
        List<Integer> todayVisitorId = todayVisitor.stream().map(VisitorInfo::getId).collect(Collectors.toList());
        Integer checkTemperature = 0;
        Integer fever = 0;
        if (!CollectionUtils.isEmpty(todayVisitorId)) {
            checkTemperature = signInfoService.count(new QueryWrapper<SignInfo>()
                    .in("visitor_id", todayVisitor)
                    .ne("temperature", null));
            // 疑似发烧人数
            fever = signInfoService.count(new QueryWrapper<SignInfo>()
                    .in("visitor_id", todayVisitor)
                    .gt("temperature", 37));
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("totalVisitor", totalVisitor);
        resultJson.put("totalInsider", totalInsider);
        resultJson.put("totalDependant", totalDependant);
        resultJson.put("totalVisiting", totalVisiting);
        resultJson.put("totalTimeout", totalTimeout);
        resultJson.put("totalSignOut", totalSignOut);
        resultJson.put("totalReject", totalReject);
        resultJson.put("totalVacation", totalVacation);
        resultJson.put("checkTemperature", checkTemperature);
        resultJson.put("fever", fever);

        return Result.ok(resultJson);
    }

    @RequestMapping("/device/statistic")
    public Result statisticDevice() {
        // 离线设备
        Integer offline = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .eq("status", StatusEnum.OFFLINE));
        // 正常设备
        Integer normal = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .eq("status", StatusEnum.OFFLINE));
        // 告警设备
        Integer alarm = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .eq("status", StatusEnum.ALARM));

        // 设备总数
        Integer total = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0));;
        // 摄像机
        Integer camera = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .eq("type", DeviceTypeEnum.CAMERA.getCode()));
        // 门禁
        Integer door = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .eq("type", DeviceTypeEnum.DOOR.getCode()));

        JSONObject resultJson = new JSONObject();
        resultJson.put("offline", offline);
        resultJson.put("normal", normal);
        resultJson.put("alarm", alarm);
        resultJson.put("total", total);
        resultJson.put("camera", camera);
        resultJson.put("door", door);

        return Result.ok(resultJson);
    }

    @RequestMapping("/alarm/statistic")
    public Result statisticAlarm() {
        String sevenDayBefore = DateTimeUtil.localDateToString(LocalDate.now().minusDays(7)) + " 00:00:00";
        // 今日告警总数
        Integer total = alarmEventInfoService.count(new QueryWrapper<AlarmEventInfo>()
                .gt("alarm_time", todayStartTime)
                .lt("alarm_time", todayEndTime));
        // 已处理告警数
        Integer handled = alarmEventInfoService.count(new QueryWrapper<AlarmEventInfo>()
                .eq("status", HandleStatusEnum.HANDLED.getCode())
                .gt("alarm_time", sevenDayBefore)
                .lt("alarm_time", todayEndTime));
        // 未处理告警数
        Integer unHandle = alarmEventInfoService.count(new QueryWrapper<AlarmEventInfo>()
                .eq("status", HandleStatusEnum.UN_HANDLE.getCode())
                .gt("alarm_time", sevenDayBefore)
                .lt("alarm_time", todayEndTime));
        // 处理中告警数
        Integer handling = alarmEventInfoService.count(new QueryWrapper<AlarmEventInfo>()
                .eq("status", HandleStatusEnum.HANDLING.getCode())
                .gt("alarm_time", sevenDayBefore)
                .lt("alarm_time", todayEndTime));

        List<AlarmEventInfo> alarmEventInfos = alarmEventInfoService.list(new QueryWrapper<AlarmEventInfo>()
                .orderByDesc("id")
                .last("limit 0, 10"));
        List<AlarmEventInfoVo> resultList = new ArrayList<>();
        for (AlarmEventInfo item : alarmEventInfos) {
            AlarmEventInfoVo data = new AlarmEventInfoVo(item);
            data.setId(item.getId());
            Integer deviceId = item.getDeviceId();
            DeviceInfo deviceInfo = deviceInfoService.getById(deviceId);

            data.setDeviceName(deviceInfo.getName());
            data.setAlarmTime(DateTimeUtil.localDateTimeToString(item.getAlarmTime()));
            data.setContent(item.getContent());
            data.setStatus(item.getStatus());
            data.setHandlePerson(item.getHandlePerson());
            resultList.add(data);
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("total", total);
        resultJson.put("handled", handled);
        resultJson.put("unHandle", unHandle);
        resultJson.put("handling", handling);
        resultJson.put("alarmEvents", resultList);

        return Result.ok(resultJson);
    }

}
