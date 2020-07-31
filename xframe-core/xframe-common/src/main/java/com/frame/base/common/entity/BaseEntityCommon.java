package com.frame.base.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @desc BaseEntityCommon
 */
@Data
public class BaseEntityCommon implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建人ID", hidden = true)
    private String createUserId;

    @ApiModelProperty(value = "创建人名称", hidden = true)
    private String createUserName;

    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    @ApiModelProperty(value = "修改人ID", hidden = true)
    private String updateUserId;

    @ApiModelProperty(value = "修改人名称", hidden = true)
    private String updateUserName;

    @ApiModelProperty(value = "修改时间", hidden = true)
    private Date updateTime;

}
