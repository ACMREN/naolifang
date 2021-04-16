package com.smartcity.naolifang.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
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

    @Value("${imageDocPath}")
    private String imageDocPath;
    @Value("${imageMappingPath}")
    private String imageMappingPath;

    @Value("${tmpDocPath}")
    private String tmpDocPath;
    @Value("${tmpMappingPath}")
    private String tmpMappingPath;

    @Value("${hikivision.platform.url}")
    private String hikivisionPlatformUrl;

    @Value("${hikivision.doorControl}")
    private String hikivisionDoorControlUrl;
    @Value("${hikivision.deviceSearch}")
    private String hikivisionDeviceSearchUrl;
    @Value("${hikivision.doorStatus}")
    private String hikivisionDoorStatusUrl;
    @Value("${hikivision.cameraStatus}")
    private String hikivisionCameraStatusUrl;
    @Value("${hikivision.encodeDeviceStatus}")
    private String hikivisionEncodeDeviceStatusUrl;
    @Value("${hikivision.readerStatus}")
    private String hikivisionReaderStatusUrl;
    @Value("${hikivision.appointment}")
    private String hikivisionAppointmentUrl;
    @Value("${hikivision.playback}")
    private String hikivisionPlaybackUrl;
    @Value("${hikivision.pictureDownload}")
    private String hikivisionPictureDownUrl;
    @Value("${hikivision.photoSearch}")
    private String hikivisionPhotoSearchUrl;
    @Value("${hikivision.getResource}")
    private String hikivisionGetResourceUrl;
    @Value("${hikivision.addPerson}")
    private String hikivisionAddPersonUrl;
    @Value("${hikivision.deletePerson}")
    private String hikivisionDeletePersonUrl;
    @Value("${hikivision.getRootOrg}")
    private String hikivisionRootOrgUrl;
    @Value("${hikivision.addPermission}")
    private String hikivisionAddPermissionUrl;
    @Value("${hikivision.permissionConfig}")
    private String hikivisionPermissionCfgUrl;
    @Value("${hikivision.downloadTask}")
    private String hikivisionDownloadTaskUrl;
    @Value("${hikivision.checkTask}")
    private String hikivisionCheckTaskUrl;

    @Value("${pollingInterval}")
    private String pollingInterval;
}
