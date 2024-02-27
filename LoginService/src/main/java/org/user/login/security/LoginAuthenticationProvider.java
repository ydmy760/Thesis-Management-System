package org.user.login.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        //用户名 密码
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        //通过用户名获取用户信息
        UserDetails userDetails = userService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new UsernameNotFoundException("用户不存在！");
        }
        // 记录：passwordEncoder方法matches第一个参数是明文，第二个是密文
        String password_true = passwordEncoder.encode(userDetails.getPassword());
        if (!passwordEncoder.matches(password, password_true)) {
            throw new BadCredentialsException("密码不正确！");
        }


        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

