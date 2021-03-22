package com.smartcity.naolifang.entity.external;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class EventInfo {
    private String eventId;
    private String srcIndex;
    private String srcType;
    private String srcName;
    private Integer eventType;
    private Integer status;
    private Integer timeout;
    private String happenTime;
    private String srcParentIndex;
    private JSONObject data;
}
