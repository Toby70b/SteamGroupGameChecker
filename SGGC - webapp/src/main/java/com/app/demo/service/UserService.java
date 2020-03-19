package com.app.demo.service;

import lombok.RequiredArgsConstructor;
import com.app.demo.model.User;
import org.springframework.stereotype.Service;
import com.app.demo.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        userRepository.save(user);
        return user;
    }

    public User findUserById(String id) {
        return userRepository.findById(id);
    }
}
