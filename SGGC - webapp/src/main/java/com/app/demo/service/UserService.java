package com.app.demo.service;

import com.app.demo.model.User;
import com.app.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        userRepository.save(user);
        return user;
    }

    public User findUserById(String id) {
        return userRepository.findUserByid(id);
    }
}
