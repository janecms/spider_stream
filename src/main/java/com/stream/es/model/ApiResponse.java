package com.stream.es.model;

import lombok.Data;

@Data
public class ApiResponse<T> {
    T data;
    int code;
    String msg;
    long count;
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.code = 1;
        response.msg = "success";
        response.data=data;
        return response;
    }

    public ApiResponse setCount(long count){
        this.count=count;
        return this;
    }


}
