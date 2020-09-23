package com.xzy.xframe.demo.order.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@Document(indexName = "clouddisk", type = "disk")
public class CloudDiskEntity {
    @Id
    private String id;
    // 名称 text类型
    private String name;
    // 来源 百度 新浪 keyword
    private String source;
    // 描述 text类型
    private String describe;
    // 分享时间 date
    private Date shartime;
    // 浏览次数 lonbg
    private Long browsetimes;
    // 文件大小
    private Double filesize;
    // 分享人  keyword
    private String sharpeople;
    // 收录时间 date
    private String collectiontime;
    // 地址  keyword
    private String baiduaddres;

}
