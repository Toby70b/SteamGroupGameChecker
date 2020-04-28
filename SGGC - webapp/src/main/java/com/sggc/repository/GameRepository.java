package com.sggc.repository;

import com.sggc.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {

    Game findGameByAppid(int appid);

    @Override
    List<Game> findAll();
}
