package com.software.userApp.controller;

import com.software.userApp.body.ModifyBody;
import com.software.userApp.config.CookieUtils;
import com.software.userApp.service.UserInfoService;
import com.software.userApp.vo.MessageResult;
import com.software.userApp.vo.Result;
import com.software.userApp.vo.UserInfoResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/user")
public class UserInfoController {
    @Autowired
    UserInfoService userInfoService;

    @ApiOperation("查看所有用户信息")
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ResponseBody
    public UserInfoResult checkAllUser(HttpServletRequest request) {
        String uId = CookieUtils.getCookieValue(request, "uId");
        UserInfoResult res = new UserInfoResult();
        if(uId == null) {
            res.error("没有cookies，无操作权限");
            return res;
        }
        res = userInfoService.getAllUser(uId);
        return res;
    }

    @ApiOperation("查看用户信息")
    @GetMapping("/user")
    public MessageResult getUser(HttpServletRequest request,
                                 @RequestParam("uid") String uid) {
        String my_uId = CookieUtils.getCookieValue(request, "uId");
        MessageResult res = new MessageResult();
        if(my_uId == null) {
            res.error("没有cookies，无操作权限");
            return res;
        }
        res = userInfoService.getUserByUid(my_uId, uid);
        return res;
    }

    @ApiOperation("修改用户信息")
    @PutMapping("/user")
    public Result modifyUserInfo(HttpServletRequest request, @RequestBody ModifyBody modifyBody) {
        String my_uId = CookieUtils.getCookieValue(request, "uId");
        Result res = new Result();
        if(my_uId == null) {
            res.error("没有cookies，无操作权限");
            return res;
        }
        res = userInfoService.modifyUserInfo(my_uId, modifyBody.getUid(), modifyBody.getName(), modifyBody.isGender(), modifyBody.getPassword());
        return res;
    }
}
