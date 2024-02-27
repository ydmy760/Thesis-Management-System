package com.software.userApp.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/over")
@Api(value = "假期申请")
public class TestController {
    @RequestMapping(value = "/apply", method = RequestMethod.GET)
    @ResponseBody
    public String apply() {
        return "success";
    }
}
