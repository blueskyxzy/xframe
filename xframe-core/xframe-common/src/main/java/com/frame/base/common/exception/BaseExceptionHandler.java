package com.frame.base.common.exception;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpStatus;
import com.frame.base.common.entity.BaseResp;
import com.frame.base.common.entity.BaseRespCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @desc BaseExceptionHandler
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BaseException.class)
    public BaseResp<String> handleException(BaseException e) {
        log.error(e.getMessage(), e);
        return BaseResp.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(BizException.class)
    public BaseResp<String> handleException(BizException e) {
        if (e.isNeedLog()) {
            log.error(e.getMessage(), e);
        } else {
            log.debug(e.getMessage(), e);
        }
        return BaseResp.error(e.getCode(), e.getMsg());
    }

    /**
     * 单个参数异常处理 @Validated 验证
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResp<String> handleException(ConstraintViolationException e) {
        StringBuilder errorMsg = new StringBuilder();
        for (ConstraintViolation<?> item : e.getConstraintViolations()) {
            if (errorMsg.length() > 0) {
                errorMsg.append("\n");
            }
            errorMsg.append(item.getMessage());
        }
        return BaseResp.error(BaseRespCode.BIZ, errorMsg.toString());
    }

    /**
     * JavaBean参数校验（x-www-form-urlencoded）异常处理 @Validated 验证
     */
    @ExceptionHandler(BindException.class)
    public BaseResp<String> handleException(BindException e) {
        log.error("参数绑定失败:" + e.getMessage(), e);

        StringBuilder errorMsg = new StringBuilder();
        for (ObjectError item : e.getBindingResult().getAllErrors()) {
            if (null == item.getDefaultMessage()) {
                continue;
            }
            if (errorMsg.length() > 0) {
                errorMsg.append("\n");
            }
            errorMsg.append(item.getDefaultMessage());
        }
        return BaseResp.error(HttpStatus.HTTP_BAD_REQUEST, "参数绑定失败:" + errorMsg);
    }

    /**
     * JavaBean参数校验（json）异常处理 @Validated 验证
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResp<String> handleException(MethodArgumentNotValidException e) {
        log.error("参数验证失败:" + e.getMessage(), e);

        StringBuilder errorMsg = new StringBuilder();
        for (FieldError item : e.getBindingResult().getFieldErrors()) {
            if (null == item.getDefaultMessage()) {
                continue;
            }
            if (errorMsg.length() > 0) {
                errorMsg.append("\n");
            }
            errorMsg.append(item.getDefaultMessage());
        }
        return BaseResp.error(HttpStatus.HTTP_BAD_REQUEST, "参数验证失败:" + errorMsg);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public BaseResp<String> handleException(NoHandlerFoundException e) {
        log.error("路径不存在，请检查路径是否正确:" + e.getMessage(), e);
        return BaseResp.error(HttpStatus.HTTP_NOT_FOUND, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResp<String> handleException(HttpMessageNotReadableException e) {
        log.error("数据读取异常:" + e.getMessage(), e);
        return BaseResp.error(HttpStatus.HTTP_BAD_REQUEST, "数据读取异常");
    }

    @ExceptionHandler(HttpException.class)
    public BaseResp<String> handleException(HttpException e) {
        log.error("HTTP异常:" + e.getMessage(), e);
        return BaseResp.error("HTTP异常");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResp<String> handleException(MissingServletRequestParameterException e) {
        log.error("缺少必要的请求参数:" + e.getMessage(), e);
        return BaseResp.error(HttpStatus.HTTP_BAD_REQUEST, "缺少必要的请求参数");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public BaseResp<String> handleException(IllegalArgumentException e) {
        log.error("请求参数不正确:" + e.getMessage(), e);
        return BaseResp.error(HttpStatus.HTTP_BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public BaseResp<String> handleException(MethodArgumentTypeMismatchException e) {
        log.error("请求参数格式错误:" + e.getMessage(), e);
        return BaseResp.error(HttpStatus.HTTP_BAD_REQUEST, "请求参数格式错误");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResp<String> handleException(HttpRequestMethodNotSupportedException e) {
        log.error("不支持当前请求方法:" + e.getMessage(), e);
        return BaseResp.error(HttpStatus.HTTP_BAD_METHOD, "不支持当前请求方法");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public BaseResp<String> handleException(HttpMediaTypeNotSupportedException e) {
        log.error("不支持当前媒体类型:" + e.getMessage(), e);
        return BaseResp.error(HttpStatus.HTTP_UNSUPPORTED_TYPE, "不支持当前媒体类型");
    }

    @ExceptionHandler(NumberFormatException.class)
    public BaseResp<String> handleException(NumberFormatException e) {
        log.error("数字格式不正确:" + e.getMessage(), e);
        return BaseResp.error("数字格式不正确");
    }

    @ExceptionHandler(Exception.class)
    public BaseResp<String> handleException(Exception e) {
        log.error("服务器异常:" + e.getMessage(), e);
        return BaseResp.error("服务器异常");
    }

}
