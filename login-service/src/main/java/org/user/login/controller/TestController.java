package org.user.login.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "测试连通性")
public class TestController {
    @Autowired
    RedisTemplate redisTemplate = new RedisTemplate();

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "success";
    }

    @RequestMapping(value = "/redis", method = RequestMethod.GET)
    public String redis() {
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set("001", "test1");
        ops.set("002", "test2");
        ops.set("key", "value");

        String s1 = String.valueOf(ops.get("001"));
        String s2 = String.valueOf(ops.get("002"));

        return "Redis结果" + s1 + " " + s2;
    }
}