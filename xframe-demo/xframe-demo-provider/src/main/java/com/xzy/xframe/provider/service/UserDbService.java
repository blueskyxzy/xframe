package com.xzy.xframe.provider.service;


import com.xzy.xframe.entity.User;

public interface UserDbService {

    User getUser(Long id);

    void updateUser(User user);
}
