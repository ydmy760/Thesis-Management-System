package org.user.login.body;

import lombok.Data;

@Data
public class RegisterBody {
    private String userName;
    private String passWord;
    private String email;
    private String workPlace;
    private String region;
    private String fullName;
    private boolean isAdmin;
}
