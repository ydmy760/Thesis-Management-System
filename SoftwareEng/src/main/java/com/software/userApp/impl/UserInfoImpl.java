package com.software.userApp.impl;

import com.software.userApp.service.UserInfoService;
import com.software.userApp.vo.MessageResult;
import com.software.userApp.vo.Result;
import com.software.userApp.vo.Role;
import com.software.userApp.vo.UserInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserInfoImpl implements UserInfoService {
    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @Override
    public UserInfoResult getAllUser(String uId) {
        UserInfoResult res = new UserInfoResult();
        String sql = "select role from user where uId=?";
        List<String> role;
        try {
            role = jdbcTemplate.queryForList(sql, new Object[]{uId}, String.class);
            if (role.isEmpty()) {
                res.error("登录用户不存在");
                return res;
            }
            if (role.get(0).equals(Role.ROLE_MANAGER)) {
                sql = "select * from user";
                res.setUserInfo(jdbcTemplate.queryForList(sql));
                res.success("查询成功");
            } else {
                res.error("无权限");
            }
        } catch (Exception e) {
            res.error("数据库错误");
        }
        return res;
    }
    @Override
    public MessageResult getUserByUid(String my_uId, String uId) {
        MessageResult res = new MessageResult();
        if(!Objects.equals(my_uId, uId)) {
            String role = jdbcTemplate.queryForObject("select role from user where uid='" + my_uId +"'", String.class);
            if(!Objects.equals(role, Role.ROLE_MANAGER)) {
                res.error("无权限");
                return res;
            }
        }
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "select * from user where uid=?",
                uId
        );
        if(list.size() == 0 ) {
            res.error("用户不存在");
            return res;
        }
        String name = String.valueOf(list.get(0).get("username"));
        boolean sex = Boolean.parseBoolean(String.valueOf(list.get(0).get("gender")));
        String role = String.valueOf(list.get(0).get("role"));

        res.success("查询成功");

        res.setUId(uId);
        res.setUserName(name);
        res.setGender(sex);
        res.setRole(role);
        return res;
    }

    @Override
    public Result modifyUserInfo(String my_uId, String uId, String username, boolean gender, String password) {
        Result res = new Result();
        try {
            if(!Objects.equals(my_uId, uId)) {
                String role = jdbcTemplate.queryForObject("select role from user where uId='" + my_uId +"'", String.class);
                if(!Objects.equals(role, Role.ROLE_MANAGER)) {
                    res.error("无权限");
                    return res;
                }
            }
            List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from user where uid=?", uId);
            if(list.size() == 0 ) {
                res.error("用户不存在");
                return res;
            }
            if (Objects.equals(password, "") || password == null) {
                jdbcTemplate.update("update user set username=?, gender=? where uId = ?", username, gender, uId);
            } else {
                jdbcTemplate.update("update user set username=?, gender=?, password=? where uId = ?", username, gender, password, uId);
            }
        } catch (Exception e) {
            res.error("数据库内部错误");
            return res;
        }
        res.success("成功更新" + uId);
        return res;
    }
}
