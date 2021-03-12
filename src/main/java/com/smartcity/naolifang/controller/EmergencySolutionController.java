package com.smartcity.naolifang.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.entity.AttachmentInfo;
import com.smartcity.naolifang.entity.EmergencySolutionInfo;
import com.smartcity.naolifang.entity.searchCondition.EmergencySolutionCondition;
import com.smartcity.naolifang.entity.vo.EmergencySolutionInfoVo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.AttachmentInfoService;
import com.smartcity.naolifang.service.EmergencySolutionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/message")
public class EmergencySolutionController {

    @Autowired
    private EmergencySolutionInfoService emergencySolutionInfoService;

    @Autowired
    private AttachmentInfoService attachmentInfoService;

    /**
     * 保存应急预案信息
     * @param emergencySolutionInfoVo
     * @return
     */
    @RequestMapping("/emergency/save")
    public Result saveEmergencySolution(@RequestBody EmergencySolutionInfoVo emergencySolutionInfoVo) {
        Integer id = emergencySolutionInfoVo.getId();
        EmergencySolutionInfo emergencySolutionInfo = new EmergencySolutionInfo(emergencySolutionInfoVo);
        if (null == id) {
            emergencySolutionInfo.setCreateTime(LocalDateTime.now());
        }
        emergencySolutionInfo.setUpdateTime(LocalDateTime.now());

        emergencySolutionInfoService.saveOrUpdate(emergencySolutionInfo);

        return Result.ok();
    }

    /**
     * 获取应急预案信息列表
     * @param emergencySolutionCondition
     * @return
     */
    @RequestMapping("/emergency/list")
    public Result listEmergencySolution(@RequestBody EmergencySolutionCondition emergencySolutionCondition) {
        Integer pageNo = emergencySolutionCondition.getPageNo();
        Integer pageSize = emergencySolutionCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取应急预案信息列表失败，信息：传入的页码或数据条数为空");
        }
        Integer offset = (pageNo - 1) * pageSize;
        String title = emergencySolutionCondition.getTitle();

        List<EmergencySolutionInfo> dataList = emergencySolutionInfoService.list(new QueryWrapper<EmergencySolutionInfo>()
                .eq("is_delete", 0)
                .eq(StringUtils.isNotBlank(title), "title", title)
                .last("limit " + offset + ", " + pageSize));
        List<EmergencySolutionInfoVo> resultList = new ArrayList<>();
        for (EmergencySolutionInfo item : dataList) {
            EmergencySolutionInfoVo data = new EmergencySolutionInfoVo(item);

            // 获取应急预案中附件的详细信息
            String attachmentIds = item.getAttachmentIds();
            List<Integer> attachmentIdList = new ArrayList<>();
            String[] attachmentIdArr = attachmentIds.split(",");
            for (String id : attachmentIdArr) {
                attachmentIdList.add(Integer.valueOf(id));
            }
            if (!CollectionUtils.isEmpty(attachmentIdList)) {
                List<AttachmentInfo> attachmentInfos = attachmentInfoService.list(new QueryWrapper<AttachmentInfo>()
                        .in("id", attachmentIdList));
                data.setAttachmentInfos(attachmentInfos);
            }

            resultList.add(data);
        }
        return Result.ok(resultList);
    }

    /**
     * 删除应急预案信息
     * @param emergencySolutionCondition
     * @return
     */
    @RequestMapping("/emergency/remove")
    public Result deleteEmergencySolution(@RequestBody EmergencySolutionCondition emergencySolutionCondition) {
        List<Integer> ids = emergencySolutionCondition.getIds();

        List<EmergencySolutionInfo> dataList = emergencySolutionInfoService.listByIds(ids);
        for (EmergencySolutionInfo item : dataList) {
            item.setIsDelete(1);
        }

        emergencySolutionInfoService.saveOrUpdateBatch(dataList);

        return Result.ok();
    }
}
