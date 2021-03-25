package com.smartcity.naolifang.controller;

import com.smartcity.naolifang.entity.FaceInfo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.FaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private FaceInfoService faceInfoService;

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

    @RequestMapping("/photo/search")
    public Result searchEventByPhoto() {


        return Result.ok();
    }
}
