package com.yjj.accountbook.common;

public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public static <T>Result<T> ok(T data){
        Result<T>r=new Result<>();
        r.code=200;
        r.msg="ok";
        r.data=data;
        return r;
    }

    public static <T>Result<T> fail(int code, String msg){
        Result<T>r=new Result<>();
        r.code=code;
        r.msg=msg;
        return r;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

}
