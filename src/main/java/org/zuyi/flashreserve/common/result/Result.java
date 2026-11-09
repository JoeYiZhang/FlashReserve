package org.zuyi.flashreserve.common.result;

import org.springframework.stereotype.Component;
import org.zuyi.flashreserve.common.constants.CodeConstants;
import lombok.Data;

@Component
@Data
public class Result<T> {

    // 状态码
    private Integer code;

    // 提示信息
    private String message;

    // 返回数据
    private T data;

    // 成功：不返回数据
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(CodeConstants.SUCCESS);
        result.setMessage("操作成功");
        return result;
    }

    // 成功：返回数据
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(CodeConstants.SUCCESS);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    // 失败：返回错误码和错误信息
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}