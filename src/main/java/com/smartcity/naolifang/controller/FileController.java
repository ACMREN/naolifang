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
        String fileName = image.getName();
        String newFileName = fileName + "-"+ uuid;
        String basePath = "/data/upload/image/avatar";
        File imagePath = new File(basePath + newFileName);
        try {
            image.transferTo(imagePath);
            return Result.ok(newFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.fail(500, "上传图片失败，信息：保存文件时候出错");
    }
}
