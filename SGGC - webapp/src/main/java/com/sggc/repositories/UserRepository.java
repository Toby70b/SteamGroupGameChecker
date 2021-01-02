package com.sggc.repositories;

import com.sggc.models.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User ,String> {
    User findUserByid(String id);
}
