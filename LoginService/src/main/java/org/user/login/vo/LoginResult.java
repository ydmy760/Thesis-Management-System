package org.user.login.vo;

import lombok.Data;
import org.user.login.entity.User;

@Data
public class LoginResult extends AbstractResult{
    private String token;
    private String fullName;
    private boolean isAdmin;
}
