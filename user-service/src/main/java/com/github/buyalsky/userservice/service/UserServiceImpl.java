package com.github.buyalsky.userservice.service;

import com.github.buyalsky.userservice.dto.UpdateUserDto;
import com.github.buyalsky.userservice.entity.User;
import com.github.buyalsky.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.net.URI;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final RestTemplate restTemplate;
    private final UserRepository repository;

    @Value("${shopping-cart-service.url}")
    private String shoppingCartServiceUrl;

    public UserServiceImpl(RestTemplate restTemplate, UserRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
    }

    public User getUserById(String id) {
        return repository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    public User createUser(User user) {
        User createdUser = repository.save(user);
        String createdUserId = createdUser.getId();
        restTemplate.postForObject(
            "http://" + shoppingCartServiceUrl + "/{createdUserId}", String.class, createdUserId);

        return user;
    }

    public void deleteUser(String userId) {
        repository.deleteById(userId);
    }

    public User updateUser(String id, UpdateUserDto updateUserDto) {
        Optional<User> currentUser = repository.findById(id);
        if (currentUser.isEmpty())
            throw new EntityNotFoundException("Specified customer cannot be updated since it does not exist");

        User updatedUser = updateUserDto.mapToUser(currentUser.get());
        return repository.save(updatedUser);
    }

}
