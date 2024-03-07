package org.user.login.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractResult {
    private static final int SUCCESS = 200;
    private static final int FAILED = 1;
    @ApiModelProperty(value = "状态码")
    private int code;
    @ApiModelProperty(value = "状态描述")
    private String msg;

    public void success(String msg) {
        this.setCode(SUCCESS);
        this.setMsg(msg);
    }
    public void error(String msg) {
        this.setCode(FAILED);
        this.setMsg(msg);
    }
}
