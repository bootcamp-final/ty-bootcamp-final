package com.github.buyalsky.userservice.repository;

import com.github.buyalsky.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
