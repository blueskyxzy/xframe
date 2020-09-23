package com.xzy.xframe.demo.order.contoller;

import com.xzy.xframe.demo.order.dao.BookDao;
import com.xzy.xframe.demo.order.dao.CloudDiskDao;
import com.xzy.xframe.demo.order.dao.UserDao;
import com.xzy.xframe.demo.order.entity.BookEntity;
import com.xzy.xframe.demo.order.entity.CloudDiskEntity;
import com.xzy.xframe.demo.order.entity.UserEntity;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RestController
public class SearchController {
	@Autowired
	private CloudDiskDao cloudDiskDao;

	@Autowired
	private BookDao bookDao;

	@Autowired
	private UserDao userDao;

	@RequestMapping("/search")
	public String search(String keyword, String describe, @PageableDefault(page = 0, value = 5) Pageable pageable,
			HttpServletRequest req) {
		Long startTime = System.currentTimeMillis();
		// 1.创建查询对象
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		if (!StringUtils.isEmpty(keyword)) {
			MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("name", keyword);
			boolQuery.must(matchQuery);
		}
		if (!StringUtils.isEmpty(describe)) {
			MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("describe", describe);
			boolQuery.must(matchQuery);
		}
		// 2.调用查询接口
		Page<CloudDiskEntity> page = cloudDiskDao.search(boolQuery, pageable);

		req.setAttribute("page", page);
		// 记录总数
		req.setAttribute("total", page.getTotalElements());
		req.setAttribute("keyword", keyword);
		// 计算分页总数
		int totalPage = (int) ((page.getTotalElements() - 1) / pageable.getPageSize() + 1);
		// 分页总数
		req.setAttribute("totalPage", totalPage);
		Long emdTime = System.currentTimeMillis();
		req.setAttribute("time", emdTime - startTime);
		return "search";
	}

	// springboot 整合 es 查询
	// 根据id查询文档信息
	@RequestMapping("/findById/{id}")
	public Optional<CloudDiskEntity> findById(@PathVariable String id) {
		return cloudDiskDao.findById(id);
	}

	// 新增book
	@RequestMapping("/addBook")
	public BookEntity addBook(@RequestBody BookEntity book) {
		return bookDao.save(book);
	}

	@RequestMapping("/getBook")
	public Optional<BookEntity> getBook(String id) {
		return bookDao.findById(id);
	}

	// 添加文档
	@RequestMapping("/addUser")
	public UserEntity addUser(@RequestBody UserEntity userEntity) {
		return userDao.save(userEntity);
	}

	// 查询文档
	@RequestMapping("/findById")
	public Optional<UserEntity> findUserById(String id) {
		return userDao.findById(id);
	}

	@RequestMapping("/selectUser")
	public List<UserEntity> selectUser(String q) {
		QueryStringQueryBuilder builder = new QueryStringQueryBuilder(q);
		Iterable<UserEntity> searchResult = userDao.search(builder);
		Iterator<UserEntity> iterator = searchResult.iterator();
		List<UserEntity> list = new ArrayList<UserEntity>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}

}
