package com.cau.web.service;

import com.cau.web.entity.User;
import com.cau.web.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 获取所有用户
    public List<User> getAllUsers() {
        return userMapper.selectList(null);  // 查询所有用户
    }

    // 根据ID获取用户
    public User getUserById(Integer id) {
        return userMapper.selectById(id);
    }

    // 添加用户
    public String addUser(User user) {
        if (userMapper.selectById(user.getId()) != null) {
            return "用户ID已存在";
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        return "用户已添加";
    }

    // 更新用户
    public String updateUser(User user) {
        if (userMapper.selectById(user.getId()) == null) {
            return "用户不存在";
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.updateById(user);
        return "用户信息已更新";
    }

    // 删除用户
    public String deleteUser(Integer id) {
        if (userMapper.selectById(id) == null) {
            return "用户不存在";
        }
        userMapper.deleteById(id);
        return "用户已删除";
    }
}

