package com.xzy.xframe.demo.order.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "user", type = "user")
@Data
public class UserEntity {
	@Id
	private String id;
	private String name;
	private Integer age;
	private Integer sex;

}
