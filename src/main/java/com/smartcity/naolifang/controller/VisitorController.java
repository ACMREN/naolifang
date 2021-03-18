package com.smartcity.naolifang.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.entity.SignInfo;
import com.smartcity.naolifang.entity.VisitorInfo;
import com.smartcity.naolifang.entity.searchCondition.VisitCondition;
import com.smartcity.naolifang.entity.vo.PageListVo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.entity.vo.VisitorInfoVo;
import com.smartcity.naolifang.service.SignInfoService;
import com.smartcity.naolifang.service.VisitorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/visitor")
public class VisitorController {

    @Autowired
    private VisitorInfoService visitorInfoService;

    @Autowired
    private SignInfoService signInfoService;

    @RequestMapping("/save")
    public Result saveVisitorInfo(@RequestBody VisitorInfoVo visitorInfoVo) {
        Integer id = visitorInfoVo.getId();
        VisitorInfo visitorInfo;
        if (null == id) {
            visitorInfo = new VisitorInfo(visitorInfoVo);
        } else {
            visitorInfo = visitorInfoService.getById(id);
        }
        visitorInfoService.saveOrUpdate(visitorInfo);

        SignInfo signInfo = new SignInfo();
        signInfo.setVisitorId(visitorInfo.getId());
        signInfoService.saveOrUpdate(signInfo);

        return Result.ok(visitorInfo.getId());
    }

    @RequestMapping("/list")
    public Result listVisitorInfo(@RequestBody VisitCondition visitCondition) {
        Integer pageNo = visitCondition.getPageNo();
        Integer pageSize  = visitCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.ok("获取访问信息列表失败，信息：传入的页码或数据条数错误");
        }
        Integer offset = (pageNo - 1) * pageSize;
        String name = visitCondition.getName();
        String idCard = visitCondition.getIdCard();
        String phone = visitCondition.getPhone();
        Integer status = visitCondition.getStatus();

        List<VisitorInfo> visitorInfos = visitorInfoService.list(new QueryWrapper<VisitorInfo>()
                .eq("is_delete",  0)
                .eq(null != status, "status", status)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(idCard), "id_card", idCard)
                .like(StringUtils.isNotBlank(phone), "phone", phone)
                .last("limit " + offset + ", " + pageSize));
        Integer totalCount = visitorInfoService.count(new QueryWrapper<VisitorInfo>()
                .eq("is_delete",  0)
                .eq(null != status, "status", status)
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(idCard), "id_card", idCard)
                .like(StringUtils.isNotBlank(phone), "phone", phone));

        List<VisitorInfoVo> resultList = new ArrayList<>();
        for (VisitorInfo item : visitorInfos) {
            VisitorInfoVo data = new VisitorInfoVo(item);
            resultList.add(data);
        }

        PageListVo pageListVo = new PageListVo(resultList, pageNo, pageSize, totalCount);
        return Result.ok(pageListVo);
    }

    @RequestMapping("/remove")
    public Result deleteVisitorInfo(@RequestBody VisitCondition visitCondition) {
        List<Integer> ids = visitCondition.getIds();

        List<VisitorInfo> visitorInfos = visitorInfoService.listByIds(ids);
        for (VisitorInfo item : visitorInfos) {
            item.setIsDelete(1);
        }
        visitorInfoService.saveOrUpdateBatch(visitorInfos);

        return Result.ok();
    }
}
