package com.yjj.accountbook.entity.dto;

import java.util.List;

public class PageResult<T>{
    private long total;
    private List<T> list;
public PageResult(long total,List<T> list){
    this.total=total;
    this.list=list;
}

    public List<T> getList() {
        return list;
    }

    public long getTotal() {
        return total;
    }
}
