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
import com.smartcity.naolifang.entity.DeviceInfo;
import com.smartcity.naolifang.entity.enumEntity.HikivisionAlarmTypeEnum;
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
    private Config config;

    /**
     * 获取海康所有的告警代码和名字
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
     * 保存告警等级信息
     * @return
     */
    @RequestMapping("/level/save")
    public Result saveWarningLevel(@RequestBody AlarmLevelInfoVo alarmLevelInfoVo) {
        List<Integer> regions = alarmLevelInfoVo.getRegions();
        List<Integer> malfunctionTypes = alarmLevelInfoVo.getMalfunctionTypes();
        Integer alarmType = alarmLevelInfoVo.getAlarmType();
        Integer alarmLevel = alarmLevelInfoVo.getAlarmLevel();

        for (Integer region : regions) {
            for (Integer malfunctionType : malfunctionTypes) {
                AlarmLevelInfo data = new AlarmLevelInfo();
                data.setAlarmLevel(alarmLevel);
                data.setAlarmType(alarmType);
                data.setRegion(region);
                data.setMalfunctionType(malfunctionType);
                alarmLevelInfoService.saveOrUpdate(data);
            }
        }

        return Result.ok();
    }

    /**
     * 列出告警等级信息
     * @return
     */
    @RequestMapping("/level/list")
    public Result listWarningLevelList() {
        List<AlarmLevelInfo> dataList = alarmLevelInfoService.list();
        List<AlarmLevelInfoVo> deviceWarningList = new ArrayList<>();
        List<AlarmLevelInfoVo> eventWarningList = new ArrayList<>();

        for (AlarmLevelInfo item : dataList) {
            Integer alarmType = item.getAlarmType();
            Integer alarmLevel = item.getAlarmLevel();
            if (alarmType.intValue() == 0) {
                AlarmLevelInfoVo data = this.packageAlarmLevelInfoVo(dataList, alarmType, alarmLevel);
                deviceWarningList.add(data);
            }
            if (alarmType.intValue() == 1) {
                AlarmLevelInfoVo data = this.packageAlarmLevelInfoVo(dataList, alarmType, alarmLevel);
                eventWarningList.add(data);
            }
        }

        JSONObject warningData = new JSONObject();
        warningData.put("deviceWarning", deviceWarningList);
        warningData.put("eventWarning", eventWarningList);

        return Result.ok(warningData);
    }

    /**
     * 组装告警等级数据VO
     * @param dataList
     * @param alarmType
     * @param alarmLevel
     * @return
     */
    private AlarmLevelInfoVo packageAlarmLevelInfoVo(List<AlarmLevelInfo> dataList, Integer alarmType, Integer alarmLevel) {
        AlarmLevelInfoVo data = new AlarmLevelInfoVo();
        Stream<AlarmLevelInfo> alarmLevelInfoStream = dataList.stream()
                .filter(item1 -> item1.getAlarmLevel().intValue() == alarmLevel)
                .filter(item1 -> item1.getAlarmType().intValue() == alarmType);
        List<Integer> regionList = alarmLevelInfoStream.map(AlarmLevelInfo::getRegion).collect(Collectors.toList());
        List<Integer> malfunctionTypeList = alarmLevelInfoStream.map(AlarmLevelInfo::getMalfunctionType).collect(Collectors.toList());

        data.setAlarmLevel(alarmLevel);
        data.setAlarmType(alarmType);
        data.setRegions(regionList);
        data.setMalfunctionTypes(malfunctionTypeList);

        return data;
    }

    /**
     * 删除告警信息
     * @param warningCondition
     * @return
     */
    @RequestMapping("/level/remove")
    public Result deleteWarningLevel(@RequestBody WarningCondition warningCondition) {
        List<Integer> ids = warningCondition.getIds();

        alarmLevelInfoService.removeByIds(ids);

        return Result.ok();
    }

    @RequestMapping("/device/list")
    public Result listDeviceWarning(@RequestBody WarningCondition warningCondition) {
        Integer pageNo = warningCondition.getPageNo();
        Integer pageSize = warningCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取设备告警信息列表失败，信息：传入的页码或数据条数为空");
        }
        Integer offset = (pageNo - 1) * pageSize;
        String indexCode = warningCondition.getIndexCode();
        Integer alarmType = warningCondition.getAlarmType();
        Integer alarmLevel = warningCondition.getAlarmLevel();
        Integer status = warningCondition.getStatus();
        String alarmStartTime = warningCondition.getAlarmStartTime();
        String alarmEndTime = warningCondition.getAlarmEndTime();

        // 1. 根据条件找出告警记录
        Integer searchDeviceId = null;
        if (StringUtils.isNotBlank(indexCode)) {
            DeviceInfo deviceInfo = deviceInfoService.getOne(new QueryWrapper<DeviceInfo>().eq("index_code", indexCode));
            searchDeviceId = deviceInfo.getId();
        }
        List<AlarmEventInfo> dataList = alarmEventInfoService.list(new QueryWrapper<AlarmEventInfo>()
                .eq("alarm_type", alarmType)
                .eq(null != searchDeviceId, "device_id", searchDeviceId)
                .eq(null != alarmLevel, "alarm_level", alarmLevel)
                .eq(null != status, "status", status)
                .gt(StringUtils.isNotBlank(alarmStartTime), "alarm_time", alarmStartTime)
                .lt(StringUtils.isNotBlank(alarmEndTime), "alarm_time", alarmEndTime)
                .last("limit " + offset + ", " + pageSize));
        int totalCount = alarmLevelInfoService.count(new QueryWrapper<AlarmLevelInfo>()
                .eq("alarm_type", alarmType)
                .eq(null != searchDeviceId, "device_id", searchDeviceId)
                .eq(null != alarmLevel, "alarm_level", alarmLevel)
                .eq(null != status, "status", status)
                .gt(StringUtils.isNotBlank(alarmStartTime), "alarm_time", alarmStartTime)
                .lt(StringUtils.isNotBlank(alarmEndTime), "alarm_time", alarmEndTime));

        // 2. 组装数据
        List<AlarmEventInfoVo> resultList = new ArrayList<>();
        Map<Integer, DeviceInfo> deviceInfoMap = new HashMap<>();
        for (AlarmEventInfo item : dataList) {
            AlarmEventInfoVo data = new AlarmEventInfoVo(item);

            Integer deviceId = item.getDeviceId();
            DeviceInfo deviceInfo = null;
            if (null == deviceInfoMap.get(deviceId)) {
                deviceInfo = deviceInfoService.getOne(new QueryWrapper<DeviceInfo>().eq("device_id", deviceId));
                deviceInfoMap.put(deviceId, deviceInfo);
            } else {
                deviceInfo = deviceInfoMap.get(deviceId);
            }
            data.setIndexCode(deviceInfo.getIndexCode());
            data.setRegion(deviceInfo.getRegion());

            resultList.add(data);
        }

        PageListVo pageListVo = new PageListVo(resultList, pageNo, pageSize, totalCount);

        return Result.ok(pageListVo);
    }

    @RequestMapping("/device/remove")
    public Result deleteDeviceWarning(@RequestBody WarningCondition warningCondition) {
        List<Integer> ids = warningCondition.getIds();

        alarmEventInfoService.removeByIds(ids);

        return Result.ok();
    }

    /**
     * 更新处理信息
     * @param alarmEventInfoVo
     * @return
     */
    @RequestMapping("/handle")
    public Result handleWarningInfo(@RequestBody AlarmEventInfoVo alarmEventInfoVo) {
        Integer id = alarmEventInfoVo.getId();
        String handlePerson = alarmEventInfoVo.getHandlePerson();
        String handleContent = alarmEventInfoVo.getHandleContent();
        Integer status = alarmEventInfoVo.getStatus();

        AlarmEventInfo alarmEventInfo = alarmEventInfoService.getById(id);
        alarmEventInfo.setHandlePerson(handlePerson);
        alarmEventInfo.setHandleContent(handleContent);
        alarmEventInfo.setHandleTime(LocalDateTime.now());
        alarmEventInfo.setStatus(status);
        alarmEventInfoService.saveOrUpdate(alarmEventInfo);

        return Result.ok();
    }

    @RequestMapping("/playback")
    public Result playbackWarningVideo(@RequestBody WarningCondition warningCondition) {
        Integer id = warningCondition.getId();
        AlarmEventInfo alarmEventInfo = alarmEventInfoService.getById(id);
        Integer deviceId = alarmEventInfo.getDeviceId();
        DeviceInfo deviceInfo = deviceInfoService.getById(deviceId);
        String indexCode = deviceInfo.getIndexCode();

        String happenTime = alarmEventInfo.getHappenTime();
        String dateTime = DateTimeUtil.iso8601ToString(happenTime);
        LocalDateTime localDateTime = DateTimeUtil.stringToLocalDateTime(dateTime);
        localDateTime.minusSeconds(15);
        dateTime = DateTimeUtil.localDateTimeToString(localDateTime);
        String startTime = DateTimeUtil.stringToIso8601(dateTime);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("cameraIndexCode", indexCode);
        paramMap.put("beginTime", startTime);
        paramMap.put("endTime", happenTime);
        paramMap.put("protocol", "rtsp");
        String resultStr = HttpUtil.doPost(config.getHikivisionPlaybackUrl(), paramMap);
        JSONObject resultJson = JSONObject.parseObject(resultStr);

        // 开启推流
        String rtspUrl = resultJson.getString("url");
        String token = UUID.randomUUID().toString();
        PushThread.PushRunnable runnable = new PushThread.PushRunnable(rtspUrl, token);
        PushThread.PushRunnable.es.execute(runnable);

        return Result.ok(resultJson);
    }

    @RequestMapping("/testPush")
    public Result testPush() {
        String rtspUrl = "rtsp://admin:a12345678@203.88.202.226:554/h264/ch1/main/av_stream";
        String token = UUID.randomUUID().toString();
        PushThread.PushRunnable runnable = new PushThread.PushRunnable(rtspUrl, token);
        PushThread.PushRunnable.es.execute(runnable);

        String rtmpUrl = "rtmp://localhost:1935/live/" + token;
        return Result.ok(rtmpUrl);
    }

    /**
     * 供海康传入告警信息
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

            // 1. 根据设备信息，找出设备编号和告警级别
            DeviceInfo deviceInfo = deviceInfoService.getOne(new QueryWrapper<DeviceInfo>().eq("index_code", indexCode));
            Integer deviceId = deviceInfo.getId();
            Integer region = deviceInfo.getRegion();
            AlarmLevelInfo alarmLevelInfo = alarmLevelInfoService.getOne(new QueryWrapper<AlarmLevelInfo>()
                    .eq("region", region)
                    .eq("malfunction_type", eventType));

            // 2. 组装告警记录数据
            alarmEventInfo.setContent(HikivisionAlarmTypeEnum.getDataByCode(eventType).getName());
            alarmEventInfo.setDeviceId(deviceId);
            alarmEventInfo.setAlarmTime(LocalDateTime.now());
            alarmEventInfo.setHappenTime(happenTime);
            alarmEventInfo.setStatus(0);
            alarmEventInfo.setAlarmLevel(alarmLevelInfo.getAlarmLevel());
            alarmEventInfo.setAlarmType(1);

            alarmEventInfoService.saveOrUpdate(alarmEventInfo);
        }

        return Result.ok();
    }
}