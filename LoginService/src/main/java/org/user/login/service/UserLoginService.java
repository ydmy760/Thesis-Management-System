package org.user.login.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.user.login.body.LoginBody;
import org.user.login.body.RegisterBody;
import org.user.login.entity.User;
import org.user.login.vo.LoginResult;
import org.user.login.vo.RegisterResult;

@ComponentScan(basePackages="org.user.login")
@Service
public interface UserLoginService {
    User getUser(String username);

    RegisterResult register(User user);
}
