package com.cau.web.Controller;

import com.cau.web.dto.ApiResponseNormal;
import com.cau.web.entity.User;
import com.cau.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@Secured("ROLE_ADMIN")  // 只允许ADMIN角色访问
public class UserController {

    @Autowired
    private UserService userService;

    // 获取所有用户
    @GetMapping("/all")
    public ApiResponseNormal<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ApiResponseNormal<>(200, users, "OK");
    }

    // 通过ID获取单个用户
    @GetMapping("/{id}")
    public ApiResponseNormal<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return new ApiResponseNormal<>(200, user, "OK");
    }

    // 添加新用户
    @PostMapping("/add")
    public ApiResponseNormal<String> addUser(@RequestBody User user) {
        String msg = userService.addUser(user);
        return new ApiResponseNormal<>(200, msg, msg);
    }

    // 更新用户信息
    @PutMapping("/update/{id}")
    public ApiResponseNormal<String> updateUser(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        String msg = userService.updateUser(user);
        return new ApiResponseNormal<>(200, msg, msg);
    }

    // 删除用户
    @DeleteMapping("/delete/{id}")
    public ApiResponseNormal<String> deleteUser(@PathVariable Integer id) {
        String msg = userService.deleteUser(id);
        return new ApiResponseNormal<>(200, msg, msg);
    }

    // 重置密码
    @PostMapping("/{id}/reset-password")
    public ApiResponseNormal<String> resetPassword(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        String newPassword = payload.get("newPassword");
        String msg = userService.resetPassword(id, newPassword);
        return new ApiResponseNormal<>(200, msg, msg);
    }
}


