package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.smartcity.naolifang.entity.vo.EmergencySolutionInfoVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 应急预案信息表
 * </p>
 *
 * @author karl
 * @since 2021-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EmergencySolutionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 概述
     */
    private String description;

    /**
     * 处理办法
     */
    private String solution;

    /**
     * 附件id
     */
    private String attachmentIds;

    /**
     * 是否已经删除：0-否，1-是
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    public EmergencySolutionInfo() {
    }

    public EmergencySolutionInfo(EmergencySolutionInfoVo emergencySolutionInfoVo) {
        this.id = emergencySolutionInfoVo.getId();
        this.title = emergencySolutionInfoVo.getTitle();
        this.description = emergencySolutionInfoVo.getDescription();
        this.solution = emergencySolutionInfoVo.getSolution();
        List<Integer> attachmentIdList = emergencySolutionInfoVo.getAttachmentIds();
        StringBuilder sb = new StringBuilder();
        for (Integer item : attachmentIdList) {
            sb.append(item).append(",");
        }
        if (sb.length() != 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        this.attachmentIds = sb.toString();
    }
}
