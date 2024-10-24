package com.example.entity;

import java.util.Map;

/**
 * 结果封装类，用于统一返回结果的格式。
 */
public class Result<T> {
    // 成功状态码
    private static final int SUCCESS_CODE = 200;
    // 失败状态码
    private static final int FAILURE_CODE = 500;

    // 状态码
    private int code;
    // 消息
    private String message;
    // 数据
    private T data;

    /**
     * 默认构造方法
     */
    public Result() {}

    /**
     * 带状态码和消息的构造方法
     *
     * @param code 状态码
     * @param message 消息
     */
    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 带状态码、消息和数据的构造方法
     *
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     */
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 全参构造方法，包含状态码、消息、数据和附加信息
     *
     * @param code 状态码
     * @param message 消息
     * @param data 数据
     * @param additionalInfo 附加信息
     */
    public Result(int code, String message, T data, Map<String, Object> additionalInfo) {
        this.code = code;
        this.message = message;
        this.data = data;
        // 可以在这里处理additionalInfo
    }

    /**
     * 成功的静态工厂方法，返回一个成功的Result对象
     *
     * @param <T> 泛型类型
     * @return 成功的Result对象
     */
    public static <T> Result<T> success() {
        return new Result<>(SUCCESS_CODE, "成功");
    }

    /**
     * 成功的静态工厂方法，返回一个包含数据的成功Result对象
     *
     * @param data 数据
     * @param <T> 泛型类型
     * @return 成功的Result对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, "成功", data);
    }

    /**
     * 成功的静态工厂方法，返回一个包含数据和自定义消息的成功Result对象
     *
     * @param data 数据
     * @param message 自定义消息
     * @param <T> 泛型类型
     * @return 成功的Result对象
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(SUCCESS_CODE, message, data);
    }

    /**
     * 失败的静态工厂方法，返回一个失败的Result对象
     *
     * @param message 失败消息
     * @param <T> 泛型类型
     * @return 失败的Result对象
     */
    public static <T> Result<T> failure(String message) {
        return new Result<>(FAILURE_CODE, message, null);
    }

    /**
     * 失败的静态工厂方法，返回一个包含自定义状态码和消息的失败Result对象
     *
     * @param code 状态码
     * @param message 失败消息
     * @param <T> 泛型类型
     * @return 失败的Result对象
     */
    public static <T> Result<T> failure(int code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置状态码
     *
     * @param code 状态码
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取消息
     *
     * @return 消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置消息
     *
     * @param message 消息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取数据
     *
     * @return 数据
     */
    public T getData() {
        return data;
    }

    /**
     * 设置数据
     *
     * @param data 数据
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * 重写toString方法，用于打印对象信息
     *
     * @return 对象信息字符串
     */
    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + (data != null ? data.toString() : "null") +
                '}';
    }
}
