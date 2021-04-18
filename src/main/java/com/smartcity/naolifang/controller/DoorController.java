package com.smartcity.naolifang.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.entity.DependantInfo;
import com.smartcity.naolifang.entity.InsiderInfo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.DependantInfoService;
import com.smartcity.naolifang.service.InsiderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/addPermission")
    public Result addDoorPermission(List<Integer> insiderIds, List<Integer> dependantIds, List<String> deviceIndexCodes) {
        Map<String, Object> dataMap = new HashMap<>();
        // 1. 对已经添加到海康平台和还没添加到海康平台的人员进行分类
        this.diffPerson(dataMap, insiderIds, dependantIds);
        // 2. 添加还没在海康平台的人员添加到海康平台
        this.addPersonToHikivisionPlatform(dataMap);
        // 3. 批量添加权限
        this.batchAddDoorPermission(dataMap, deviceIndexCodes);

        return Result.ok();
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
