package com.software.userApp.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractResult {
    @ApiModelProperty(value = "状态码")
    private int code;
    @ApiModelProperty(value = "返回信息")
    private String msg;

    public void success(String msg){
        this.setCode(StateCode.SUCCESS);
        this.setMsg(msg);
    }

    public void error(String msg){
        this.setCode(StateCode.DATABASE_FAILURE);
        this.setMsg(msg);
    }
}
