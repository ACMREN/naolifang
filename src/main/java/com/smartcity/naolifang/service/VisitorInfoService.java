package com.smartcity.naolifang.service;

import com.alibaba.fastjson.JSONObject;
import com.smartcity.naolifang.entity.VisitorInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 访客信息表 服务类
 * </p>
 *
 * @author karl
 * @since 2021-03-18
 */
public interface VisitorInfoService extends IService<VisitorInfo> {

    String appointToHikivision(String visitStartTime, String visitEndTime, String visitorName, String phoneNo);

    List<JSONObject> findTrackByPhoto(String startTime, String endTime, String base64Str);
}
