package com.smartcity.naolifang.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.entity.DutyInfo;
import com.smartcity.naolifang.entity.InsiderInfo;
import com.smartcity.naolifang.entity.enumEntity.GenderEnum;
import com.smartcity.naolifang.entity.searchCondition.DutyCondition;
import com.smartcity.naolifang.entity.vo.DutyInfoVo;
import com.smartcity.naolifang.entity.vo.PageListVo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.DutyInfoService;
import com.smartcity.naolifang.service.InsiderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("onCallOfficier")
public class DutyController {

    @Autowired
    private DutyInfoService dutyInfoService;

    @Autowired
    private InsiderInfoService insiderInfoService;

    @RequestMapping("/save")
    public Result saveDuty(@RequestBody DutyInfoVo dutyInfoVo) {
        DutyInfo dutyInfo = new DutyInfo(dutyInfoVo);

        dutyInfoService.saveOrUpdate(dutyInfo);

        return Result.ok();
    }

    @RequestMapping("/list")
    public Result listDuty(@RequestBody DutyCondition dutyCondition) {
        Integer pageNo = dutyCondition.getPageNo();
        Integer pageSize = dutyCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取值班列表信息失败，信息：传入的页码和条数为空");
        }
        Integer offset = (pageNo - 1) * pageSize;
        String name = dutyCondition.getName();
        String gender = dutyCondition.getGender();
        String position = dutyCondition.getPosition();
        String startTime = dutyCondition.getStartTime();
        String endTime = dutyCondition.getEndTime();
        Integer genderInt = 0;
        if (StringUtils.isNotBlank(gender)) {
            genderInt = GenderEnum.getDataByName(gender).getCode();
        }

        List<DutyInfo> dutyInfos = dutyInfoService.list(new QueryWrapper<DutyInfo>()
                .eq("is_delete", 0)
                .eq(StringUtils.isNotBlank(gender), "gender", genderInt)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(position), "position", position)
                .gt(StringUtils.isNotBlank(startTime), "start_time", startTime)
                .lt(StringUtils.isNotBlank(endTime), "end_time", endTime)
                .last("limit " + offset + ", " + pageSize));
        Integer totalCount = dutyInfoService.count(new QueryWrapper<DutyInfo>()
                .eq("is_delete", 0)
                .eq(StringUtils.isNotBlank(gender), "gender", genderInt)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(position), "position", position)
                .gt(StringUtils.isNotBlank(startTime), "start_time", startTime)
                .lt(StringUtils.isNotBlank(endTime), "end_time", endTime)
                .last("limit " + offset + ", " + pageSize));

        List<DutyInfoVo> resultList = new ArrayList<>();
        for (DutyInfo item : dutyInfos) {
            DutyInfoVo data = new DutyInfoVo(item);
            Integer insiderId = item.getInsiderId();
            InsiderInfo insiderInfo = insiderInfoService.getById(insiderId);
            data.setImageUri(insiderInfo.getImageUri());
            resultList.add(data);
        }

        PageListVo pageListVo = new PageListVo(resultList, pageNo, pageSize, totalCount);

        return Result.ok(pageListVo);
    }

    @RequestMapping("/remove")
    public Result deleteDuty(@RequestBody DutyCondition dutyCondition) {
        List<Integer> ids = dutyCondition.getIds();

        List<DutyInfo> dutyInfos = dutyInfoService.listByIds(ids);
        for (DutyInfo item : dutyInfos) {
            item.setIsDelete(1);
        }
        dutyInfoService.saveOrUpdateBatch(dutyInfos);

        return Result.ok();
    }
}
