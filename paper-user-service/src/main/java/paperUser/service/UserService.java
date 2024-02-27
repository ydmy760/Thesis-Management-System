package paperUser.service;

import org.springframework.stereotype.Service;
import paperUser.entity.User;

@Service
public interface UserService {
    User getUser(String username);
}
