package org.user.login.controller;

import cn.hutool.jwt.JWT;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.user.login.body.LoginBody;
import org.user.login.body.RegisterBody;
import org.user.login.entity.User;
import org.user.login.model.MyConstant;
import org.user.login.service.UserLoginService;
import org.user.login.vo.LoginResult;
import org.user.login.vo.RegisterResult;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@RestController
@Api(value = "登录注册")
public class LoginController {
    @Autowired
    UserLoginService loginService;

    @Autowired
    AuthenticationManager authenticationManager;

    //过期时间：七天
    public static final long EXPIRE = 7 * 24 * 60 * 60;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResult login(HttpServletResponse response,
                             @RequestBody LoginBody loginBody) {
        LoginResult res = new LoginResult();
        // 拦截http请求，获取用户名，密码等信息
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginBody.getUsername(), loginBody.getPassWord());
        // 从filter中获取认证信息，然后查找合适的AuthenticationProvider来发起认证流程
        UserDetails user = (UserDetails) authenticationManager.authenticate(token).getPrincipal();
        String takeToken = JWT.create()
                .setPayload("username", loginBody.getUsername())
                .setKey(MyConstant.JWT_SIGN_KEY.getBytes(StandardCharsets.UTF_8))
                .setExpiresAt(new Date(System.currentTimeMillis() + EXPIRE * 1000))
                .sign();
        res.success("登录成功");
        res.setToken(takeToken);
        res.setFullName(user.getUsername());
        res.setAdmin(user.isEnabled());
        return res;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RegisterResult register(@RequestBody RegisterBody registerBody) {
        String uId = String.valueOf(UUID.randomUUID());
        User user = new User();
        user.setAll(registerBody);
        user.setUid(uId);
        return loginService.register(user);
    }
}
