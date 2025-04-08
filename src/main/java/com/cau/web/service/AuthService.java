package com.cau.web.service;
import com.cau.web.entity.User;

public interface AuthService {
    boolean register(User user, String token);
    User login(String username, String password);

    boolean createUser(User user, String role);

    boolean updateUser(Integer id, User user);

    boolean deleteUser(Integer id);

    User getUserInfo(String username);
}
