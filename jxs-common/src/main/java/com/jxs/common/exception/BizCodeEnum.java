package com.jxs.common.exception;

public enum BizCodeEnum {

    UNKNOWN_EXCEPTION(10000, "未知错误"),
    VALIDATE_EXCEPTION(10001, "参数格式验证错误");
    private int code;

    private String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
