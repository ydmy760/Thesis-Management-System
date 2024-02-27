package com.software.userApp.service;

import com.software.userApp.vo.MessageResult;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;


@ComponentScan(basePackages="com.software.userApp")
@Service
public interface LoginService {
    MessageResult login(String uId, String password); //登录函数

    MessageResult register(String name, boolean sex, String role, String password); //注册函数
}

