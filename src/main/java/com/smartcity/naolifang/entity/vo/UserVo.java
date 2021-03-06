package com.smartcity.naolifang.entity.vo;

import com.smartcity.naolifang.entity.InsiderInfo;
import com.smartcity.naolifang.entity.Permission;
import com.smartcity.naolifang.entity.Role;
import com.smartcity.naolifang.entity.User;
import com.smartcity.naolifang.entity.enumEntity.GenderEnum;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserVo {
    private Integer id;
    private String name;
    private String gender;
    private String username;
    private String rankNum;
    private String idCard;
    private String department;
    private String nickName;
    private String rankName;
    private String address;
    private String phone;
    private String password;
    private Integer isEnable;
    private Integer registerId;
    private String remark;
    private String createTime;
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
        this.createTime = dtf.format(user.getCreateTime());
        this.updateTime = dtf.format(user.getUpdateTime());
    }

    public UserVo packageDetailInfo(InsiderInfo insiderInfo) {
        this.name = insiderInfo.getName();
        this.gender = GenderEnum.getDataByCode(insiderInfo.getGender()).getName();
        this.rankNum = insiderInfo.getRankNum();
        this.idCard = insiderInfo.getIdCard();
        this.department = insiderInfo.getDepartment();
        this.nickName = insiderInfo.getNickName();
        this.rankName = insiderInfo.getRankName();
        this.address = insiderInfo.getAddress();
        this.phone = insiderInfo.getPhone();

        return this;
    }
}
