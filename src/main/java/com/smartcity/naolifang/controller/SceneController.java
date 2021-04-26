package com.smartcity.naolifang.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.*;
import com.smartcity.naolifang.entity.enumEntity.DeviceTypeEnum;
import com.smartcity.naolifang.entity.enumEntity.HandleStatusEnum;
import com.smartcity.naolifang.entity.enumEntity.StatusEnum;
import com.smartcity.naolifang.entity.enumEntity.VisitStatusEnum;
import com.smartcity.naolifang.entity.searchCondition.SceneCondition;
import com.smartcity.naolifang.entity.vo.*;
import com.smartcity.naolifang.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Autowired
    private CameraPollingInfoService cameraPollingInfoService;

    final String todayStartTime = DateTimeUtil.localDateToString(LocalDate.now()) + " 00:00:00";
    final String todayEndTime = DateTimeUtil.localDateToString(LocalDate.now()) + " 23:59:59";

    @RequestMapping("/camera/list")
    public Result listCamera() {
        List<DeviceInfo> deviceInfos = deviceInfoService.list(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .eq("type", DeviceTypeEnum.CAMERA.getCode()));

        List<DeviceInfoVo> resultList = new ArrayList<>();
        for (DeviceInfo item : deviceInfos) {
            DeviceInfoVo data = new DeviceInfoVo(item);
            resultList.add(data);
        }

        return Result.ok(resultList);
    }

    @RequestMapping("/camera/polling/save")
    public Result saveCameraPollingInfo(@RequestBody CameraPollingInfoBo cameraPollingInfoBo) {
        Integer userId = cameraPollingInfoBo.getUserId();
        List<Integer> cameraIds = cameraPollingInfoBo.getCameraIds();

        List<CameraPollingInfo> cameraPollingInfos = new ArrayList<>();
        cameraPollingInfoService.remove(new QueryWrapper<CameraPollingInfo>().eq("user_id", userId));
        for (Integer cameraId : cameraIds) {
            CameraPollingInfo cameraPollingInfo = new CameraPollingInfo();
            cameraPollingInfo.setUserId(userId);
            cameraPollingInfo.setCameraId(cameraId);
            cameraPollingInfos.add(cameraPollingInfo);
        }
        cameraPollingInfoService.saveBatch(cameraPollingInfos);

        return Result.ok();
    }

    @RequestMapping("/camera/polling/list")
    public Result listCameraPollingInfo(@RequestBody SceneCondition sceneCondition) {
        Integer userId = sceneCondition.getUserId();
        List<CameraPollingInfo> cameraPollingInfos = cameraPollingInfoService.list(new QueryWrapper<CameraPollingInfo>().eq("user_id", userId));

        List<DeviceInfoVo> cameraInfos = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cameraPollingInfos)) {
            for (CameraPollingInfo item : cameraPollingInfos) {
                Integer cameraId = item.getCameraId();
                DeviceInfo cameraInfo = deviceInfoService.getById(cameraId);
                DeviceInfoVo cameraInfoVo = new DeviceInfoVo(cameraInfo);
                cameraInfos.add(cameraInfoVo);
            }
        }
        return Result.ok(cameraInfos);
    }

    @RequestMapping("/onCall")
    public Result onCall() {
        String nowTime = DateTimeUtil.localDateTimeToString(LocalDateTime.now());
        List<DutyInfo> dutyList = dutyInfoService.list(new QueryWrapper<DutyInfo>()
                .eq("is_delete", 0)
                .le("start_time", nowTime)
                .ge("end_time", nowTime)
                .orderByDesc("id"));
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

    /**
     * 大屏获取所有设备
     * @return
     */
    @RequestMapping("/device")
    public Result sceneDeviceGet() {
        List<DeviceInfo> deviceInfos = deviceInfoService.list(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .ne("status", StatusEnum.FORBID.getCode())
                .ne("status", StatusEnum.INACTIVE.getCode()));
        List<DeviceInfoVo> resultList = new ArrayList<>();
        for (DeviceInfo item : deviceInfos) {
            DeviceInfoVo data = new DeviceInfoVo(item);
            Integer status = item.getStatus();
            if (status.equals(StatusEnum.ALARM.getCode())) {
                AlarmEventInfo alarmEventInfo = alarmEventInfoService.getOne(new QueryWrapper<AlarmEventInfo>().eq("device_id", item.getId()));

                data.setHappenStartTime(alarmEventInfo.getHappenStartTime());
                data.setHappenEndTime(alarmEventInfo.getHappenEndTime());
            }
            resultList.add(data);
        }

        return Result.ok(resultList);
    }

    @RequestMapping("/person/statistic")
    public Result statisticVisitor() {
        // 预约访客总数
        Integer totalVisitor = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("status", VisitStatusEnum.NO_VISIT.getCode()));
        // 内部人员总数
        Integer totalInsider = insiderInfoService.count();
        // 内部人员在营人数
        Integer inInsider = insiderInfoService.count(new QueryWrapper<InsiderInfo>()
                .eq("is_out", 0));
        // 内部人员离营人数
        Integer outInsider = insiderInfoService.count(new QueryWrapper<InsiderInfo>()
                .eq("is_out", 1));
        // 登记家属总数
        Integer totalDependant = dependantInfoService.count();
        // 家属人员在营人数
        Integer inDependant = dependantInfoService.count(new QueryWrapper<DependantInfo>()
                .eq("is_out", 0));
        // 家属人员离营人数
        Integer outDependant = dependantInfoService.count(new QueryWrapper<DependantInfo>()
                .eq("is_out", 1));
        // 在访人数
        Integer totalVisiting = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("status", VisitStatusEnum.SIGN_IN)
                .or().eq("status", VisitStatusEnum.TIME_OUT));
        // 超时人数
        Integer totalTimeout = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("status", VisitStatusEnum.TIME_OUT));
        // 签离人数
        Integer totalSignOut = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("status", VisitStatusEnum.SIGN_OUT));
        // 拒绝入营
        Integer totalReject = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("status", VisitStatusEnum.REJECT));
        // 请假人数
        Integer totalVacation = vacationInfoService.count(new QueryWrapper<VacationInfo>()
                .eq("cancel_vacation_status", 0));
        // 在营总人数
        Integer totalCount = inInsider + inDependant + totalVisiting + totalTimeout;
        // 体温检测人数
        List<VisitorInfo> todayVisitor = visitorInfoService.list(new QueryWrapper<VisitorInfo>());
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
                    .gt("temperature", 37.5));
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("inInsider", inInsider);
        resultJson.put("outInsider", outInsider);
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
        resultJson.put("totalCount", totalCount);

        return Result.ok(resultJson);
    }

    @RequestMapping("/device/statistic")
    public Result statisticDevice() {
        // 离线设备
        Integer offline = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .eq("status", StatusEnum.OFFLINE.getCode()));
        // 正常设备
        Integer normal = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .eq("status", StatusEnum.ONLINE.getCode()));
        // 告警设备
        Integer alarm = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .eq("status", StatusEnum.ALARM.getCode()));

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
        // 编码设备
        Integer encodeDevice = deviceInfoService.count(new QueryWrapper<DeviceInfo>()
                .eq("is_delete", 0)
                .eq("type", DeviceTypeEnum.ENCODE_DEVICE.getCode()));

        JSONObject resultJson = new JSONObject();
        resultJson.put("offline", offline);
        resultJson.put("normal", normal);
        resultJson.put("alarm", alarm);
        resultJson.put("total", total);
        resultJson.put("camera", camera);
        resultJson.put("door", door);
        resultJson.put("encodeDevice", encodeDevice);

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
                .gt("alarm_time", sevenDayBefore)
                .lt("alarm_time", todayEndTime)
                .orderByDesc("id"));
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
