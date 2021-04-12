package com.smartcity.naolifang.service;

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

    boolean addPersonToHikivisionPlatform(Integer id, Integer type);

    String getRootOrg();

    Result batchAddPermissionToHikivisionPlatform(List<String> indexCodes, List<String> deviceIndexCodes);

}
