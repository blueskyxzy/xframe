package com.frame.base.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @desc BaseEntityFlag
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseEntityFlag extends BaseEntityCommon {

    @ApiModelProperty(value = "删除标志:0=有效,非0=删除", hidden = true)
    private Long deleteFlag;

    /**
     * 标记为已删除
     */
    public void flagDelete() {
        this.deleteFlag = System.currentTimeMillis();
    }

    /**
     * 检查是否被删除
     */
    public boolean checkIsDeleted() {
        return null != deleteFlag && deleteFlag.compareTo(0L) != 0;
    }

}
