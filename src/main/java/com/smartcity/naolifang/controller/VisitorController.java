package com.smartcity.naolifang.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.common.util.HttpUtil;
import com.smartcity.naolifang.common.util.SystemUtil;
import com.smartcity.naolifang.entity.*;
import com.smartcity.naolifang.entity.enumEntity.HikivisionEventTypeEnum;
import com.smartcity.naolifang.entity.enumEntity.VisitStatusEnum;
import com.smartcity.naolifang.entity.external.EventInfo;
import com.smartcity.naolifang.entity.external.HikivisionBaseEvent;
import com.smartcity.naolifang.entity.searchCondition.VisitCondition;
import com.smartcity.naolifang.entity.vo.PageListVo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.entity.vo.SignInfoVo;
import com.smartcity.naolifang.entity.vo.VisitorInfoVo;
import com.smartcity.naolifang.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/visitor")
public class VisitorController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VisitorInfoService visitorInfoService;

    @Autowired
    private SignInfoService signInfoService;

    @Autowired
    private FaceInfoService faceInfoService;

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Autowired
    private InsideOutRecordService insideOutRecordService;

    @Autowired
    private Config config;

    @RequestMapping("/save")
    public Result saveVisitorInfo(@RequestBody VisitorInfoVo visitorInfoVo) {
        Integer id = visitorInfoVo.getId();
        VisitorInfo visitorInfo;
        visitorInfo = new VisitorInfo(visitorInfoVo);
        // ???????????????????????????????????????????????????
        if (null == id) {
            String visitStartTime = DateTimeUtil.stringToIso8601(visitorInfoVo.getVisitStartTime());
            String visitEndTime = DateTimeUtil.stringToIso8601(visitorInfoVo.getVisitEndTime());
            String orderId = visitorInfoService.appointToHikivision(visitStartTime, visitEndTime, visitorInfoVo.getName(), visitorInfoVo.getPhone());
            if (StringUtils.isBlank(orderId)) {
                return Result.fail(500, "??????????????????????????????????????????????????????????????????");
            }
            visitorInfo.setOrderId(orderId);
        }

        visitorInfoService.saveOrUpdate(visitorInfo);

        SignInfo signInfo;
        signInfo = signInfoService.getOne(new QueryWrapper<SignInfo>().eq("visitor_id", visitorInfo.getId()));
        if (null == signInfo) {
            signInfo = new SignInfo();
            signInfo.setVisitorId(visitorInfo.getId());
            signInfoService.saveOrUpdate(signInfo);
        }

        return Result.ok(visitorInfo.getId());
    }

    /**
     * ????????????
     * @param visitCondition
     * @return
     */
    @RequestMapping("/reject")
    public Result rejectSign(@RequestBody VisitCondition visitCondition) {
        Integer id = visitCondition.getId();
        VisitorInfo visitorInfo = visitorInfoService.getById(id);
        visitorInfo.setStatus(VisitStatusEnum.REJECT.getCode());
        visitorInfoService.saveOrUpdate(visitorInfo);

        return Result.ok();
    }

    @RequestMapping("/list")
    public Result listVisitorInfo(@RequestBody VisitCondition visitCondition) {
        Integer pageNo = visitCondition.getPageNo();
        Integer pageSize  = visitCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.ok("??????????????????????????????????????????????????????????????????????????????");
        }
        Integer offset = (pageNo - 1) * pageSize;
        String name = visitCondition.getName();
        String idCard = visitCondition.getIdCard();
        String phone = visitCondition.getPhone();
        Integer status = visitCondition.getStatus();

        List<VisitorInfo> visitorInfos = visitorInfoService.list(new QueryWrapper<VisitorInfo>()
                .eq("is_delete",  0)
                .eq(null != status, "status", status)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(idCard), "id_card", idCard)
                .like(StringUtils.isNotBlank(phone), "phone", phone)
                .orderByDesc("id")
                .last("limit " + offset + ", " + pageSize));
        Integer totalCount = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("is_delete",  0)
                .eq(null != status, "status", status)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(idCard), "id_card", idCard)
                .like(StringUtils.isNotBlank(phone), "phone", phone));

        List<VisitorInfoVo> resultList = new ArrayList<>();
        for (VisitorInfo item : visitorInfos) {
            VisitorInfoVo data = new VisitorInfoVo(item);
            resultList.add(data);
        }

        PageListVo pageListVo = new PageListVo(resultList, pageNo, pageSize, totalCount);
        return Result.ok(pageListVo);
    }

    @RequestMapping("/remove")
    public Result deleteVisitorInfo(@RequestBody VisitCondition visitCondition) {
        List<Integer> ids = visitCondition.getIds();

        List<VisitorInfo> visitorInfos = visitorInfoService.listByIds(ids);
        for (VisitorInfo item : visitorInfos) {
            item.setIsDelete(1);
        }
        visitorInfoService.saveOrUpdateBatch(visitorInfos);

        return Result.ok();
    }

    @RequestMapping("/sign")
    public Result getSignInfo(@RequestBody SignInfoVo signInfoVo) {
        Integer visitorId = signInfoVo.getVisitorId();
        String temperatureStr = signInfoVo.getTemperature();
        if (StringUtils.isNotBlank(temperatureStr) && !this.isNumber(temperatureStr)) {
            return Result.fail(400, "??????????????????????????????????????????????????????");
        }
        SignInfo signInfo = signInfoService.getOne(new QueryWrapper<SignInfo>().eq("visitor_id", visitorId));

        signInfo.updateSignInfo(signInfoVo);
        signInfoService.saveOrUpdate(signInfo);

        return Result.ok();
    }

    private boolean isNumber(String number) {
        try {
            Double temp = Double.parseDouble(number);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    @RequestMapping("/track/find")
    public Result findTrack(@RequestBody VisitCondition visitCondition) {
        String startTime = visitCondition.getStartTime();
        String endTime = visitCondition.getEndTime();
        Integer visitorId = visitCondition.getVisitorId();

        String isoStartTime = DateTimeUtil.stringToIso8601(startTime);
        String isoEndTime = DateTimeUtil.stringToIso8601(endTime);

        VisitorInfo visitorInfo = visitorInfoService.getById(visitorId);
        String imageUri = visitorInfo.getImageUri();

        try {
            byte[] data = HttpUtil.downImageToByte("http://" + SystemUtil.getLocalAddress() + ":2020" + imageUri);
            Base64.Encoder encoder = Base64.getEncoder();
            String base64Str = encoder.encodeToString(data);

            List<JSONObject> resultList = visitorInfoService.findTrackByPhoto(isoStartTime, isoEndTime, base64Str);

            return Result.ok(resultList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.ok(new ArrayList<>());
    }

    /**
     * ???????????????????????????????????????????????????
     * @param hikivisionBaseEvent
     * @return
     */
    @RequestMapping("/external/sign")
    public Result updateVisitStatus(@RequestBody HikivisionBaseEvent hikivisionBaseEvent) {
        logger.info("?????????????????????????????????/???????????????" + hikivisionBaseEvent.toString());
        List<EventInfo> eventInfos = hikivisionBaseEvent.getParams().getEvents();
        for (EventInfo item : eventInfos) {
            Integer eventType = item.getEventType();
            JSONObject dataJson = item.getData();
            String deviceIndexCode = item.getSrcIndex();
            String happenTime = item.getHappenTime();
            String visitorId = dataJson.getString("ExtEventCardNo");
            String endTime = dataJson.getString("endTime");
            String photoUri = dataJson.getString("ExtEventPictureURL");
            InsideOutRecord insideOutRecord = new InsideOutRecord();

            // ????????????????????????????????????????????????????????????
            String photoUrl = faceInfoService.saveFaceInfo(photoUri);

            VisitorInfo visitorInfo = visitorInfoService.getOne(new QueryWrapper<VisitorInfo>().eq("order_id", visitorId));
            if (null != visitorInfo) {
                // ??????????????????????????????????????????????????????????????????
                if (eventType.equals(HikivisionEventTypeEnum.GUEST_CARD_PASS.getCode())) {
                    visitorInfo.setStatus(VisitStatusEnum.SIGN_IN.getCode());
                    visitorInfo.setImageUri(photoUrl);
                    visitorInfoService.saveOrUpdate(visitorInfo);
                }
                if (eventType.equals(HikivisionEventTypeEnum.VISITOR_SIGN_OUT.getCode())) {
                    visitorInfo.setStatus(VisitStatusEnum.SIGN_OUT.getCode());
                    visitorInfo.setLeaveTime(DateTimeUtil.stringToLocalDateTime(DateTimeUtil.iso8601ToString(endTime)));
                    visitorInfoService.saveOrUpdate(visitorInfo);
                }
            }

            // 3. ???????????????????????????
            DeviceInfo deviceInfo = deviceInfoService.getOne(new QueryWrapper<DeviceInfo>()
                    .eq("indexCode", deviceIndexCode));
            insideOutRecord.setPersonId(-1);
            insideOutRecord.setName(visitorInfo.getName());
            insideOutRecord.setIdCard(visitorInfo.getIdCard());
            insideOutRecord.setImageUri(visitorInfo.getImageUri());
            insideOutRecord.setPersonType(2);
            insideOutRecord.setDeviceId(deviceInfo.getId());
            insideOutRecord.setDeviceName(deviceInfo.getName());
            insideOutRecord.setPositionInfo(deviceInfo.getPositionInfo());
            insideOutRecord.setTime(DateTimeUtil.stringToLocalDateTime(DateTimeUtil.iso8601ToString(happenTime)));
            insideOutRecordService.saveOrUpdate(insideOutRecord);
        }
        return Result.ok();
    }
}
