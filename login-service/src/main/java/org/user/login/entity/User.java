package org.user.login.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.user.login.body.RegisterBody;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user")
public class User {
    @TableId(type = IdType.AUTO)
    private String uid;
    private String username;
    private String password;
    private String email;
    private String workplace;
    private String region;
    private String fullName;
    private boolean isAdmin;

    public void setAll(RegisterBody body) {
        this.setUsername(body.getUserName());
        this.setPassword(body.getPassWord());
        this.setEmail(body.getEmail());
        this.setRegion(body.getRegion());
        this.setWorkplace(body.getWorkPlace());
        this.setFullName(body.getFullName());
        this.setAdmin(body.isAdmin());
    }
}
