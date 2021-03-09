package com.smartcity.naolifang.service.impl;

import com.smartcity.naolifang.entity.MessagePollingInfo;
import com.smartcity.naolifang.mapper.MessagePollingInfoMapper;
import com.smartcity.naolifang.service.MessagePollingInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息轮询信息表 服务实现类
 * </p>
 *
 * @author karl
 * @since 2021-03-09
 */
@Service
public class MessagePollingInfoServiceImpl extends ServiceImpl<MessagePollingInfoMapper, MessagePollingInfo> implements MessagePollingInfoService {

}
