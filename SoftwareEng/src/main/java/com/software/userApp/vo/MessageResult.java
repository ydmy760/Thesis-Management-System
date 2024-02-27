package com.software.userApp.vo;

import lombok.Data;

@Data
public class MessageResult extends AbstractResult{
    String uId;
    String userName;
    boolean gender;
    String role;
}
