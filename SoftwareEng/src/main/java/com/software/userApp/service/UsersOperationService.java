package com.software.userApp.service;

import com.software.userApp.vo.MessageResult;
import com.software.userApp.vo.Result;

public interface UsersOperationService {
    MessageResult addUser(String uId, String name, boolean gender, String role, String password);
    Result deleteUser(String my_uId, String uId);
}
