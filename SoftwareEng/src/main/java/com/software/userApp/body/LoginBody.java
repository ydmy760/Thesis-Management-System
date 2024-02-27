package com.software.userApp.body;

import lombok.Data;

@Data
public class LoginBody {
    String uid;
    String password; // 这里的password不应该明文传输，尝试前端md5加密
}
