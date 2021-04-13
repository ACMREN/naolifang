package com.smartcity.naolifang.entity;

import lombok.Data;

import java.util.List;

@Data
public class CameraPollingInfoBo {
    private Integer userId;
    private List<Integer> cameraIds;
}
