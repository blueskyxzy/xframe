package com.xzy.xframe.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;

    private String orderBillId;

    private Long userId;

    private String userName;

    private Date createTime;

    private Date updateTime;
}