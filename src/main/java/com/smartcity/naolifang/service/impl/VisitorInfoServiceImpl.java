package com.smartcity.naolifang.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.common.util.HttpUtil;
import com.smartcity.naolifang.entity.VisitorInfo;
import com.smartcity.naolifang.entity.vo.HikivisionVisitorInfo;
import com.smartcity.naolifang.mapper.VisitorInfoMapper;
import com.smartcity.naolifang.service.VisitorInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * <p>
 * 访客信息表 服务实现类
 * </p>
 *
 * @author karl
 * @since 2021-03-18
 */
@Service
public class VisitorInfoServiceImpl extends ServiceImpl<VisitorInfoMapper, VisitorInfo> implements VisitorInfoService {

    @Autowired
    private Config config;

    @Override
    public String appointToHikivision(String visitStartTime, String visitEndTime, String visitorName, String phoneNo) {
        String receptionistId = UUID.randomUUID().toString();
        String appointmentUrl = config.getHikivisionAppointmentUrl();
        HikivisionVisitorInfo visitorInfo = new HikivisionVisitorInfo();
        visitorInfo.setGender(1);
        visitorInfo.setVisitorName(visitorName);
        visitorInfo.setPhoneNo(phoneNo);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("receptionistId", receptionistId);
        paramMap.put("visitStartTime", visitStartTime);
        paramMap.put("visitEndTime", visitEndTime);
        paramMap.put("visitorInfo", visitorInfo);

        String resultStr = HttpUtil.postToHikvisionPlatform(appointmentUrl, paramMap);
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        String resultCode = resultJson.getString("code");
        if (resultCode.equals("0")) {
            String orderId = resultJson.getJSONObject("data").getString("orderId");
            return orderId;
        }

        return null;
    }

    @Override
    public List<JSONObject> findTrackByPhoto(String startTime, String endTime, String base64Str) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        paramMap.put("facePicBinaryData", base64Str);
        paramMap.put("minSimilarity", 80);
        String resultStr = HttpUtil.postToHikvisionPlatform(config.getHikivisionPhotoSearchUrl(), paramMap);

        JSONObject resultJson = JSONObject.parseObject(resultStr);
        List<JSONObject> dataList = JSONObject.parseArray(resultJson.getString("list"), JSONObject.class);
        List<JSONObject> resultList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dataList)) {
            for (JSONObject item : dataList) {
                JSONObject data = new JSONObject();
                String indexCode = item.getString("cameraIndexCode");
                String captureTime = item.getString("captureTime");
                String bkgPicUrl = item.getString("bkgPicUrl");
                String happenTime = DateTimeUtil.iso8601ToString(captureTime);
                data.put("indexCode", indexCode);
                data.put("happenTime", happenTime);
                data.put("bkgPicUrl", bkgPicUrl);
                resultList.add(data);
            }
        }

        return resultList;
    }
}
