package com.software.userApp.impl;

import com.software.userApp.service.UsersOperationService;
import com.software.userApp.vo.MessageResult;
import com.software.userApp.vo.Result;
import com.software.userApp.vo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UsersOperationImpl implements UsersOperationService {
    @Autowired
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    @Override
    public MessageResult addUser(String uId, String name, boolean gender, String role, String password) {
        MessageResult res = new MessageResult();
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from user where uId=?", uId);
        if(list.size() == 0) {
            res.error("登录用户不存在");
            return res;
        }
        String my_role = String.valueOf(list.get(0).get("role"));
        //生成唯一的uId
        String u_id = role + System.currentTimeMillis();
        if(Objects.equals(my_role, Role.ROLE_MANAGER)) {
            try {
                jdbcTemplate.update("insert into user values(?,?,?,?,?)", u_id, name, gender, role, password);
            } catch (Exception e) {
                res.error("数据库内部错误，写入失败");
                return res;
            }
        } else {
            res.error("无权限");
            return res;
        }
        res.success("写入成功");
        res.setUId(u_id);
        res.setUserName(name);
        res.setRole(role);
        res.setGender(gender);

        return res;
    }

    @Override
    public Result deleteUser(String my_uId, String uId) {
        Result res = new Result();
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from user where uId=?", my_uId);
        if(list.size() == 0) {
            res.error("登录用户不存在");
            return res;
        }
        String role = (String) list.get(0).get("role");
        if(Objects.equals(role, Role.ROLE_MANAGER)) {
            List<Map<String, Object>> list_user = jdbcTemplate.queryForList("select * from user where uId=?", uId);
            if(list_user.size() == 0 || Objects.equals(list_user.get(0).get("role"), Role.ROLE_MANAGER)) {
                res.error("删除对象不存在或删除对象级别不允许被删除");
                return res;
            }
            try {
                jdbcTemplate.update("delete from user where uId=?", uId);
            } catch (Exception e) {
                res.error("数据库内部错误");
                return res;
            }
        } else {
            res.error("无权限");
            return res;
        }
        res.success("成功删除" + uId);
        return res;
    }
}
