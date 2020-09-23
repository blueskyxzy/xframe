package com.xzy.xframe.demo.order.dao;

import com.xzy.xframe.demo.order.entity.CloudDiskEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CloudDiskDao extends ElasticsearchRepository<CloudDiskEntity, String> {

}
