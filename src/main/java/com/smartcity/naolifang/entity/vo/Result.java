package com.smartcity.naolifang.entity.vo;

import lombok.Data;

@Data
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result ok() {
        return new Result(200, "成功", null);
    }

    public static Result ok(Object data) {
        return new Result(200, "成功", data);
    }

    public static Result fail(Integer code, String msg) {
        return new Result(code, msg, null);
    }

    public static Result build(Integer code, String msg, Object data) {
        return new Result(code, msg, data);
    }
}
