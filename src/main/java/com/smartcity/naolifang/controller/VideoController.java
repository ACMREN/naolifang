package com.smartcity.naolifang.controller;

import com.alibaba.fastjson.JSONObject;
import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.common.util.HttpUtil;
import com.smartcity.naolifang.entity.FaceInfo;
import com.smartcity.naolifang.entity.searchCondition.VideoCondition;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.FaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private FaceInfoService faceInfoService;

    @Autowired
    private Config config;

    @RequestMapping("/photo/save")
    public Result savePhoto(FaceInfo faceInfo) {
        Integer id = faceInfo.getId();
        FaceInfo data;
        if (null == id) {
            data = new FaceInfo();
        } else {
            data = faceInfoService.getById(id);
        }
        data.setPhotoUrl(faceInfo.getPhotoUrl());
        data.setName(faceInfo.getName());
        data.setIdCard(faceInfo.getIdCard());
        faceInfoService.saveOrUpdate(data);

        return Result.ok();
    }

    /**
     * 查询人员轨迹
     * @param videoCondition
     * @return
     * @throws IOException
     */
    @RequestMapping("/photo/search")
    public Result searchEventByPhoto(@RequestBody VideoCondition videoCondition) throws IOException {
        String startTime = videoCondition.getStartTime();
        String endTime = videoCondition.getEndTime();
        MultipartFile image = videoCondition.getImage();

        byte[] byteData = image.getBytes();
        Base64.Encoder encoder = Base64.getEncoder();
        String base64Str = encoder.encodeToString(byteData);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        paramMap.put("facePicBinaryData", base64Str);
        paramMap.put("minSimilarity", 50);
        String resultStr = HttpUtil.doPost(config.getHikivisionPlatformUrl() + config.getHikivisionPhotoSearchUrl(), paramMap);

        JSONObject resultJson = JSONObject.parseObject(resultStr);
        List<JSONObject> dataList = JSONObject.parseArray(resultJson.getString("list"), JSONObject.class);
        List<JSONObject> resultList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dataList)) {
            for (JSONObject item : dataList) {
                JSONObject data = new JSONObject();
                String indexCode = item.getString("cameraIndexCode");
                String captureTime = item.getString("captureTime");
                String happenTime = DateTimeUtil.iso8601ToString(captureTime);
                data.put("indexCode", indexCode);
                data.put("happenTime", happenTime);
                resultList.add(data);
            }
        }

        return Result.ok(resultList);
    }
}
