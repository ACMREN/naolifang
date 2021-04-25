package com.smartcity.naolifang.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.*;
import com.smartcity.naolifang.entity.enumEntity.GenderEnum;
import com.smartcity.naolifang.entity.enumEntity.HikivisionEventTypeEnum;
import com.smartcity.naolifang.entity.enumEntity.VisitStatusEnum;
import com.smartcity.naolifang.entity.external.EventInfo;
import com.smartcity.naolifang.entity.external.HikivisionBaseEvent;
import com.smartcity.naolifang.entity.searchCondition.RosterCondition;
import com.smartcity.naolifang.entity.searchCondition.UserCondition;
import com.smartcity.naolifang.entity.vo.DependantInfoVo;
import com.smartcity.naolifang.entity.vo.InsiderInfoVo;
import com.smartcity.naolifang.entity.vo.PageListVo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.DependantInfoService;
import com.smartcity.naolifang.service.DeviceInfoService;
import com.smartcity.naolifang.service.InsideOutRecordService;
import com.smartcity.naolifang.service.InsiderInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/roster")
public class RosterController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private InsiderInfoService insiderInfoService;

    @Autowired
    private DependantInfoService dependantInfoService;

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Autowired
    private InsideOutRecordService insideOutRecordService;

    /**
     * 新增/更新内部人员
     * @param insiderInfoVo
     * @return
     */
    @RequestMapping("/insider/save")
    public Result saveInsider(@RequestBody InsiderInfoVo insiderInfoVo) {
        InsiderInfo insiderInfo = new InsiderInfo(insiderInfoVo);
        if (null == insiderInfo.getCreateTime()) {
            insiderInfo.setCreateTime(LocalDateTime.now());
        }
        insiderInfo.setUpdateTime(LocalDateTime.now());

        insiderInfoService.saveOrUpdate(insiderInfo);

        return Result.ok(insiderInfo.getId());
    }

    /**
     * 获取内部人员列表
     * @param rosterCondition
     * @return
     */
    @RequestMapping("/insider/list")
    public Result getInsiderList(@RequestBody RosterCondition rosterCondition) {
        Integer pageNo = rosterCondition.getPageNo();
        Integer pageSize = rosterCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取营内人员信息失败，信息：传入的页数和页码为空");
        }
        String name = rosterCondition.getName();
        String department = rosterCondition.getDepartment();
        String phone = rosterCondition.getPhone();
        String idCard = rosterCondition.getIdCard();
        String rankNum = rosterCondition.getRankNum();
        String gender = rosterCondition.getGender();
        Integer genderInt = 0;
        if (StringUtils.isNotBlank(gender)) {
            genderInt = GenderEnum.getDataByName(gender).getCode();
        }
        Integer offset = (pageNo - 1) * pageSize;

        List<InsiderInfo> dataList = insiderInfoService.list(new QueryWrapper<InsiderInfo>()
                .eq("is_account", 0)
                .eq(StringUtils.isNotBlank(gender), "gender", genderInt)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(idCard), "id_card", idCard)
                .like(StringUtils.isNotBlank(phone), "phone", phone)
                .like(StringUtils.isNotBlank(department), "department", department)
                .like(StringUtils.isNotBlank(rankNum), "rank_num", rankNum)
                .orderByDesc("id")
                .last("limit " + offset + ", " + pageSize));
        Integer totalCount = insiderInfoService.count(new QueryWrapper<InsiderInfo>()
                .eq("is_account", 0)
                .eq(StringUtils.isNotBlank(gender), "gender", genderInt)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(idCard), "id_card", idCard)
                .like(StringUtils.isNotBlank(phone), "phone", phone)
                .like(StringUtils.isNotBlank(department), "department", department)
                .like(StringUtils.isNotBlank(rankNum), "rank_num", rankNum));

        List<InsiderInfoVo> resultList = new ArrayList<>();
        for (InsiderInfo item : dataList) {
            InsiderInfoVo data = new InsiderInfoVo(item);
            resultList.add(data);
        }

        PageListVo pageListVo = new PageListVo(resultList, pageNo, pageSize, totalCount);
        return Result.ok(pageListVo);
    }

    @RequestMapping("/insider/remove")
    public Result deleteInsider(@RequestBody RosterCondition rosterCondition) {
        List<Integer> ids = rosterCondition.getIds();
        insiderInfoService.removeByIds(ids);

        return Result.ok();
    }

    @RequestMapping("/insider/external/sign")
    public Result insiderSign(@RequestBody HikivisionBaseEvent hikivisionBaseEvent) {
        logger.info("收到海康传入的内部人员签到/签离信息：" + hikivisionBaseEvent.toString());
        List<EventInfo> eventInfos = hikivisionBaseEvent.getParams().getEvents();
        for (EventInfo item : eventInfos) {
            // 1. 获取签到/签离事件信息
            Integer eventType = item.getEventType();
            JSONObject dataJson = item.getData();
            String deviceIndexCode = item.getSrcIndex();
            String happenTime = item.getHappenTime();
            String personId = dataJson.getString("ExtEventPersonNo");
            InsideOutRecord insideOutRecord = new InsideOutRecord();

            // 2. 更新人员的在离营状态
            InsiderInfo insiderInfo = insiderInfoService.getOne(new QueryWrapper<InsiderInfo>().eq("index_code", personId));
            if (null != insiderInfo) {
                insideOutRecord.setPersonId(insiderInfo.getId());
                insideOutRecord.setName(insiderInfo.getName());
                insideOutRecord.setIdCard(insiderInfo.getIdCard());
                insideOutRecord.setImageUri(insiderInfo.getImageUri());
                insideOutRecord.setPersonType(0);
                // 如果人脸通过认证，则更新离营状态
                if (eventType.equals(HikivisionEventTypeEnum.FACE_PASS.getCode())) {
                    if (deviceIndexCode.equals("123")) {
                        insiderInfo.setIsOut(1);
                        insideOutRecord.setType(0);
                    }
                    if (deviceIndexCode.equals("456")) {
                        insiderInfo.setIsOut(0);
                        insideOutRecord.setType(1);
                    }
                    insiderInfoService.saveOrUpdate(insiderInfo);
                }
            }

            DependantInfo dependantInfo = dependantInfoService.getOne(new QueryWrapper<DependantInfo>().eq("index_code", personId));
            if (null != dependantInfo) {
                insideOutRecord.setPersonId(dependantInfo.getId());
                insideOutRecord.setName(dependantInfo.getName());
                insideOutRecord.setIdCard(dependantInfo.getIdCard());
                insideOutRecord.setImageUri(dependantInfo.getImageUri());
                insideOutRecord.setPersonType(1);
                // 如果人脸通过认证，则更新离营状态
                if (eventType.equals(HikivisionEventTypeEnum.FACE_PASS.getCode())) {
                    // 固定闸机为出营
                    if (deviceIndexCode.equals("123")) {
                        insiderInfo.setIsOut(1);
                        insideOutRecord.setType(0);
                    }
                    // 固定闸机为离营
                    if (deviceIndexCode.equals("456")) {
                        insiderInfo.setIsOut(0);
                        insideOutRecord.setType(1);
                    }
                    insiderInfoService.saveOrUpdate(insiderInfo);
                }
            }

            // 3. 保存内部人员出入营记录
            DeviceInfo deviceInfo = deviceInfoService.getOne(new QueryWrapper<DeviceInfo>()
                    .eq("indexCode", deviceIndexCode));
            insideOutRecord.setDeviceId(deviceInfo.getId());
            insideOutRecord.setDeviceName(deviceInfo.getName());
            insideOutRecord.setPositionInfo(deviceInfo.getPositionInfo());
            insideOutRecord.setTime(DateTimeUtil.stringToLocalDateTime(DateTimeUtil.iso8601ToString(happenTime)));
            insideOutRecordService.saveOrUpdate(insideOutRecord);
        }
        return Result.ok();
    }



    @RequestMapping("/dependant/save")
    public Result saveDependant(@RequestBody DependantInfoVo dependantInfoVo) {
        DependantInfo dependantInfo = new DependantInfo(dependantInfoVo);
        if (null == dependantInfo.getCreateTime()) {
            dependantInfo.setCreateTime(LocalDateTime.now());
        }
        dependantInfo.setUpdateTime(LocalDateTime.now());

        dependantInfoService.saveOrUpdate(dependantInfo);
        return Result.ok();
    }

    @RequestMapping("/dependant/list")
    public Result getDependantList(@RequestBody RosterCondition rosterCondition) {
        Integer pageNo = rosterCondition.getPageNo();
        Integer pageSize = rosterCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取家属列表信息失败，信息：传入的页数或页码为空");
        }
        Integer offset = (pageNo - 1) * pageSize;
        String name = rosterCondition.getName();
        String phone = rosterCondition.getPhone();
        String gender = rosterCondition.getGender();
        String relation = rosterCondition.getRelation();
        String institution = rosterCondition.getInstitution();
        Integer genderInt = 0;
        if (StringUtils.isNotBlank(gender)) {
            genderInt = GenderEnum.getDataByName(gender).getCode();
        }

        List<DependantInfo> dataList = dependantInfoService.list(new QueryWrapper<DependantInfo>()
                .eq(StringUtils.isNotBlank(gender), "gender", genderInt)
                .like(StringUtils.isNotBlank(phone), "phone", phone)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(relation), "relation", relation)
                .like(StringUtils.isNotBlank(institution), "institution", institution)
                .orderByDesc("id")
                .last("limit " + offset + ", " + pageSize));
        Integer totalCount = dependantInfoService.count(new QueryWrapper<DependantInfo>()
                .eq(StringUtils.isNotBlank(gender), "gender", genderInt)
                .like(StringUtils.isNotBlank(phone), "phone", phone)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(relation), "relation", relation)
                .like(StringUtils.isNotBlank(institution), "institution", institution));

        List<DependantInfoVo> resultList = new ArrayList<>();
        for (DependantInfo item : dataList) {
            DependantInfoVo data = new DependantInfoVo(item);
            resultList.add(data);
        }
        PageListVo pageListVo = new PageListVo(resultList, pageNo, pageSize, totalCount);

        return Result.ok(pageListVo);
    }

    @RequestMapping("/dependant/remove")
    public Result deleteDependant(@RequestBody RosterCondition rosterCondition) {
        List<Integer> ids = rosterCondition.getIds();
        dependantInfoService.removeByIds(ids);

        return Result.ok();
    }
}
