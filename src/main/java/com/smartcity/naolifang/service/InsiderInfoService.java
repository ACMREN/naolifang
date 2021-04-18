package com.smartcity.naolifang.service;

import com.smartcity.naolifang.entity.DoorPermissionInfo;
import com.smartcity.naolifang.entity.InsiderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.smartcity.naolifang.entity.vo.Result;

import java.util.List;

/**
 * <p>
 * 营区内部人员表 服务类
 * </p>
 *
 * @author karl
 * @since 2021-03-04
 */
public interface InsiderInfoService extends IService<InsiderInfo> {

    /**
     * 获取已经上传到海康平台的人员名单
     * @return
     */
    List<String> getPersonFromHikivisionPlatform();

    /**
     * 上传人员名单到海康平台
     * @param id
     * @param type
     * @return
     */
    String addPersonToHikivisionPlatform(Integer id, Integer type);

    String getRootOrg();

    /**
     * 批量添加人员门禁权限到海康平台
     * @param indexCodes
     * @param deviceIndexCodes
     * @param configType
     * @return
     */
    Result batchConfigPermissionToHikivisionPlatform(List<String> indexCodes, List<String> deviceIndexCodes, Integer configType);

    void configPermissionFromHikivisionPlatform(DoorPermissionInfo doorPermissionInfo, String indexCode, String deviceIndexCode, Integer operationType);
}
