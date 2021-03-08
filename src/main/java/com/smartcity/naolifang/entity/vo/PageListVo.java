package com.smartcity.naolifang.entity.vo;

import lombok.Data;

import java.util.Collection;

@Data
public class PageListVo<T> {
    private Collection<T> dataList;
    // 页码
    private Integer pageNo;
    // 每页数量
    private Integer pageSize;
    // 数据总数
    private Integer total;
    // 分页数
    private Integer pageNum;

    public PageListVo(Collection<T> dataList, Integer pageNo, Integer pageSize, Integer total) {
        this.dataList = dataList;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        if (null != pageSize && null != total) {
            if (pageSize.intValue() != 0) {
                this.pageNum = total / pageSize;
            }
        }
    }

    public PageListVo() {

    }
}