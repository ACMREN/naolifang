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

    @Value("${fileDocPath}")
    private String fileDocPath;
    @Value("${fileMappingPath}")
    private String fileMappingPath;
}
