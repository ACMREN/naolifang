package com.smartcity.naolifang.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smartcity.naolifang.entity.MessagePollingInfo;
import com.smartcity.naolifang.entity.searchCondition.MessageCondition;
import com.smartcity.naolifang.entity.vo.Result;
import com.smartcity.naolifang.service.MessagePollingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessagePollingInfoService messagePollingInfoService;

    private static Integer pollingInterval = 0;

    /**
     * 保存轮询消息
     * @param messagePollingInfo
     * @return
     */
    @RequestMapping("/polling/save")
    public Result savePollingInfo(@RequestBody MessagePollingInfo messagePollingInfo) {
        Integer id = messagePollingInfo.getId();
        if (null == id) {
            messagePollingInfo.setCreateTime(LocalDateTime.now());
        }
        messagePollingInfo.setUpdateTime(LocalDateTime.now());

        messagePollingInfoService.saveOrUpdate(messagePollingInfo);

        return Result.ok();
    }

    /**
     * 获取轮询消息列表
     * @param messageCondition
     * @return
     */
    @RequestMapping("/polling/list")
    public Result listPollingInfo(@RequestBody MessageCondition messageCondition) {
        Integer pageNo = messageCondition.getPageNo();
        Integer pageSize = messageCondition.getPageSize();
        if (null == pageNo || null == pageSize) {
            return Result.fail(500, "获取消息轮询信息失败，信息：传入的页码或数据条数为空");
        }
        Integer offset = (pageNo - 1) * pageSize;
        List<MessagePollingInfo> resultList = messagePollingInfoService.list(new QueryWrapper<MessagePollingInfo>()
                .last("limit " + offset + ", " + pageSize));

        return Result.ok(resultList);
    }

    /**
     * 删除轮询消息
     * @param messageCondition
     * @return
     */
    @RequestMapping("/polling/remove")
    public Result deletePollingInfo(@RequestBody MessageCondition messageCondition) {
        List<Integer> ids = messageCondition.getIds();

        List<MessagePollingInfo> dataList = messagePollingInfoService.listByIds(ids);
        for (MessagePollingInfo item : dataList) {
            item.setIsDelete(1);
        }
        messagePollingInfoService.saveOrUpdateBatch(dataList);

        return Result.ok();
    }

    /**
     * 获取消息轮询间隔
     * @return
     */
    @RequestMapping("/polling/getInterval")
    public Result  getPollingInterval() {
        File file = null;
        if (pollingInterval.intValue() == 0) {
            try {
                file = ResourceUtils.getFile("classpath:application-dev.properties");
                FileInputStream inputStream = new FileInputStream(file);
                Properties properties = new Properties();
                properties.load(inputStream);

                pollingInterval = Integer.valueOf(properties.getProperty("pollingInterval"));

                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Result.ok(pollingInterval);
    }

    /**
     * 修改消息轮询间隔
     * @param messageCondition
     * @return
     */
    @RequestMapping("/polling/changeInterval")
    public Result changePollingInterval(@RequestBody MessageCondition messageCondition) {
        Integer newInterval = messageCondition.getNewInterval();

        File file = null;
        pollingInterval = newInterval;
        try {
            file = ResourceUtils.getFile("classpath:application-dev.properties");
            FileInputStream inputStream = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(inputStream);

            properties.setProperty("pollingInterval", newInterval.toString());

            FileWriter fileWriter = new FileWriter(file);
            properties.store(fileWriter, null);

            fileWriter.flush();
            fileWriter.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.ok();
    }
}
