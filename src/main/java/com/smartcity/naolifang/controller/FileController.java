package com.smartcity.naolifang.controller;

import com.smartcity.naolifang.entity.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    @RequestMapping("uploadImage")
    public Result uploadImage(@RequestParam MultipartFile image) {
        if (image.isEmpty()) {
            return Result.fail(500, "上传图片失败，信息：没有上传任何文件");
        }

        String uuid = UUID.randomUUID().toString();
        String fileName = image.getOriginalFilename();
        String baseName = fileName.split("\\.")[0];
        String suffix = fileName.split("\\.")[1];
        String newFileName = baseName + "-"+ uuid;
        String basePath = "/data/upload/image/avatar/";
        String imagePath = basePath + newFileName + "." + suffix;
        File imageFile = new File(imagePath);
        try {
            image.transferTo(imageFile);
            return Result.ok(newFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.fail(500, "上传图片失败，信息：保存文件时候出错");
    }
}
