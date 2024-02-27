package com.software.userApp.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserInfoResult extends AbstractResult{
    List<Map<String, Object>> userInfo;
}
