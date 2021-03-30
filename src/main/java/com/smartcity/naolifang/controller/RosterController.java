package com.smartcity.naolifang.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.entity.DependantInfo;
import com.smartcity.naolifang.entity.InsiderInfo;
import com.smartcity.naolifang.entity.enumEntity.GenderEnum;
import com.smartcity.naolifang.entity.searchCondition.RosterCondition;
import com.smartcity.naolifang.entity.searchCondition.UserCondition;
import com.smartcity.naolifang.entity.vo.DependantInfoVo;
import com.smartcity.naolifang.entity.vo.InsiderInfoVo;
import com.smartcity.naolifang.entity.vo.PageListVo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.DependantInfoService;
import com.smartcity.naolifang.service.InsiderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/roster")
public class RosterController {

    @Autowired
    private InsiderInfoService insiderInfoService;

    @Autowired
    private DependantInfoService dependantInfoService;

    /**
     * 新增/更新内部人员
     * @param insiderInfoVo
     * @return
     */
    @RequestMapping("/insider/save")
    public Result saveInsider(@RequestBody InsiderInfoVo insiderInfoVo) {
        InsiderInfo insiderInfo = new InsiderInfo(insiderInfoVo);
        if (null == insiderInfo.getCreateTime()) {
            insiderInfo.setCreateTime(LocalDateTime.now());
        }
        insiderInfo.setUpdateTime(LocalDateTime.now());

        insiderInfoService.saveOrUpdate(insiderInfo);

        return Result.ok(insiderInfo.getId());
    }

    /**
     * 获取内部人员列表
     * @param rosterCondition
     * @return
     */
    @RequestMapping("/insider/list")
    public Result getInsiderList(@RequestBody RosterCondition rosterCondition) {
        Integer pageNo = rosterCondition.getPageNo();
        Integer pageSize = rosterCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取营内人员信息失败，信息：传入的页数和页码为空");
        }
        String name = rosterCondition.getName();
        String department = rosterCondition.getDepartment();
        String phone = rosterCondition.getPhone();
        String idCard = rosterCondition.getIdCard();
        String rankNum = rosterCondition.getRankNum();
        String gender = rosterCondition.getGender();
        Integer genderInt = 0;
        if (StringUtils.isNotBlank(gender)) {
            genderInt = GenderEnum.getDataByName(gender).getCode();
        }
        Integer offset = (pageNo - 1) * pageSize;

        List<InsiderInfo> dataList = insiderInfoService.list(new QueryWrapper<InsiderInfo>()
                .eq("is_account", 0)
                .eq(StringUtils.isNotBlank(gender), "gender", genderInt)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(idCard), "id_card", idCard)
                .like(StringUtils.isNotBlank(phone), "phone", phone)
                .like(StringUtils.isNotBlank(department), "department", department)
                .like(StringUtils.isNotBlank(rankNum), "rank_num", rankNum)
                .last("limit " + offset + ", " + pageSize));
        Integer totalCount = insiderInfoService.count(new QueryWrapper<InsiderInfo>()
                .eq("is_account", 0)
                .eq(StringUtils.isNotBlank(gender), "gender", genderInt)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(idCard), "id_card", idCard)
                .like(StringUtils.isNotBlank(phone), "phone", phone)
                .like(StringUtils.isNotBlank(department), "department", department)
                .like(StringUtils.isNotBlank(rankNum), "rank_num", rankNum));

        List<InsiderInfoVo> resultList = new ArrayList<>();
        for (InsiderInfo item : dataList) {
            InsiderInfoVo data = new InsiderInfoVo(item);
            resultList.add(data);
        }

        PageListVo pageListVo = new PageListVo(resultList, pageNo, pageSize, totalCount);
        return Result.ok(pageListVo);
    }

    @RequestMapping("/insider/remove")
    public Result deleteInsider(@RequestBody RosterCondition rosterCondition) {
        List<Integer> ids = rosterCondition.getIds();
        insiderInfoService.removeByIds(ids);

        return Result.ok();
    }

    @RequestMapping("/dependant/save")
    public Result saveDependant(@RequestBody DependantInfoVo dependantInfoVo) {
        DependantInfo dependantInfo = new DependantInfo(dependantInfoVo);
        if (null == dependantInfo.getCreateTime()) {
            dependantInfo.setCreateTime(LocalDateTime.now());
        }
        dependantInfo.setUpdateTime(LocalDateTime.now());

        dependantInfoService.saveOrUpdate(dependantInfo);
        return Result.ok();
    }

    @RequestMapping("/dependant/list")
    public Result getDependantList(@RequestBody RosterCondition rosterCondition) {
        Integer pageNo = rosterCondition.getPageNo();
        Integer pageSize = rosterCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取家属列表信息失败，信息：传入的页数或页码为空");
        }
        Integer offset = (pageNo - 1) * pageSize;
        String name = rosterCondition.getName();
        String phone = rosterCondition.getPhone();
        String gender = rosterCondition.getGender();
        String relation = rosterCondition.getRelation();
        String institution = rosterCondition.getInstitution();
        Integer genderInt = 0;
        if (StringUtils.isNotBlank(gender)) {
            genderInt = GenderEnum.getDataByName(gender).getCode();
        }

        List<DependantInfo> dataList = dependantInfoService.list(new QueryWrapper<DependantInfo>()
                .eq(StringUtils.isNotBlank(gender), "gender", genderInt)
                .like(StringUtils.isNotBlank(phone), "phone", phone)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(relation), "relation", relation)
                .like(StringUtils.isNotBlank(institution), "institution", institution)
                .last("limit " + offset + ", " + pageSize));
        Integer totalCount = dependantInfoService.count(new QueryWrapper<DependantInfo>()
                .eq(StringUtils.isNotBlank(gender), "gender", genderInt)
                .like(StringUtils.isNotBlank(phone), "phone", phone)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(relation), "relation", relation)
                .like(StringUtils.isNotBlank(institution), "institution", institution));

        List<DependantInfoVo> resultList = new ArrayList<>();
        for (DependantInfo item : dataList) {
            DependantInfoVo data = new DependantInfoVo(item);
            resultList.add(data);
        }
        PageListVo pageListVo = new PageListVo(resultList, pageNo, pageSize, totalCount);

        return Result.ok(pageListVo);
    }

    @RequestMapping("/dependant/remove")
    public Result deleteDependant(@RequestBody RosterCondition rosterCondition) {
        List<Integer> ids = rosterCondition.getIds();
        dependantInfoService.removeByIds(ids);

        return Result.ok();
    }
}
