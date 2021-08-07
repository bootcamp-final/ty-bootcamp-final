package com.github.buyalsky.userservice.controller;

import com.github.buyalsky.userservice.dto.UpdateUserDto;
import com.github.buyalsky.userservice.entity.User;
import com.github.buyalsky.userservice.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable String userId,
                           @RequestBody UpdateUserDto updateUserDto) {
        return userService.updateUser(userId, updateUserDto);
    }

    @DeleteMapping
    public void deleteUser(String userId) {
        userService.deleteUser(userId);
    }
}
