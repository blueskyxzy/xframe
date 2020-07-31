package com.frame.base.common.exception;

import com.frame.base.common.entity.BaseRespCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @desc BizException
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {

    final Integer code;
    final String msg;
    final boolean needLog;

    public BizException(String msg) {
        super(msg);
        this.msg = msg;
        this.code = BaseRespCode.BIZ;
        this.needLog = false;
    }

    public BizException(String msg, boolean needLog) {
        super(msg);
        this.msg = msg;
        this.code = BaseRespCode.BIZ;
        this.needLog = needLog;
    }

    public BizException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
        this.needLog = false;
    }

}
