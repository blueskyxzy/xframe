package com.frame.base.common.model;

import com.frame.base.common.entity.BaseEntityCommon;
import lombok.Data;

import java.util.Date;

/**
 * @desc SysUserVO
 */
@Data
public class SysUserVO {

    /**
     * 系统用户ID
     */
    String sUserId;

    /**
     * 系统用户名称
     */
    String sUserName;

    /**
     * 登录名
     */
    String loginName;

    /**
     * 手机号
     */
    String mobile;

    public void createObjBase(Object obj) {
        if (obj instanceof BaseEntityCommon) {
            ((BaseEntityCommon) obj).setCreateTime(new Date());
            ((BaseEntityCommon) obj).setCreateUserId(this.getSUserId());
            ((BaseEntityCommon) obj).setCreateUserName(this.getSUserName());
        }
    }

    public void updateObjBase(Object obj) {
        if (obj instanceof BaseEntityCommon) {
            ((BaseEntityCommon) obj).setUpdateTime(new Date());
            ((BaseEntityCommon) obj).setUpdateUserId(this.getSUserId());
            ((BaseEntityCommon) obj).setUpdateUserName(this.getSUserName());
        }
    }

}
