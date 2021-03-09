package com.smartcity.naolifang.controller;

import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.entity.AttachmentInfo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.AttachmentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private Config config;

    @Autowired
    private AttachmentInfoService attachmentInfoService;

    @RequestMapping("/test")
    public Result test() {
        return Result.ok(config.getPollingInterval());
    }

    /**
     * 上传图片
     * @param image
     * @param type
     * @return
     */
    @RequestMapping("/upload/image")
    public Result uploadImage(@RequestParam MultipartFile image, String type) {
        if (image.isEmpty()) {
            return Result.fail(500, "上传图片失败，信息：没有上传任何文件");
        }

        String uuid = UUID.randomUUID().toString();
        String docPath = "";
        String mappingPath = "";
        if (type.equals("avatar")) {
            String fileName = image.getOriginalFilename();
            String baseName = fileName.split("\\.")[0];
            String suffix = fileName.split("\\.")[1];
            String newFileName = baseName + "-"+ uuid + "." + suffix;
            docPath = config.getAvatarDocPath() + newFileName;
            mappingPath = config.getAvatarMappingPath() + newFileName;
        }

        File imageFile = new File(docPath);
        try {
            image.transferTo(imageFile);
            return Result.ok(mappingPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.fail(500, "上传图片失败，信息：保存文件时候出错");
    }

    /**
     * 上传文件
     * @param file
     * @param type
     * @return
     */
    @RequestMapping("/upload/file")
    public Result uploadFile(@RequestParam MultipartFile file, String type) {
        if (file.isEmpty()) {
            return Result.fail(500, "上传文件失败，信息：没有上传任何文件");
        }

        String uuid = UUID.randomUUID().toString();
        String docPath = "";
        String mappingPath = "";

        String fileName = file.getOriginalFilename();
        String baseName = fileName.split("\\.")[0];
        String suffix = fileName.split("\\.")[1];
        String newFileName = baseName + "-"+ uuid + "." + suffix;
        if (type.equals("video")) {
            docPath = config.getVideoDocPath() + newFileName;
            mappingPath = config.getVideoMappingPath() + newFileName;
        } else if (type.equals("word")) {
            docPath = config.getWordDocPath() + newFileName;
            mappingPath = config.getWordMappingPath() + newFileName;
        } else if (type.equals("excel")) {
            docPath = config.getExcelDocPath() + newFileName;
            mappingPath = config.getExcelMappingPath() + newFileName;
        } else if (type.equals("ppt")) {
            docPath = config.getPptDocPath() + newFileName;
            mappingPath = config.getPptMappingPath() + newFileName;
        } else if (type.equals("pdf")) {
            docPath = config.getPdfDocPath() + newFileName;
            mappingPath = config.getPdfMappingPath() + newFileName;
        } else {
            return Result.fail(500, "上传附件失败，信息：不支持的附件类型");
        }

        AttachmentInfo attachmentInfo = new AttachmentInfo();
        attachmentInfo.setOriginalName(fileName);
        attachmentInfo.setEncodeName(newFileName);
        attachmentInfo.setMappingPath(mappingPath);
        attachmentInfo.setCreateTime(LocalDateTime.now());
        attachmentInfoService.saveOrUpdate(attachmentInfo);

        File dest = new File(docPath);
        try {
            file.transferTo(dest);
            return Result.ok(attachmentInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.fail(500, "上传文件失败，信息：保存文件时候出错");
    }
}
