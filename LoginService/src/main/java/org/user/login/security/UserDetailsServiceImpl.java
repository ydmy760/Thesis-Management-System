package org.user.login.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.user.login.entity.User;
import org.user.login.service.UserLoginService;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserLoginService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUser(username);
        if(user == null) return null;
        return new UserDetailsImpl(user);
    }
}


