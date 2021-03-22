package com.smartcity.naolifang.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.common.util.HttpUtil;
import com.smartcity.naolifang.entity.VisitorInfo;
import com.smartcity.naolifang.entity.vo.HikivisionVisitorInfo;
import com.smartcity.naolifang.mapper.VisitorInfoMapper;
import com.smartcity.naolifang.service.VisitorInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

        String resultStr = HttpUtil.doPost(appointmentUrl, paramMap);
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        int resultCode = resultJson.getInteger("code");
        if (resultCode == 0) {
            String orderId = resultJson.getJSONObject("data").getString("orderId");
            return orderId;
        }

        return null;
    }
}
