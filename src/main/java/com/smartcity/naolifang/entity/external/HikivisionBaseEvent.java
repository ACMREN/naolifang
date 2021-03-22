package com.smartcity.naolifang.entity.external;

import lombok.Data;

@Data
public class HikivisionBaseEvent {
    private String method;
    private ParamInfo params;
}
