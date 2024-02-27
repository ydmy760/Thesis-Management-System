package org.user.login.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.user.login.dao.LoginDao;
import org.user.login.entity.User;
import org.user.login.service.UserLoginService;
import org.user.login.vo.RegisterResult;

import java.util.List;

@Service
public class UserLoginImpl implements UserLoginService {
    @Autowired
    private LoginDao loginDao;

    @Override
    public User getUser(String username) {
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("username", username).select("*");
        List<User> list = loginDao.selectList(queryWrapper);
        if(list.size() == 0)return null;
        return list.get(0);
    }

    @Override
    public RegisterResult register(User user) {
        RegisterResult res = new RegisterResult();
        String username = user.getUsername();
        String email = user.getEmail();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username", username).select("*");
        userQueryWrapper.eq("email", email).select("*");
        List<User> list = loginDao.selectList(queryWrapper);
        List<User> userList = loginDao.selectList(userQueryWrapper);

        if(list.size() != 0) {
            res.error("用户名已被注册");
            return res;
        }
        if(userList.size() != 0 ) {
            res.error("邮箱已被注册");
            return res;
        }
        loginDao.insert(user);
        res.success("注册成功");
        res.setUId(user.getUid());
        return res;
    }
}
