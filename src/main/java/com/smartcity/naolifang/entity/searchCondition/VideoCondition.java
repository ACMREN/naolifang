package com.smartcity.naolifang.entity.searchCondition;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
public class VideoCondition {
    private String startTime;
    private String endTime;
    private MultipartFile image;
}
