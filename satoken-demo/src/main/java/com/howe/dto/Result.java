package com.howe.dto;

import com.howe.constant.ResponseResultCode;
import lombok.*;

/**
 * @author howe
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class Result<T> {
    //响应状态码
    private Integer code;
    //响应消息
    private String msg;
    //响应数据
    private T data;

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(ResponseResultCode.SUCCESS.code)
                .data(data)
                .build();
    }

    public static <T> Result<T> success(String msg) {
        return Result.<T>builder()
                .code(ResponseResultCode.SUCCESS.code)
                .msg(msg)
                .build();
    }

    public static <T> Result<T> success(T data,String msg) {
        return Result.<T>builder()
                .code(ResponseResultCode.SUCCESS.code)
                .msg(msg)
                .data(data)
                .build();
    }

    /**
     * 204 请求处理成功但是数据为空
     */
    public static <T> Result<T> noContent() {
        return Result.<T>builder()
                .code(ResponseResultCode.No_Content.code)
                .msg(ResponseResultCode.No_Content.msg)
                .build();
    }

    public static <T> Result<T> fail() {
        return Result.<T>builder()
                .code(ResponseResultCode.ERROR.code)
                .msg("fail")
                .build();
    }

    public static <T> Result<T> fail(Throwable e) {
        return Result.<T>builder()
                .code(ResponseResultCode.ERROR.code)
                .msg(e.getMessage())
                .build();
    }

    public static <T> Result<T> fail(String msg) {
        return Result.<T>builder()
                .code(ResponseResultCode.ERROR.code)
                .msg(msg)
                .build();
    }

    public static <T> Result<T> unauthorized(String msg) {
        return Result.<T>builder()
                .code(ResponseResultCode.UNAUTHORIZED.code)
                .msg(msg)
                .build();
    }

    public static <T> Result<T> badRequest(String msg) {
        return Result.<T>builder()
                .code(ResponseResultCode.BAD_REQUEST.code)
                .msg(msg)
                .build();
    }

    public static <T> Result<T> notFound(String msg) {
        return Result.<T>builder()
                .code(ResponseResultCode.NOT_FOUND.code)
                .msg(msg)
                .build();
    }

    public static <T> Result<T> forbidden(String msg) {
        return Result.<T>builder()
                .code(ResponseResultCode.FORBIDDEN.code)
                .msg(msg)
                .build();
    }


}
