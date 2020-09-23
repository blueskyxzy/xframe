package com.xzy.xframe.demo.order.dao;

import com.xzy.xframe.demo.order.entity.UserEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserDao extends ElasticsearchRepository<UserEntity, String> {

}
