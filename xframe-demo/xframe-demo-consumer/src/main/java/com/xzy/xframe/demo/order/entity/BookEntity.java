package com.xzy.xframe.demo.order.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "book", type = "book")
@Data
public class BookEntity {
	@Id
	String id;
	String name;
	String message;

}
