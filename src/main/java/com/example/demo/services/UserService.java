package com.example.demo.services;

import lombok.RequiredArgsConstructor;
import com.example.demo.models.User;
import org.springframework.stereotype.Service;
import com.example.demo.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save (User user){
        userRepository.save(user);
        return user;
    }

    public User findUserById(String id){
        return userRepository.findById(id);
    }
}
