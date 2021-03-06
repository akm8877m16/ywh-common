package ywh.common.util.exception;

/**
 * add custom exception here
 */
public enum ExceptionEnum {
    UNKNOW_ERROR(-1,"未知错误"),
    USER_NOT_FIND(-101,"用户不存在"),
    DEVICE_NO_BIND(-102,"非所属用户设备"),

    REDIS_ERROR(-103,"redis error"),
    JSON_ERROR(-203,"json convert error"),
    ;

    private Integer code;

    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

