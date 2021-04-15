package com.smartcity.naolifang.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.common.util.HttpUtil;
import com.smartcity.naolifang.common.util.SystemUtil;
import com.smartcity.naolifang.entity.DependantInfo;
import com.smartcity.naolifang.entity.FaceInfo;
import com.smartcity.naolifang.entity.InsiderInfo;
import com.smartcity.naolifang.entity.searchCondition.FileCondition;
import com.smartcity.naolifang.entity.searchCondition.RosterCondition;
import com.smartcity.naolifang.entity.searchCondition.VideoCondition;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.DependantInfoService;
import com.smartcity.naolifang.service.FaceInfoService;
import com.smartcity.naolifang.service.InsiderInfoService;
import com.smartcity.naolifang.service.VisitorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    private InsiderInfoService insiderInfoService;

    @Autowired
    private DependantInfoService dependantInfoService;

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
     * @return
     * @throws IOException
     */
    @RequestMapping("/photo/search")
    public Result searchEventByPhoto(@RequestBody FileCondition fileCondition) throws IOException {
        String startTime = fileCondition.getStartTime();
        String endTime = fileCondition.getEndTime();
        String imageUrl = fileCondition.getImageUrl();

        String isoStartTime = DateTimeUtil.stringToIso8601(startTime);
        String isoEndTime = DateTimeUtil.stringToIso8601(endTime);

        byte[] byteData = HttpUtil.downImageToByte("http://" + SystemUtil.getLocalAddress() + ":2020" + imageUrl);
        Base64.Encoder encoder = Base64.getEncoder();
        String base64Str = encoder.encodeToString(byteData);

        List<JSONObject> resultList = visitorInfoService.findTrackByPhoto(isoStartTime, isoEndTime, base64Str);

        return Result.ok(resultList);
    }

    /**
     * 根据关键字搜索花名册人员
     * @param rosterCondition
     * @return
     */
    @RequestMapping("/search")
    public Result searchRoster(@RequestBody RosterCondition rosterCondition) {
        String name = rosterCondition.getName();
        String idCard = rosterCondition.getIdCard();
        List<JSONObject> resultList = new ArrayList<>();
        if (StringUtils.isNotBlank(name)) {
            List<InsiderInfo> insiderInfos = insiderInfoService.list(new QueryWrapper<InsiderInfo>()
                    .like("name", name));
            List<DependantInfo> dependantInfos = dependantInfoService.list(new QueryWrapper<DependantInfo>()
                    .like("name", name));
            return packingResult(resultList, insiderInfos, dependantInfos);
        }
        if (StringUtils.isNotBlank(idCard)) {
            List<InsiderInfo> insiderInfos = insiderInfoService.list(new QueryWrapper<InsiderInfo>()
                    .like("idCard", idCard));
            List<DependantInfo> dependantInfos = dependantInfoService.list(new QueryWrapper<DependantInfo>()
                    .like("idCard", idCard));
            return packingResult(resultList, insiderInfos, dependantInfos);
        }
        return Result.ok(new ArrayList<>());
    }

    /**
     * 组装结果数据
     * @param resultList
     * @param insiderInfos
     * @param dependantInfos
     * @return
     */
    private Result packingResult(List<JSONObject> resultList, List<InsiderInfo> insiderInfos, List<DependantInfo> dependantInfos) {
        for (InsiderInfo item : insiderInfos) {
            JSONObject data = new JSONObject();
            data.put("name", item.getName());
            data.put("idCard", item.getIdCard());
            data.put("imageUri", item.getImageUri());
            resultList.add(data);
        }
        for (DependantInfo item : dependantInfos) {
            JSONObject data = new JSONObject();
            data.put("name", item.getName());
            data.put("idCard", item.getIdCard());
            data.put("imageUri", item.getImageUri());
            resultList.add(data);
        }
        return Result.ok(resultList);
    }
}
