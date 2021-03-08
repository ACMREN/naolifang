package com.smartcity.naolifang.controller;

import com.smartcity.naolifang.entity.DutyInfo;
import com.smartcity.naolifang.entity.vo.DutyInfoVo;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.DutyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("duty")
public class DutyController {

    @Autowired
    private DutyInfoService dutyInfoService;

    @RequestMapping("/duty/save")
    public Result saveDuty(@RequestBody DutyInfoVo dutyInfoVo) {
        DutyInfo dutyInfo = new DutyInfo(dutyInfoVo);

        dutyInfoService.save(dutyInfo);

        return Result.ok();
    }
}
