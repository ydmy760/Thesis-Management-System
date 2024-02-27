package com.software.userApp.service;

import com.software.userApp.vo.MessageResult;
import com.software.userApp.vo.Result;
import com.software.userApp.vo.UserInfoResult;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@ComponentScan(basePackages="com.software.userApp")
@Service
public interface UserInfoService {
    UserInfoResult getAllUser(String uId);
    MessageResult getUserByUid(String my_uId, String uid);
    Result modifyUserInfo(String my_uId, String uId, String username, boolean gender, String password);
}
