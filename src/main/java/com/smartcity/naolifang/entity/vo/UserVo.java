package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.entity.InsiderInfo;
import com.smartcity.naolifang.entity.Permission;
import com.smartcity.naolifang.entity.Role;
import com.smartcity.naolifang.entity.User;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserVo {
    private Integer id;
    private String name;
    private String username;
    private String idCard;
    private String groupName;
    private String position;
    private String superior;
    private String phone;
    private String password;
    private Integer isEnable;
    private Integer registerId;
    private String remark;
    private String creteTime;
    private String updateTime;

    private List<Integer> roleIds = new ArrayList<>();
    private List<Role>  roles = new ArrayList<>();
    private List<Permission> permissions = new ArrayList<>();

    public UserVo() {
    }

    public UserVo(User user) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.id = user.getId();
        this.username = user.getUsername();
        this.registerId = user.getRegisterId();
        this.remark = user.getRemark();
        this.isEnable = user.getIsEnable();
        this.creteTime = dtf.format(user.getCreateTime());
        this.updateTime = dtf.format(user.getUpdateTime());
    }

    public UserVo packageDetailInfo(InsiderInfo insiderInfo) {
        this.name = insiderInfo.getName();
        this.idCard = insiderInfo.getIdCard();
        this.groupName = insiderInfo.getGroupName();
        this.position = insiderInfo.getPosition();
        this.superior = insiderInfo.getSuperior();
        this.phone = insiderInfo.getPhone();

        return this;
    }
}
