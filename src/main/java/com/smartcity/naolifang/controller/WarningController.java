package com.smartcity.naolifang.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.bean.PushThread;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.common.util.HttpUtil;
import com.smartcity.naolifang.entity.AlarmEventInfo;
import com.smartcity.naolifang.entity.AlarmLevelInfo;
import com.smartcity.naolifang.entity.AlarmMalfunctionInfo;
import com.smartcity.naolifang.entity.DeviceInfo;
import com.smartcity.naolifang.entity.enumEntity.HandleStatusEnum;
import com.smartcity.naolifang.entity.enumEntity.HikivisionAlarmTypeEnum;
import com.smartcity.naolifang.entity.enumEntity.StatusEnum;
import com.smartcity.naolifang.entity.external.EventInfo;
import com.smartcity.naolifang.entity.external.HikivisionBaseEvent;
import com.smartcity.naolifang.entity.external.ParamInfo;
import com.smartcity.naolifang.entity.searchCondition.WarningCondition;
import com.smartcity.naolifang.entity.vo.AlarmEventInfoVo;
import com.smartcity.naolifang.entity.vo.AlarmLevelInfoVo;
import com.smartcity.naolifang.entity.vo.PageListVo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.AlarmEventInfoService;
import com.smartcity.naolifang.service.AlarmLevelInfoService;
import com.smartcity.naolifang.service.AlarmMalfunctionInfoService;
import com.smartcity.naolifang.service.DeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/warning")
public class WarningController {

    @Autowired
    private AlarmLevelInfoService alarmLevelInfoService;

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Autowired
    private AlarmEventInfoService alarmEventInfoService;

    @Autowired
    private AlarmMalfunctionInfoService alarmMalfunctionInfoService;

    @Autowired
    private Config config;

    /**
     * ??????????????????????????????????????????
     * @return
     */
    @RequestMapping("/hikivision/list")
    public Result listHikivisionWarning() {
        List<JSONObject> warningList = new ArrayList<>();
        for (HikivisionAlarmTypeEnum item : HikivisionAlarmTypeEnum.values()) {
            JSONObject data = new JSONObject();
            data.put("name", item.getName());
            data.put("code", item.getCode());
            warningList.add(data);
        }

        return Result.ok(warningList);
    }

    /**
     * ????????????????????????
     * @return
     */
    @RequestMapping("/level/save")
    public Result saveWarningLevel(@RequestBody AlarmLevelInfoVo alarmLevelInfoVo) {
        List<Integer> regions = alarmLevelInfoVo.getRegions();
        List<Long> malfunctionTypes = alarmLevelInfoVo.getMalfunctionTypes();
        Integer alarmType = alarmLevelInfoVo.getAlarmType();
        Integer alarmLevel = alarmLevelInfoVo.getAlarmLevel();

        // 1. ????????????????????????
        StringBuilder sb = new StringBuilder(20);
        for (Integer region : regions) {
            sb.append(region).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        // 2. ???????????????????????????
        AlarmLevelInfo data = new AlarmLevelInfo();
        data.setRegion(sb.toString());
        data.setAlarmType(alarmType);
        data.setAlarmLevel(alarmLevel);
        alarmLevelInfoService.saveOrUpdate(data);

        // 3. ??????????????????????????????????????????
        for (Long malfunctionType : malfunctionTypes) {
            AlarmMalfunctionInfo alarmMalfunctionInfo = new AlarmMalfunctionInfo();
            alarmMalfunctionInfo.setAlarmLevelId(data.getId());
            alarmMalfunctionInfo.setMalfunctionType(malfunctionType);
            alarmMalfunctionInfoService.saveOrUpdate(alarmMalfunctionInfo);
        }

        return Result.ok();
    }

    /**
     * ????????????????????????
     * @return
     */
    @RequestMapping("/level/list")
    public Result listWarningLevelList() {
        List<AlarmLevelInfo> dataList = alarmLevelInfoService.list();
        List<AlarmLevelInfoVo> deviceWarningList = new ArrayList<>();
        List<AlarmLevelInfoVo> eventWarningList = new ArrayList<>();

        for (AlarmLevelInfo item : dataList) {
            AlarmLevelInfoVo data = new AlarmLevelInfoVo();
            Integer alarmType = item.getAlarmType();

            String regionStr = item.getRegion();
            List<Integer> regions = new ArrayList<>();
            String[] regionArr = regionStr.split(",");
            for (String item1 : regionArr) {
                regions.add(Integer.valueOf(item1));
            }

            Integer levelId = item.getId();
            List<AlarmMalfunctionInfo> malfunctionInfos = alarmMalfunctionInfoService.list(new QueryWrapper<AlarmMalfunctionInfo>().eq("alarm_level_id", levelId));
            List<Long> malfunctionTypes = malfunctionInfos.stream().map(AlarmMalfunctionInfo::getMalfunctionType).collect(Collectors.toList());

            data.setId(item.getId());
            data.setRegions(regions);
            data.setAlarmType(alarmType);
            data.setAlarmLevel(item.getAlarmLevel());
            data.setMalfunctionTypes(malfunctionTypes);

            if (alarmType.intValue() == 0) {
                deviceWarningList.add(data);
            }
            if (alarmType.intValue() == 1) {
                eventWarningList.add(data);
            }
        }

        JSONObject resultJson = new JSONObject();
        resultJson.put("deviceWarningList", deviceWarningList);
        resultJson.put("eventWarningList", eventWarningList);

        return Result.ok(resultJson);
    }

    /**
     * ??????????????????
     * @param warningCondition
     * @return
     */
    @RequestMapping("/level/remove")
    public Result deleteWarningLevel(@RequestBody WarningCondition warningCondition) {
        List<Integer> ids = warningCondition.getIds();

        for (Integer id : ids) {
            alarmMalfunctionInfoService.remove(new QueryWrapper<AlarmMalfunctionInfo>().eq("alarm_level_id", id));
        }

        alarmLevelInfoService.removeByIds(ids);

        return Result.ok();
    }

    @RequestMapping("/list")
    public Result listDeviceWarning(@RequestBody WarningCondition warningCondition) {
        Integer pageNo = warningCondition.getPageNo();
        Integer pageSize = warningCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "????????????????????????????????????????????????????????????????????????????????????");
        }
        Integer offset = (pageNo - 1) * pageSize;
        String indexCode = warningCondition.getIndexCode();
        Integer alarmType = warningCondition.getAlarmType();
        Integer alarmLevel = warningCondition.getAlarmLevel();
        Integer status = warningCondition.getStatus();
        String alarmStartTime = warningCondition.getAlarmStartTime();
        String alarmEndTime = warningCondition.getAlarmEndTime();

        // 1. ??????????????????????????????
        Integer searchDeviceId = null;
        if (StringUtils.isNotBlank(indexCode)) {
            DeviceInfo deviceInfo = deviceInfoService.getOne(new QueryWrapper<DeviceInfo>().like("index_code", indexCode));
            if (null != deviceInfo) {
                searchDeviceId = deviceInfo.getId();
            }
        }
        List<AlarmEventInfo> dataList = alarmEventInfoService.list(new QueryWrapper<AlarmEventInfo>()
                .eq("alarm_type", alarmType)
                .eq(null != searchDeviceId, "device_id", searchDeviceId)
                .eq(null != alarmLevel, "alarm_level", alarmLevel)
                .eq(null != status, "status", status)
                .gt(StringUtils.isNotBlank(alarmStartTime), "alarm_time", alarmStartTime)
                .lt(StringUtils.isNotBlank(alarmEndTime), "alarm_time", alarmEndTime)
                .last("limit " + offset + ", " + pageSize));
        int totalCount = alarmEventInfoService.count(new QueryWrapper<AlarmEventInfo>()
                .eq("alarm_type", alarmType)
                .eq(null != searchDeviceId, "device_id", searchDeviceId)
                .eq(null != alarmLevel, "alarm_level", alarmLevel)
                .eq(null != status, "status", status)
                .gt(StringUtils.isNotBlank(alarmStartTime), "alarm_time", alarmStartTime)
                .lt(StringUtils.isNotBlank(alarmEndTime), "alarm_time", alarmEndTime));

        // 2. ????????????
        List<AlarmEventInfoVo> resultList = new ArrayList<>();
        Map<Integer, DeviceInfo> deviceInfoMap = new HashMap<>();
        for (AlarmEventInfo item : dataList) {
            AlarmEventInfoVo data = new AlarmEventInfoVo(item);

            Integer deviceId = item.getDeviceId();
            DeviceInfo deviceInfo = null;
            if (null == deviceInfoMap.get(deviceId)) {
                deviceInfo = deviceInfoService.getOne(new QueryWrapper<DeviceInfo>().eq("id", deviceId));
                deviceInfoMap.put(deviceId, deviceInfo);
            } else {
                deviceInfo = deviceInfoMap.get(deviceId);
            }
            data.setIndexCode(deviceInfo.getIndexCode());
            data.setRegion(deviceInfo.getRegion());
            data.setDeviceType(deviceInfo.getType());
            data.setDeviceName(deviceInfo.getName());
            data.setPosition(deviceInfo.getPosition());
            data.setPositionInfo(deviceInfo.getPositionInfo());

            resultList.add(data);
        }

        PageListVo pageListVo = new PageListVo(resultList, pageNo, pageSize, totalCount);

        return Result.ok(pageListVo);
    }

    @RequestMapping("/remove")
    public Result deleteDeviceWarning(@RequestBody WarningCondition warningCondition) {
        List<Integer> ids = warningCondition.getIds();

        alarmEventInfoService.removeByIds(ids);

        return Result.ok();
    }

    /**
     * ??????????????????
     * @param alarmEventInfoVo
     * @return
     */
    @RequestMapping("/save")
    public Result handleWarningInfo(@RequestBody AlarmEventInfoVo alarmEventInfoVo) {
        Integer id = alarmEventInfoVo.getId();
        String handlePerson = alarmEventInfoVo.getHandlePerson();
        String handleContent = alarmEventInfoVo.getHandleContent();
        Integer status = alarmEventInfoVo.getStatus();

        // 1. ???????????????????????????
        AlarmEventInfo alarmEventInfo = alarmEventInfoService.getById(id);
        alarmEventInfo.setHandlePerson(handlePerson);
        alarmEventInfo.setHandleContent(handleContent);
        alarmEventInfo.setHandleTime(LocalDateTime.now());
        alarmEventInfo.setStatus(status);
        alarmEventInfoService.saveOrUpdate(alarmEventInfo);

        // 2. ????????????????????????????????????????????????
        if (status.equals(HandleStatusEnum.HANDLED.getCode())) {
            DeviceInfo deviceInfo = deviceInfoService.getById(alarmEventInfo.getDeviceId());
            deviceInfo.setStatus(HandleStatusEnum.HANDLED.getCode());
            deviceInfoService.saveOrUpdate(deviceInfo);
        }

        return Result.ok();
    }

    @RequestMapping("/playback")
    public Result playbackWarningVideo(@RequestBody WarningCondition warningCondition) {
        Integer id = warningCondition.getId();
        AlarmEventInfo alarmEventInfo = alarmEventInfoService.getById(id);
        Integer deviceId = alarmEventInfo.getDeviceId();
        DeviceInfo deviceInfo = deviceInfoService.getById(deviceId);
        String indexCode = deviceInfo.getIndexCode();

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("cameraIndexCode", indexCode);
        paramMap.put("beginTime", alarmEventInfo.getHappenStartTime());
        paramMap.put("endTime", alarmEventInfo.getHappenEndTime());
        paramMap.put("protocol", "rtsp");
        String resultStr = HttpUtil.postToHikvisionPlatform(config.getHikivisionPlaybackUrl(), paramMap);
        JSONObject resultJson = JSONObject.parseObject(resultStr);

        // ????????????
//        String rtspUrl = resultJson.getString("url");
//        String token = UUID.randomUUID().toString();
//        PushThread.PushRunnable runnable = new PushThread.PushRunnable(rtspUrl, token);
//        PushThread.PushRunnable.es.execute(runnable);

        return Result.ok(resultJson);
    }

    @RequestMapping("/testPush")
    public Result testPush() {
//        String rtspUrl = "rtsp://admin:a12345678@203.88.202.226:554/h264/ch1/main/av_stream";
//        String token = UUID.randomUUID().toString();
//        PushThread.PushRunnable runnable = new PushThread.PushRunnable(rtspUrl, token);
//        PushThread.PushRunnable.es.execute(runnable);
//
//        String rtmpUrl = "rtmp://localhost:1935/live/" + token;
//        return Result.ok(rtmpUrl);
        return Result.ok();
    }

    /**
     * ???????????????????????????
     * @param hikivisionBaseEvent
     * @return
     */
    @RequestMapping("/external/save")
    public Result saveWarningEventInfo(@RequestBody HikivisionBaseEvent hikivisionBaseEvent) {
        ParamInfo params = hikivisionBaseEvent.getParams();
        List<EventInfo> eventInfos = params.getEvents();
        for (EventInfo item : eventInfos) {
            AlarmEventInfo alarmEventInfo = new AlarmEventInfo();
            Integer eventType = item.getEventType();
            String indexCode = item.getSrcIndex();
            String happenTime = item.getHappenTime();
            Integer status = item.getStatus();

            // 1. ??????????????????????????????????????????????????????
            DeviceInfo deviceInfo = deviceInfoService.getOne(new QueryWrapper<DeviceInfo>().eq("index_code", indexCode));
            Integer deviceId = deviceInfo.getId();
            Integer region = deviceInfo.getRegion();
//            List<AlarmMalfunctionInfo> malfunctionInfos = alarmMalfunctionInfoService
//                    .list(new QueryWrapper<AlarmMalfunctionInfo>().eq("malfunction_type", eventType));
//            List<Integer> alarmLevelIds = malfunctionInfos.stream().map(AlarmMalfunctionInfo::getAlarmLevelId).distinct().collect(Collectors.toList());
//            List<AlarmLevelInfo> alarmLevelInfos = alarmLevelInfoService.list(new QueryWrapper<AlarmLevelInfo>()
//                    .like("region", region)
//                    .in("id", alarmLevelIds));
//
//            // ???????????????????????????????????????????????????????????????
//            int finalAlarmLevel = -1;
//            if (alarmLevelInfos.size() > 1) {
//                for (AlarmLevelInfo item1 : alarmLevelInfos) {
//                    Integer alarmLevel = item1.getAlarmLevel();
//                    if (alarmLevel > finalAlarmLevel) {
//                        finalAlarmLevel = alarmLevel;
//                    }
//                }
//            }

            // 2. ????????????????????????
            alarmEventInfo.setContent(HikivisionAlarmTypeEnum.getDataByCode(eventType).getName());
            alarmEventInfo.setDeviceId(deviceId);
            alarmEventInfo.setAlarmTime(LocalDateTime.now());
            if (status == 1 || status == 0) {
                // ??????????????????10?????????5???????????????????????????????????????
                String startTime = DateTimeUtil.iso8601ToString(happenTime);
                LocalDateTime localDateTime = DateTimeUtil.stringToLocalDateTime(startTime);
                String happenStartTime  = String.valueOf(localDateTime.minusSeconds(10).getSecond());
                String happenEndTime = String.valueOf(localDateTime.plusSeconds(5).getSecond());

                alarmEventInfo.setHappenStartTime(happenStartTime);
                alarmEventInfo.setHappenEndTime(happenEndTime);
            }
            if (status == 2) {
                // ??????????????????????????????????????????????????????
                String endTime = DateTimeUtil.iso8601ToString(happenTime);
                LocalDateTime localDateTime = DateTimeUtil.stringToLocalDateTime(endTime);
                alarmEventInfo.setHappenEndTime(String.valueOf(localDateTime.plusSeconds(5).getSecond()));
            }
            alarmEventInfo.setStatus(0);
//            alarmEventInfo.setAlarmLevel(finalAlarmLevel);
            alarmEventInfo.setAlarmType(1);
            alarmEventInfoService.saveOrUpdate(alarmEventInfo);

            deviceInfo.setStatus(StatusEnum.ALARM.getCode());
            deviceInfoService.saveOrUpdate(deviceInfo);
        }

        return Result.ok();
    }
}
