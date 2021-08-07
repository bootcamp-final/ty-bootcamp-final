package com.github.buyalsky.userservice.service;

import com.github.buyalsky.userservice.dto.UpdateUserDto;
import com.github.buyalsky.userservice.entity.User;

public interface UserService {

    User getUserById(String id);

    public User createUser(User user);

    public void deleteUser(String userId);

    public User updateUser(String id, UpdateUserDto updateUserDto);
}
