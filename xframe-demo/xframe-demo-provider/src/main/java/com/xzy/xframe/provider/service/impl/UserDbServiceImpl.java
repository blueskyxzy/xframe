package com.xzy.xframe.provider.service.impl;

import com.xzy.xframe.provider.dao.UserDao;
import com.xzy.xframe.provider.entity.User;
import com.xzy.xframe.provider.service.UserDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: xzy
 * @create: 2020-12-14
 **/

@Service
public class UserDbServiceImpl implements UserDbService {

    @Autowired
    private UserDao userDao;


    @Override
    public User getUser(Long id) {
        User user = userDao.selectByPrimaryKey(id);
        return user;
    }
}
