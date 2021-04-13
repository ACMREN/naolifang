package com.smartcity.naolifang.service;

import com.smartcity.naolifang.entity.FaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 人脸图库信息表 服务类
 * </p>
 *
 * @author karl
 * @since 2021-03-25
 */
public interface FaceInfoService extends IService<FaceInfo> {

    String saveFaceInfo(String photoUri);
}
