package com.smartcity.naolifang.entity.external;

import lombok.Data;

import java.util.List;

@Data
public class ParamInfo {
    private String sendTime;
    private String ability;
    private List<String> uids;
    private List<String> clients;
    private List<EventInfo> events;
}
