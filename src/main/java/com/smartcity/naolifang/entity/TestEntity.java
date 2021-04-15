package com.smartcity.naolifang.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class TestEntity {
    private String url;
    private JSONObject paramJson;
}
