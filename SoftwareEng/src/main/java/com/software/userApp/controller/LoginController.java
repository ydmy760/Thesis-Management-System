package com.software.userApp.controller;

import com.software.userApp.body.LoginBody;
import com.software.userApp.body.RegisterBody;
import com.software.userApp.config.CookieUtils;
import com.software.userApp.service.LoginService;
import com.software.userApp.vo.MessageResult;
import com.software.userApp.vo.Result;
import com.software.userApp.vo.StateCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
@Api(value = "登录相关")
public class LoginController {
    @Autowired
    LoginService loginService;

    @ApiOperation("登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public MessageResult login(HttpServletResponse response,
                               @RequestBody LoginBody loginBody) {
        MessageResult res;
        String uId = loginBody.getUid();
        String pwd = loginBody.getPassword();
        res = loginService.login(uId, pwd);
        if(res.getCode() == StateCode.DATA_FAILURE) {
            res.error("login failed");
            return res;
        }
        String u_Id = res.getUId();
        Cookie cookie = new Cookie("uId", u_Id);
        cookie.setPath("/");
        response.addCookie(cookie);

        return res;
    }
    @ApiOperation("注册")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public MessageResult register(HttpServletResponse response,
                           @RequestBody RegisterBody registerBody){
        String name = registerBody.getName();
        boolean sex = registerBody.isGender();
        String role = registerBody.getRole();
        String pwd = registerBody.getPassword();
        MessageResult result = loginService.register(name, sex, role, pwd);
        return result;
    }

    @ApiOperation("登出")
    @RequestMapping(value = "/out", method = RequestMethod.POST)
    public Result logOut(HttpServletRequest request,
                         HttpServletResponse response) {
        Result res = new Result();
        CookieUtils.setCookie(request, response,"uId", null, 0);
        res.success("success");
        return res;
    }
}
