package com.software.userApp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "返回状态码")
public class StateCode {
    @ApiModelProperty(value = "成功")
    public static final int SUCCESS = 0;
    @ApiModelProperty(value = "数据库失败")
    public static final int DATABASE_FAILURE = 1;
    @ApiModelProperty(value = "数据类型错误")
    public static final int DATA_FAILURE = 2;
}
