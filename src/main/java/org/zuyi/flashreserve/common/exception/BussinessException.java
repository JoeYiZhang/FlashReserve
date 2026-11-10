package org.zuyi.flashreserve.common.exception;

import lombok.Getter;

@Getter
public class BussinessException extends RuntimeException {
    Integer code;
    public BussinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
