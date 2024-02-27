package com.software.userApp.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Role {
    @ApiModelProperty(value = "管理员")
    public static final String ROLE_MANAGER = "admin";
    @ApiModelProperty(value = "普通用户")
    public static final String ROLE_STAFF = "user";
}
