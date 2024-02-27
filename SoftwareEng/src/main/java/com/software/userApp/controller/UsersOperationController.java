package com.software.userApp.controller;


import com.software.userApp.body.AddUserBody;
import com.software.userApp.body.RegisterBody;
import com.software.userApp.service.UsersOperationService;
import com.software.userApp.vo.MessageResult;
import com.software.userApp.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1")
@Api(value = "用户相关操作")
public class UsersOperationController {
    @Autowired
    UsersOperationService usersOperationService;

    @ApiOperation(value = "管理员加人")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public MessageResult addUser(@RequestBody AddUserBody body,
                                 HttpServletRequest request
                                 ) {
        Cookie[] cookies = request.getCookies();
        MessageResult res = new MessageResult();
        String uId = getCookie(cookies);
        if(uId == null) {
            res.error("没有cookies，无操作权限");
            return res;
        }
        String name = body.getName();
        boolean gender = body.isGender();
        String role = body.getRole();
        String password = "123456";
        return usersOperationService.addUser(uId,name,gender,role,password);
    }

    @ApiOperation(value = "管理员删人")
    @RequestMapping(value = "/user/{uId}", method = RequestMethod.DELETE)
    public Result deleteUser(HttpServletRequest request,
                             @PathVariable("uId") String uId) {
        Result res = new Result();
        Cookie[] cookies = request.getCookies();
        String my_uId = getCookie(cookies);
        if(my_uId == null) {
            res.error("没有cookies，无操作权限");
            return res;
        }
        return usersOperationService.deleteUser(my_uId, uId);
    }

    public String getCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("uId")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
