package com.smartcity.naolifang.controller;

import com.alibaba.fastjson.JSONObject;
import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.common.util.HttpUtil;
import com.smartcity.naolifang.entity.FaceInfo;
import com.smartcity.naolifang.entity.searchCondition.VideoCondition;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.FaceInfoService;
import com.smartcity.naolifang.service.VisitorInfoService;
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
    private VisitorInfoService visitorInfoService;

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

        String isoStartTime = DateTimeUtil.stringToIso8601(startTime);
        String isoEndTime = DateTimeUtil.stringToIso8601(endTime);

        byte[] byteData = image.getBytes();
        Base64.Encoder encoder = Base64.getEncoder();
        String base64Str = encoder.encodeToString(byteData);

        List<JSONObject> resultList = visitorInfoService.findTrackByPhoto(isoStartTime, isoEndTime, base64Str);

        return Result.ok(resultList);
    }
}
