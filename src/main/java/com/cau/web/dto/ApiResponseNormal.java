package com.cau.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiResponseNormal<T> {
    @JsonProperty("code")
    private int code;

    @JsonProperty("data")
    private T data;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("token")
    private String token;

    // 无参构造函数
    public ApiResponseNormal() {}

    // 带参构造函数
    public ApiResponseNormal(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    // Getters 和 Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
