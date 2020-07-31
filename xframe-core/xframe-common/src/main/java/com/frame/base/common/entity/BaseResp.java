package com.frame.base.common.entity;

import cn.hutool.http.HttpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "统一API响应结果封装")
public class BaseResp<T> {

    @ApiModelProperty(value = "返回状态码:0=成功,其他=失败", position = 1)
    protected int code;

    @ApiModelProperty(value = "是否成功", position = 2)
    protected boolean success;

    @ApiModelProperty(value = "返回描述", position = 3)
    protected String msg;

    @ApiModelProperty(value = "数据结果集", position = 4)
    protected T data;

    protected BaseResp() {
        this.code = 0;
        this.success = true;
        this.msg = "success";
    }

    public static <T> BaseResp<T> ok() {
        return new BaseResp<>();
    }

    public static <T> BaseResp<T> okMsg(String msg) {
        BaseResp<T> baseResp = new BaseResp<>();
        baseResp.setMsg(msg);
        return baseResp;
    }

    public static <T> BaseResp<T> ok(T data) {
        BaseResp<T> baseResp = new BaseResp<>();
        baseResp.setData(data);
        return baseResp;
    }

    public static BaseResp<String> error() {
        return error("未知异常，请联系管理员");
    }

    public static BaseResp<String> error(String msg) {
        return error(HttpStatus.HTTP_INTERNAL_ERROR, msg);
    }

    public static BaseResp<String> errorBiz(String msg) {
        return error(BaseRespCode.BIZ, msg);
    }

    public static BaseResp<String> error(int code, String msg) {
        BaseResp<String> baseResp = new BaseResp<>();
        baseResp.setCode(code);
        baseResp.setSuccess(false);
        baseResp.setMsg(msg);
        return baseResp;
    }

}
