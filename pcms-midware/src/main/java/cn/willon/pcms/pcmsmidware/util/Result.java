package cn.willon.pcms.pcmsmidware.util;

import lombok.Data;

import java.io.Serializable;

/**
 * Result
 *
 * @author Willon
 * @since 2019-04-06
 */
@Data
public class Result implements Serializable {
    public static final int OK = 200;
    public static final int INTERNAL_ERROR = 500;
    public static final String SUCCESS = "ok";
    private Integer code;
    private String message;
    private Object data;

    public static Result successResult(Object data) {
        Result result = new Result();
        result.setCode(OK);
        result.setMessage(SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result failResult(Throwable e) {
        Result result = new Result();
        result.setCode(INTERNAL_ERROR);
        result.setMessage(e.getMessage());
        result.setData(null);
        return result;
    }

    public static Result failResult(String message) {
        Result result = new Result();
        result.setCode(INTERNAL_ERROR);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}
