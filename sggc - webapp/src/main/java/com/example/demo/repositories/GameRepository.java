package com.example.demo.repositories;

import com.example.demo.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  GameRepository extends JpaRepository<Game, Integer> {

    Game findById(int id);

    @Override
    List<Game> findAll();
}
