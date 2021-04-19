package com.smartcity.naolifang.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.entity.DependantInfo;
import com.smartcity.naolifang.entity.DoorPermissionInfo;
import com.smartcity.naolifang.entity.InsideOutRecord;
import com.smartcity.naolifang.entity.InsiderInfo;
import com.smartcity.naolifang.entity.searchCondition.IOManagerCondition;
import com.smartcity.naolifang.entity.vo.InsideOutRecordVo;
import com.smartcity.naolifang.entity.vo.PageListVo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.DependantInfoService;
import com.smartcity.naolifang.service.DoorPermissionInfoService;
import com.smartcity.naolifang.service.InsideOutRecordService;
import com.smartcity.naolifang.service.InsiderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/IOManager")
public class DoorController {

    @Autowired
    private InsiderInfoService insiderInfoService;

    @Autowired
    private DependantInfoService dependantInfoService;

    @Autowired
    private DoorPermissionInfoService doorPermissionInfoService;

    @Autowired
    private InsideOutRecordService insideOutRecordService;

    @RequestMapping("/addPermission")
    public Result addDoorPermission(@RequestBody IOManagerCondition ioManagerCondition) {
        Map<String, Object> dataMap = new HashMap<>();
        List<Integer> insiderIds = ioManagerCondition.getInsiderIds();
        List<Integer> dependantIds = ioManagerCondition.getDependantIds();
        List<String> deviceIndexCodes = ioManagerCondition.getDeviceIndexCodes();

        // 1. 对已经添加到海康平台和还没添加到海康平台的人员进行分类
        this.diffPerson(dataMap, insiderIds, dependantIds);
        // 2. 添加还没在海康平台的人员添加到海康平台
        this.addPersonToHikivisionPlatform(dataMap);
        // 3. 批量添加权限
        this.batchAddDoorPermission(dataMap, deviceIndexCodes);

        return Result.ok();
    }

    @RequestMapping("/permissionRecord/list")
    public Result listPermissionRecord(@RequestBody IOManagerCondition ioManagerCondition) {
        Integer pageNo = ioManagerCondition.getPageNo();
        Integer pageSize = ioManagerCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取下发记录信息失败，信息：传入的页码或数据条数为空");
        }
        Integer offset = (pageNo - 1) * pageSize;

        String name = ioManagerCondition.getName();
        String idCard = ioManagerCondition.getIdCard();
        Integer personType = ioManagerCondition.getPersonType();
        String deviceName = ioManagerCondition.getDeviceName();

        List<DoorPermissionInfo> dataList = doorPermissionInfoService.list(new QueryWrapper<DoorPermissionInfo>()
                .eq(null != personType, "person_type", personType)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(idCard), "id_card", idCard)
                .like(StringUtils.isNotBlank(deviceName), "device_name", deviceName)
                .last("limit " + pageNo + ", " + offset));
        Integer totalCount = doorPermissionInfoService.count(new QueryWrapper<DoorPermissionInfo>()
                .eq(null != personType, "person_type", personType)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(idCard), "id_card", idCard)
                .like(StringUtils.isNotBlank(deviceName), "device_name", deviceName));

        PageListVo pageListVo = new PageListVo(dataList, pageNo, pageSize, totalCount);
        return Result.ok(pageListVo);
    }

    @RequestMapping("/permissionRecord/config")
    public Result configPermissionRecord(@RequestBody IOManagerCondition ioManagerCondition) {
        Integer id = ioManagerCondition.getId();

        DoorPermissionInfo doorPermissionInfo = doorPermissionInfoService.getById(id);
        String indexCode = doorPermissionInfo.getPersonIndexCode();
        String deviceIndexCode = doorPermissionInfo.getDeviceIndexCode();
        Integer operationType = ioManagerCondition.getOperationType();

        insiderInfoService.configPermissionFromHikivisionPlatform(doorPermissionInfo, indexCode, deviceIndexCode, operationType);

        return Result.ok();
    }

    @RequestMapping("/insideOutRecord/list")
    public Result listInsideOutRecord(@RequestBody IOManagerCondition ioManagerCondition) {
        Integer pageNo = ioManagerCondition.getPageNo();
        Integer pageSize = ioManagerCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取下发记录信息失败，信息：传入的页码或数据条数为空");
        }
        Integer offset = (pageNo - 1) * pageSize;

        String name = ioManagerCondition.getName();
        String idCard = ioManagerCondition.getIdCard();
        Integer personType = ioManagerCondition.getPersonType();
        String deviceName = ioManagerCondition.getDeviceName();
        Integer type = ioManagerCondition.getType();
        String startTime = ioManagerCondition.getStartTime();
        String endTime = ioManagerCondition.getEndTime();

        List<InsideOutRecord> dataList = insideOutRecordService.list(new QueryWrapper<InsideOutRecord>()
                .eq(null != personType, "person_type", personType)
                .eq(null != type, "type", type)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(idCard), "id_card", idCard)
                .like(StringUtils.isNotBlank(deviceName), "device_name", deviceName)
                .gt(StringUtils.isNotBlank(startTime), "time", startTime)
                .lt(StringUtils.isNotBlank(endTime), "time", endTime)
                .last("limit " + pageNo + ", " + pageSize));
        Integer totalCount = insideOutRecordService.count(new QueryWrapper<InsideOutRecord>()
                .eq(null != personType, "person_type", personType)
                .eq(null != type, "type", type)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(idCard), "id_card", idCard)
                .like(StringUtils.isNotBlank(deviceName), "device_name", deviceName)
                .gt(StringUtils.isNotBlank(startTime), "time", startTime)
                .lt(StringUtils.isNotBlank(endTime), "time", endTime));

        List<InsideOutRecordVo> resultList = new ArrayList<>();
        for (InsideOutRecord item : dataList) {
            InsideOutRecordVo data = new InsideOutRecordVo(item);
            resultList.add(data);
        }

        return Result.ok(resultList);
    }

    /**
     * 人员分类
     * @param dataMap
     * @param insiderIds
     * @param dependantIds
     */
    private void diffPerson(Map<String, Object> dataMap, List<Integer> insiderIds, List<Integer> dependantIds) {
        List<InsiderInfo> insiderInfos = insiderInfoService.listByIds(insiderIds);
        List<DependantInfo> dependantInfos = dependantInfoService.listByIds(dependantIds);
        List<Integer> notExistInsiderIds = new ArrayList<>();
        List<Integer> notExistDependantIds = new ArrayList<>();
        List<String> existPersonIds = new ArrayList<>();
        for (InsiderInfo item : insiderInfos) {
            if (StringUtils.isNotBlank(item.getIndexCode())) {
                existPersonIds.add(item.getIndexCode());
            } else {
                notExistInsiderIds.add(item.getId());
            }
        }
        for (DependantInfo item : dependantInfos) {
            if (StringUtils.isNotBlank(item.getIndexCode())) {
                existPersonIds.add(item.getIndexCode());
            } else {
                notExistDependantIds.add(item.getId());
            }
        }

        dataMap.put("existPersonIds", existPersonIds);
        dataMap.put("notExistInsiderIds", notExistInsiderIds);
        dataMap.put("notExistDependantIds", notExistDependantIds);
    }

    /**
     * 添加人员到海康平台
     * @param dataMap
     */
    private void addPersonToHikivisionPlatform(Map<String, Object> dataMap) {
        List<Integer> notExistInsiderIds = (List<Integer>) dataMap.get("notExistInsiderIds");
        List<Integer> notExistDependantIds = (List<Integer>) dataMap.get("notExistDependantIds");
        List<String> existPersonIds = (List<String>) dataMap.get("existPersonIds");
        for (Integer id : notExistInsiderIds) {
            existPersonIds.add(insiderInfoService.addPersonToHikivisionPlatform(id, 0));
        }
        for (Integer id : notExistDependantIds) {
            existPersonIds.add(insiderInfoService.addPersonToHikivisionPlatform(id, 1));
        }
    }

    /**
     * 批量添加门禁权限
     * @param dataMap
     * @param deviceIndexCodes
     */
    private void batchAddDoorPermission(Map<String, Object> dataMap, List<String> deviceIndexCodes) {
        List<String> existPersonIds = (List<String>) dataMap.get("existPersonIds");
        insiderInfoService.batchConfigPermissionToHikivisionPlatform(existPersonIds, deviceIndexCodes, 0);
    }

}
