package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.entity.AttachmentInfo;
import com.smartcity.naolifang.entity.EmergencySolutionInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmergencySolutionInfoVo {
    private Integer id;
    private String title;
    private String description;
    private String solution;
    private List<AttachmentInfo> attachmentInfos = new ArrayList<>();

    private List<Integer> attachmentIds = new ArrayList<>();

    public EmergencySolutionInfoVo() {
    }

    public EmergencySolutionInfoVo(EmergencySolutionInfo emergencySolutionInfo) {
        this.id = emergencySolutionInfo.getId();
        this.title = emergencySolutionInfo.getTitle();
        this.description = emergencySolutionInfo.getDescription();
        this.solution = emergencySolutionInfo.getSolution();
    }
}
