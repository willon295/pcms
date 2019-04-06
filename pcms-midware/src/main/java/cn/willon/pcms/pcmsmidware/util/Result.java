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
    private Integer code;
    private String message;
    private Object data;
    public static Result successResult(Object data) {
        Result result = new Result();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    public static Result failResult(Throwable e) {
        Result result = new Result();
        result.setCode(500);
        result.setMessage(e.getMessage());
        result.setData(null);
        return result;
    }
}
