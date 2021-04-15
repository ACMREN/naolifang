package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.MessagePollingInfo;
import lombok.Data;

@Data
public class MessagePollingInfoVo {
    private Integer id;
    private String title;
    private String content;
    private Integer isPolling;
    private String createTime;
    private String updateTime;

    public MessagePollingInfoVo() {
    }

    public MessagePollingInfoVo(MessagePollingInfo messagePollingInfo) {
        this.id = messagePollingInfo.getId();
        this.title = messagePollingInfo.getTitle();
        this.content = messagePollingInfo.getContent();
        this.isPolling = messagePollingInfo.getIsPolling();
        this.createTime = DateTimeUtil.localDateTimeToString(messagePollingInfo.getCreateTime());
        this.updateTime = DateTimeUtil.localDateTimeToString(messagePollingInfo.getUpdateTime());
    }
}
