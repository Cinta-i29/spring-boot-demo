package com.howe.constant;

import lombok.AllArgsConstructor;

/**
 * 常量响应状态码
 * @author howe
 */
@AllArgsConstructor
public enum ResponseResultCode {
    /**
     * 成功
     */
    SUCCESS(200, "success"),
    /**
     * 请求处理成功，但没有任何资源可以返回给客户端
     */
    No_Content(204, "no content"),
    /**
     * 请求错误
     */
    BAD_REQUEST(400, "bad request"),
    /**
     * 请求未授权
     */
    UNAUTHORIZED(401, "unauthorized"),
    /**
     * 权限不足
     */
    FORBIDDEN(403, "forbidden"),
    /**
     * 资源未找到
     */
    NOT_FOUND(404, "not found"),
    /**
     * 服务器内部错误
     */
    ERROR(500, "error");
    /**
     * 状态码
     */
    public final int code;
    /**
     * 状态码描述
     */
    public final String msg;

    @Override
    public String toString() {
        return this.msg;
    }
}
