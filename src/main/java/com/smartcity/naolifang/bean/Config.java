package com.smartcity.naolifang.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:application-dev.properties")
public class Config {
    @Value("${avatarDocPath}")
    private String avatarDocPath;
    @Value("${avatarMappingPath}")
    private String avatarMappingPath;

    @Value("${videoDocPath}")
    private String videoDocPath;
    @Value("${videoMappingPath}")
    private String videoMappingPath;

    @Value("${wordDocPath}")
    private String wordDocPath;
    @Value("${wordMappingPath}")
    private String wordMappingPath;

    @Value("${excelDocPath}")
    private String excelDocPath;
    @Value("${excelMappingPath}")
    private String excelMappingPath;

    @Value("${pptDocPath}")
    private String pptDocPath;
    @Value("${pptMappingPath}")
    private String pptMappingPath;

    @Value("${pdfDocPath}")
    private String pdfDocPath;
    @Value("${pdfMappingPath}")
    private String pdfMappingPath;

    @Value("${pollingInterval}")
    private String pollingInterval;
}
