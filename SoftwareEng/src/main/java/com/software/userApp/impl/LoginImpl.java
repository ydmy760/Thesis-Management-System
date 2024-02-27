package com.software.userApp.impl;

import com.software.userApp.service.LoginService;
import com.software.userApp.vo.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginImpl implements LoginService {
    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    @Override
    public MessageResult login(String uId, String password) {
        MessageResult res = new MessageResult();
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "select * from user where uId=?",
                uId
        );
        if(list.size() == 0) {
            res.error("用户未注册");
            return res;
        }
        String pwd = String.valueOf(list.get(0).get("password"));

        if(list.size() == 0 || !Objects.equals(password, pwd)) {
            res.error("登陆错误");
            return res;
        }
        String name = String.valueOf(list.get(0).get("username"));
        System.out.println(list);
        boolean sex = Objects.equals(String.valueOf(list.get(0).get("gender")), "1");
        String role = String.valueOf(list.get(0).get("role"));

        res.success("登陆成功");

        res.setUId(uId);
        res.setUserName(name);
        res.setGender(sex);
        res.setRole(role);
        return res;
    }

    @Override
    public MessageResult register(String name, boolean sex, String role, String password) {
        MessageResult res = new MessageResult();
        String uId = role + System.currentTimeMillis() + name;
        try {
            jdbcTemplate.update("insert into user values(?,?,?,?,?)", uId, name, sex, role, password);
        }
        catch(Exception e){
            res.error("数据库错误");
            return res;
        }
        res.setUId(uId);
        res.setUserName(name);
        res.setRole(role);
        res.setGender(sex);
        res.success("注册成功");
        return res;
    }
}
