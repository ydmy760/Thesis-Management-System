package paperUser.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paperUser.entity.User;
import paperUser.repository.UserRepository;
import paperUser.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(String username) {
        List<User> user = userRepository.findByUsername(username);
        if (user.size() == 0) return null;
        return user.get(0);
    }
}
