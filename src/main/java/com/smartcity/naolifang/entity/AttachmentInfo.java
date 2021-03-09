package com.smartcity.naolifang.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 上传附件信息表
 * </p>
 *
 * @author karl
 * @since 2021-03-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AttachmentInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 附件id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 附件原名
     */
    private String originalName;

    /**
     * 加密名字
     */
    private String encodeName;

    /**
     * 映射路径
     */
    private String mappingPath;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否已经删除：0-否，1-是
     */
    private Integer isDelete;

    /**
     * 上传时间
     */
    private LocalDateTime createTime;


}
