package com.xzy.xframe.demo.order.dao;

import com.xzy.xframe.demo.order.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookDao extends CrudRepository<BookEntity, String> {
	public List<BookEntity> getByMessage(String key);
}
