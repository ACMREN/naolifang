package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.smartcity.naolifang.common.util.DateTimeUtil;
import com.smartcity.naolifang.entity.enumEntity.DeviceTypeEnum;
import com.smartcity.naolifang.entity.enumEntity.DoorStatusEnum;
import com.smartcity.naolifang.entity.enumEntity.RegionEnum;
import com.smartcity.naolifang.entity.enumEntity.StatusEnum;
import com.smartcity.naolifang.entity.vo.DeviceInfoVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 设备信息表
 * </p>
 *
 * @author karl
 * @since 2021-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 设备名字
     */
    private String name;

    /**
     * 设备类型
     */
    private Integer type;

    /**
     * 设备编号
     */
    private String indexCode;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 厂商
     */
    private String manufacturer;

    /**
     * 位置
     */
    private String position;

    /**
     * 区域
     */
    private Integer region;

    /**
     * 维护人员
     */
    private String maintainPerson;

    /**
     * 状态：0-离线，1-在线，2-已禁止，3-未激活
     */
    private Integer status;

    /**
     * 门禁开关状态：0-正常开关，1-常关，2-常闭
     */
    private Integer doorStatus;

    /**
     * 上线时间
     */
    private LocalDateTime createTime;

    /**
     * 生命周期
     */
    private Long liveTime;

    /**
     * 是否已经删除：0-否，1-是
     */
    private Integer isDelete;


    public DeviceInfo() {
    }

    public DeviceInfo(DeviceInfoVo deviceInfoVo) {
        this.id = deviceInfoVo.getId();
        this.name = deviceInfoVo.getName();
        this.type = DeviceTypeEnum.getDataByName(deviceInfoVo.getType()).getCode();
        this.indexCode = deviceInfoVo.getIndexCode();
        this.ip = deviceInfoVo.getIp();
        this.manufacturer = deviceInfoVo.getManufacturer();
        this.position = deviceInfoVo.getPosition();
        this.region = RegionEnum.getDataByName(deviceInfoVo.getRegion()).getCode();
        this.maintainPerson = deviceInfoVo.getMaintainPerson();
        this.status = StatusEnum.getDataByName(deviceInfoVo.getStatus()).getCode();
        this.doorStatus = DoorStatusEnum.getDataByName(deviceInfoVo.getDoorStatus()).getCode();
        if (StringUtils.isNotBlank(deviceInfoVo.getCreateTime())) {
            this.createTime = DateTimeUtil.stringToLocalDateTime(deviceInfoVo.getCreateTime());
        }
    }
}
