package com.smartcity.naolifang.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.bean.Config;
import com.smartcity.naolifang.entity.FaceInfo;
import com.smartcity.naolifang.mapper.FaceInfoMapper;
import com.smartcity.naolifang.service.FaceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 人脸图库信息表 服务实现类
 * </p>
 *
 * @author karl
 * @since 2021-03-25
 */
@Service
public class FaceInfoServiceImpl extends ServiceImpl<FaceInfoMapper, FaceInfo> implements FaceInfoService {

    @Autowired
    private Config config;

    @Override
    public String saveFaceInfo(String photoUri) {
        String photoUrl = "";
        if (StringUtils.isNotBlank(photoUri)) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("url", photoUri);

            try {
                URL url = new URL(config.getHikivisionPictureDownUrl());
                URLConnection connection = url.openConnection();
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                PrintWriter out;
                JSONObject param = new JSONObject(paramMap);
                out = new PrintWriter(connection.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();

                photoUrl = connection.getHeaderField("Location");
                // 第一期先不处理人脸图库的逻辑
//                FaceInfo faceInfo = new FaceInfo();
//                faceInfo.setPhotoUrl(photoUrl);
//                this.saveOrUpdate(faceInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return photoUrl;
    }
}
