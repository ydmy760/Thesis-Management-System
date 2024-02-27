package com.software.userApp.body;

import lombok.Data;

@Data
public class RegisterBody {
    String name;
    boolean gender;
    String role;
    String password;
}
