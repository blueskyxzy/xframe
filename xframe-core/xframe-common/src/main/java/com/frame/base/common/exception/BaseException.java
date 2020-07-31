package com.frame.base.common.exception;

import cn.hutool.http.HttpStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @desc BaseException
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    final Integer code;
    final String msg;

    public BaseException(String msg) {
        super(msg);
        this.msg = msg;
        this.code = HttpStatus.HTTP_INTERNAL_ERROR;
    }

    public BaseException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = HttpStatus.HTTP_INTERNAL_ERROR;
    }

    public BaseException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public BaseException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

}
