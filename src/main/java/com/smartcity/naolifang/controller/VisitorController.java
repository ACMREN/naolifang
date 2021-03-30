package com.smartcity.naolifang.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.common.util.HttpUtil;
import com.smartcity.naolifang.entity.FaceInfo;
import com.smartcity.naolifang.entity.SignInfo;
import com.smartcity.naolifang.entity.VisitorInfo;
import com.smartcity.naolifang.entity.enumEntity.HikivisionEventTypeEnum;
import com.smartcity.naolifang.entity.enumEntity.VisitStatusEnum;
import com.smartcity.naolifang.entity.external.EventInfo;
import com.smartcity.naolifang.entity.external.HikivisionBaseEvent;
import com.smartcity.naolifang.entity.searchCondition.VisitCondition;
import com.smartcity.naolifang.entity.vo.PageListVo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.entity.vo.SignInfoVo;
import com.smartcity.naolifang.entity.vo.VisitorInfoVo;
import com.smartcity.naolifang.service.FaceInfoService;
import com.smartcity.naolifang.service.SignInfoService;
import com.smartcity.naolifang.service.VisitorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/visitor")
public class VisitorController {

    @Autowired
    private VisitorInfoService visitorInfoService;

    @Autowired
    private SignInfoService signInfoService;

    @Autowired
    private FaceInfoService faceInfoService;

    @Autowired
    private Config config;

    @RequestMapping("/save")
    public Result saveVisitorInfo(@RequestBody VisitorInfoVo visitorInfoVo) {
        Integer id = visitorInfoVo.getId();
        VisitorInfo visitorInfo;
        if (null == id) {
            visitorInfo = new VisitorInfo(visitorInfoVo);
        } else {
            visitorInfo = visitorInfoService.getById(id);
        }
        // 调用海康的预约接口
//        String visitStartTime = DateTimeUtil.stringToIso8601(visitorInfoVo.getVisitStartTime());
//        String visitEndTime = DateTimeUtil.stringToIso8601(visitorInfoVo.getVisitEndTime());
//        String orderId = visitorInfoService.appointToHikivision(visitStartTime, visitEndTime, visitorInfoVo.getName(), visitorInfoVo.getPhone());
//        if (StringUtils.isBlank(orderId)) {
//            return Result.fail(500, "保存预约信息失败，信息：调用海康预约接口失败");
//        }
//        visitorInfo.setOrderId(orderId);

        visitorInfoService.saveOrUpdate(visitorInfo);

        SignInfo signInfo;
        signInfo = signInfoService.getOne(new QueryWrapper<SignInfo>().eq("visitor_id", visitorInfo.getId()));
        if (null == signInfo) {
            signInfo.setVisitorId(visitorInfo.getId());
            signInfoService.saveOrUpdate(signInfo);
        }

        return Result.ok(visitorInfo.getId());
    }

    @RequestMapping("/list")
    public Result listVisitorInfo(@RequestBody VisitCondition visitCondition) {
        Integer pageNo = visitCondition.getPageNo();
        Integer pageSize  = visitCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.ok("获取访问信息列表失败，信息：传入的页码或数据条数错误");
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
        SignInfo signInfo = signInfoService.getOne(new QueryWrapper<SignInfo>().eq("visitor_id", visitorId));

        signInfo.updateSignInfo(signInfoVo);
        signInfoService.saveOrUpdate(signInfo);

        return Result.ok();
    }

    /**
     * 提供给海康访客机进行签到签离的接口
     * @param hikivisionBaseEvent
     * @return
     */
    @RequestMapping("/external/sign")
    public Result updateVisitStatus(@RequestBody HikivisionBaseEvent hikivisionBaseEvent) {
        List<EventInfo> eventInfos = hikivisionBaseEvent.getParams().getEvents();
        for (EventInfo item : eventInfos) {
            Integer eventType = item.getEventType();
            JSONObject dataJson = item.getData();
            String visitorId  = dataJson.getString("visitorId");
            String endTime = dataJson.getString("endTime");
            String photoUri = dataJson.getString("photoUrl");

            VisitorInfo visitorInfo = visitorInfoService.getOne(new QueryWrapper<VisitorInfo>().eq("order_id", visitorId));
            if (null != visitorInfo) {
                // 如果是来宾卡通过的话，则修改到访状态为已签入
                if (eventType.equals(HikivisionEventTypeEnum.GUEST_CARD_PASS.getCode())) {
                    visitorInfo.setStatus(VisitStatusEnum.SIGN_IN.getCode());
                    visitorInfoService.saveOrUpdate(visitorInfo);
                }
                if (eventType.equals(HikivisionEventTypeEnum.VISITOR_SIGN_OUT.getCode())) {
                    visitorInfo.setStatus(VisitStatusEnum.SIGN_OUT.getCode());
                    visitorInfo.setLeaveTime(DateTimeUtil.stringToLocalDateTime(DateTimeUtil.iso8601ToString(endTime)));
                    visitorInfoService.saveOrUpdate(visitorInfo);
                }
            }

            // 如果包含有图片资源的话，则保存到人脸图库

        }

        return Result.ok();
    }
}
