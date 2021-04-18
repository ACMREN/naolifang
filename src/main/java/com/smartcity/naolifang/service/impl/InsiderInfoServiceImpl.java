package com.smartcity.naolifang.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.common.util.HttpUtil;
import com.smartcity.naolifang.entity.DependantInfo;
import com.smartcity.naolifang.entity.DeviceInfo;
import com.smartcity.naolifang.entity.DoorPermissionInfo;
import com.smartcity.naolifang.entity.InsiderInfo;
import com.smartcity.naolifang.entity.enumEntity.GenderEnum;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.mapper.InsiderInfoMapper;
import com.smartcity.naolifang.service.DependantInfoService;
import com.smartcity.naolifang.service.DeviceInfoService;
import com.smartcity.naolifang.service.DoorPermissionInfoService;
import com.smartcity.naolifang.service.InsiderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 营区内部人员表 服务实现类
 * </p>
 *
 * @author karl
 * @since 2021-03-04
 */
@Service
public class InsiderInfoServiceImpl extends ServiceImpl<InsiderInfoMapper, InsiderInfo> implements InsiderInfoService {

    @Autowired
    private Config config;

    @Autowired
    private DependantInfoService dependantInfoService;

    @Autowired
    private DoorPermissionInfoService doorPermissionInfoService;

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Override
    public List<String> getPersonFromHikivisionPlatform() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("resourceType", "person");

        String resultStr = HttpUtil.postToHikvisionPlatform(config.getHikivisionGetPersonListUrl(), paramMap);
        if (StringUtils.isBlank(resultStr)){
            return new ArrayList<>();
        }
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        List<JSONObject> personDatas = JSONObject.parseArray(resultJson.getJSONObject("data").getString("list"), JSONObject.class);
        List<String> personIds = new ArrayList<>();
        for (JSONObject personData : personDatas) {
            String personId = personData.getString("personId");
            personIds.add(personId);
        }

        return personIds;
    }

    @Override
    public String addPersonToHikivisionPlatform(Integer id, Integer type) {
        // 1. 获取基本信息
        String name = "";
        Integer gender = 0;
        String imageUri = "";
        String idCard = "";
        InsiderInfo insiderInfo = null;
        DependantInfo dependantInfo = null;
        if (type == 0) {
            insiderInfo = this.getById(id);
            name = insiderInfo.getName();
            gender = GenderEnum.getDataByCode(insiderInfo.getGender()).getCode() + 1;
            idCard = insiderInfo.getIdCard();
            imageUri = insiderInfo.getImageUri();
        } else {
            dependantInfo = dependantInfoService.getById(id);
            name = dependantInfo.getName();
            gender = GenderEnum.getDataByCode(dependantInfo.getGender()).getCode() + 1;
            idCard = dependantInfo.getIdCard();
            imageUri = dependantInfo.getImageUri();
        }
        String fileName = imageUri.substring(imageUri.lastIndexOf("/"));
        String filePath = config.getAvatarDocPath() + fileName;

        // 2. 把人脸照片转换成base64编码
        byte[] data = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            data = new byte[fis.available()];
            fis.read(data);
            fis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Base64.Encoder encoder = Base64.getEncoder();
        String base64Str = encoder.encodeToString(data);
        List<JSONObject> faces = new ArrayList<>();
        JSONObject faceData = new JSONObject();
        faceData.put("faceData", base64Str);
        faces.add(faceData);

        // 3. 放入参数列表
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("certificateType", 111);
        paramMap.put("certificateNo", idCard);
        paramMap.put("personName", name);
        paramMap.put("gender", gender);
        paramMap.put("orgIndexCode", this.getRootOrg());
        paramMap.put("faces", faces);

        // 4. 请求结果
        String resultStr = HttpUtil.postToHikvisionPlatform(config.getHikivisionAddPersonUrl(), paramMap);
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        String code = resultJson.getString("code");
        if (!code.equals("0")) {
            return null;
        }

        // 5. 更新对应人员信息的唯一标识
        String personId = resultJson.getJSONObject("data").getString("personId");
        if (type == 0) {
            insiderInfo.setIndexCode(personId);
            this.saveOrUpdate(insiderInfo);
        } else {
            dependantInfo.setIndexCode(personId);
            dependantInfoService.saveOrUpdate(dependantInfo);
        }

        return personId;
    }

    @Override
    public String getRootOrg() {
        String resultStr = HttpUtil.postToHikvisionPlatform(config.getHikivisionRootOrgUrl(), null);

        JSONObject resultJson = JSONObject.parseObject(resultStr);
        String orgIndexCode = resultJson.getString("orgIndexCode");

        return orgIndexCode;
    }

    @Override
    public Result batchConfigPermissionToHikivisionPlatform(List<String> indexCodes, List<String> deviceIndexCodes, Integer configType) {
        // 1. 配置个人数据
        List<JSONObject> personDatas = new ArrayList<>();
        JSONObject personData = new JSONObject();
        personData.put("indexCodes", indexCodes);
        personData.put("personDataType", "person");
        personDatas.add(personData);

        // 2. 配置设备数据
        List<JSONObject> resourceInfos = new ArrayList<>();
        packageResourceInfos(deviceIndexCodes, resourceInfos);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("personDatas", personDatas);
        paramMap.put("resourceInfos", resourceInfos);

        // 3. 请求添加权限的接口
        String configUrl = "";
        if (configType == 0) {
            configUrl = config.getHikivisionAddPermissionUrl();
        } else {
            configUrl = config.getHikivisionDeletePersonUrl();
        }
        String resultStr = HttpUtil.postToHikvisionPlatform(configUrl, paramMap);
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        String code = resultJson.getString("code");
        if (!code.equals("0")) {
            String msg = resultJson.getString("msg");
            return Result.fail(500, code + ":" + msg);
        }

        // 4. 配置设备数据
        resourceInfos = new ArrayList<>();
        packageResourceInfos(deviceIndexCodes, resourceInfos);
        paramMap = new HashMap<>();
        paramMap.put("taskType", 5);
        paramMap.put("resourceInfos", resourceInfos);

        // 5. 请求出入权限配置快捷下载接口
        resultStr = HttpUtil.postToHikvisionPlatform(config.getHikivisionPermissionCfgUrl(), paramMap);
        resultJson = JSONObject.parseObject(resultStr);
        code = resultJson.getString("code");
        if (!code.equals("0")) {
            String msg = resultJson.getString("msg");
            return Result.fail(500, code + ":" + msg);
        }
        String taskId = resultJson.getJSONObject("data").getString("taskId");

        // 6. 配置下载任务参数
        paramMap = new HashMap<>();
        paramMap.put("taskId", taskId);

        // 7. 请求下载任务接口
        resultStr = HttpUtil.postToHikvisionPlatform(config.getHikivisionDownloadTaskUrl(), paramMap);
        resultJson = JSONObject.parseObject(resultStr);
        code = resultJson.getString("code");
        if (!code.equals("0")) {
            String msg = resultJson.getString("msg");
            return Result.fail(500, code + ":" + msg);
        }

        // 8. 查看下载任务的完成情况
        Result result = this.checkDownloadTask(paramMap);

        // 9. 统计下载情况并生成下发记录
        this.handleDownloadResult(indexCodes, deviceIndexCodes, taskId);

        return result;
    }

    /**
     * 检查下载任务的完成情况
     * @param paramMap
     * @return
     */
    private Result checkDownloadTask(Map<String, Object> paramMap) {
        String resultStr = HttpUtil.postToHikvisionPlatform(config.getHikivisionCheckTaskUrl(), paramMap);
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        String code = resultJson.getString("code");
        if (!code.equals("0")) {
            String msg = resultJson.getString("msg");
            return Result.fail(500, code + ":" + msg);
        }
        Boolean isFinish = resultJson.getJSONObject("data").getBoolean("isDownloadFinished");
        if (!isFinish) {
            this.checkDownloadTask(paramMap);
        }
        return Result.ok(resultJson);
    }

    private void packageResourceInfos(List<String> deviceIndexCodes, List<JSONObject> resourceInfos) {
        for (String deviceIndexCode : deviceIndexCodes) {
            JSONObject resourceInfo = new JSONObject();
            resourceInfo.put("resourceIndexCode", deviceIndexCode);
            resourceInfo.put("resourceType", "door");
            List<Integer> channelNos = new ArrayList<>();
            channelNos.add(1);
            resourceInfo.put("channelNos", channelNos);
            resourceInfos.add(resourceInfo);
        }
    }

    /**
     * 处理下载结果
     */
    private void handleDownloadResult(List<String> indexCodes, List<String> deviceIndexCodes, String taskId) {
        // 1. 组装人员indexCodeMap
        List<JSONObject> personInfos = new ArrayList<>();
        this.packingPersonInfo(indexCodes, personInfos);
        Map<String, JSONObject> personIndexCodeMap = new HashMap<>();
        for (JSONObject personInfo : personInfos) {
            personIndexCodeMap.put(personInfo.getString("indexCode"), personInfo);
        }

        // 2. 保存下发记录
        this.savePermissionRecord(deviceIndexCodes, personIndexCodeMap, taskId);
    }

    /**
     * 组装人员信息
     * @param indexCodes
     * @param personInfos
     */
    private void packingPersonInfo(List<String> indexCodes, List<JSONObject> personInfos) {
        List<InsiderInfo> insiderInfos = this.list(new QueryWrapper<InsiderInfo>().in("index_code", indexCodes));
        List<DependantInfo> dependantInfos = dependantInfoService.list(new QueryWrapper<DependantInfo>().in("index_code", indexCodes));
        for (InsiderInfo item : insiderInfos) {
            JSONObject personInfo = new JSONObject();
            personInfo.put("id", item.getId());
            personInfo.put("indexCode", item.getIndexCode());
            personInfo.put("name", item.getName());
            personInfo.put("idCard", item.getIdCard());
            personInfo.put("imageUri", item.getImageUri());
            personInfo.put("personType", 0);
            personInfos.add(personInfo);
        }
        for (DependantInfo item : dependantInfos) {
            JSONObject personInfo = new JSONObject();
            personInfo.put("id", item.getId());
            personInfo.put("indexCode", item.getIndexCode());
            personInfo.put("name", item.getName());
            personInfo.put("idCard", item.getIdCard());
            personInfo.put("imageUri", item.getImageUri());
            personInfo.put("personType", 1);
            personInfos.add(personInfo);
        }
    }

    /**
     * 保存下发记录
     * @param deviceIndexCodes
     * @param personIndexCodeMap
     * @param taskId
     */
    private void savePermissionRecord(List<String> deviceIndexCodes, Map<String, JSONObject> personIndexCodeMap, String taskId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("taskId", taskId);
        for (String deviceIndexCode : deviceIndexCodes) {
            DeviceInfo deviceInfo = deviceInfoService.getOne(new QueryWrapper<DeviceInfo>().eq("index_code", deviceIndexCode));
            JSONObject resourceInfo = new JSONObject();
            resourceInfo.put("resourceIndexCode", deviceIndexCode);
            resourceInfo.put("resourceType", "door");
            List<Integer> channelNos = new ArrayList<>();
            channelNos.add(1);
            resourceInfo.put("channelNos", channelNos);

            String resultStr = HttpUtil.postToHikvisionPlatform(config.getHikivisionTaskDetailUrl(), paramMap);
            JSONObject resultJson = JSONObject.parseObject(resultStr);

            List<JSONObject> downloadResultList = JSONObject.parseArray(resultJson.getJSONObject("data").getString("list"), JSONObject.class);
            List<DoorPermissionInfo> doorPermissionInfos = new ArrayList<>();
            for (JSONObject item : downloadResultList) {
                String personId = item.getString("personId");
                Integer downloadResult = item.getInteger("persondownloadResult");
                JSONObject personInfo = personIndexCodeMap.get(personId);
                if (null != personInfo) {
                    DoorPermissionInfo data = new DoorPermissionInfo();
                    data.setPersonId(personInfo.getInteger("id"));
                    data.setName(personInfo.getString("name"));
                    data.setIdCard(personInfo.getString("idCard"));
                    data.setImageUri(personInfo.getString("imageUri"));
                    data.setPersonType(personInfo.getInteger("personType"));
                    data.setDeviceIndexCode(deviceIndexCode);
                    data.setDeviceName(deviceInfo.getName());
                    data.setPositionInfo(deviceInfo.getPositionInfo());
                    data.setStatus(downloadResult);
                    doorPermissionInfos.add(data);
                }
            }
            doorPermissionInfoService.saveOrUpdateBatch(doorPermissionInfos);
        }
    }
}
