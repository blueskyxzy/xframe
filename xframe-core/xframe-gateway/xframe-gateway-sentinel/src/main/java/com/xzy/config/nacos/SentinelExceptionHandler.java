package com.xzy.config.nacos;

import cn.hutool.http.HttpStatus;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.frame.base.common.entity.BaseResp;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 */
@RestControllerAdvice
public class SentinelExceptionHandler {

    @ExceptionHandler(FlowException.class)
    public BaseResp<String> handleException(FlowException e) {
        return BaseResp.error(HttpStatus.HTTP_UNAVAILABLE, "该接口已经被限流啦");
    }

    @ExceptionHandler(ParamFlowException.class)
    public BaseResp<String> handleException(ParamFlowException e) {
        return BaseResp.error(HttpStatus.HTTP_UNAVAILABLE, "您当前访问的频次过高，请稍后重试");
    }

}
