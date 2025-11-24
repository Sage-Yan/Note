package com.common;

import lombok.Data;

@Data
public class AjaxResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> AjaxResult<T> success(T data) {
        AjaxResult<T> result = success();
        result.setData(data);
        return result;
    }

    public static <T> AjaxResult<T> success() {
        AjaxResult<T> result = new AjaxResult<>();
        result.setCode(200);
        result.setMsg("success");
        return result;
    }

    public static <T> AjaxResult<T> success(Integer code, T data, String msg) {
        AjaxResult<T> result = new AjaxResult<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> AjaxResult<T> error(String msg) {
        AjaxResult<T> result = new AjaxResult<>();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }
}
