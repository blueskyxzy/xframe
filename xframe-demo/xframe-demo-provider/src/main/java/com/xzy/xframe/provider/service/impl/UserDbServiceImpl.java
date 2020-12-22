package com.xzy.xframe.provider.service.impl;

import com.xzy.xframe.entity.User;
import com.xzy.xframe.provider.dao.UserDao;
import com.xzy.xframe.provider.service.UserDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user) {
        userDao.updateByPrimaryKeySelective(user);
    }
}
