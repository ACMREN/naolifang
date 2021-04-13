package com.smartcity.naolifang.config.scheduleJob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.VisitorInfo;
import com.smartcity.naolifang.entity.enumEntity.VisitStatusEnum;
import com.smartcity.naolifang.service.VisitorInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class CheckVisitTimeoutJob {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VisitorInfoService visitorInfoService;

    /**
     * 检查访问超时的记录
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void checkVisitTimeout() {
        logger.info("=========开始检测访问状态==========");
        String begin = DateTimeUtil.localDateToString(LocalDate.now()) + " 00:00:00";
        String end = DateTimeUtil.localDateToString(LocalDate.now()) + " 23:59:59";

        // 1. 找出所有当天所有在访的记录
        List<VisitorInfo> dataList = visitorInfoService.list(new QueryWrapper<VisitorInfo>()
                .eq("status", VisitStatusEnum.SIGN_IN.getCode())
                .gt("visit_start_time", begin)
                .lt("visit_start_time", end));

        // 2. 判断如果超时的记录则更新到访状态为超时
        LocalDateTime now = LocalDateTime.now();
        for (VisitorInfo item : dataList) {
            LocalDateTime visitEndTime = item.getVisitEndTime();
            Duration duration = Duration.between(visitEndTime, now);
            if (duration.isNegative()) {
                item.setStatus(VisitStatusEnum.TIME_OUT.getCode());
            }
        }

        visitorInfoService.saveOrUpdateBatch(dataList);
        logger.info("=========结束检测访问状态==========");
    }
}
