package com.app.demo;

import com.app.demo.model.Game;
import com.app.demo.repository.GameRepository;
import com.app.demo.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SteamGroupGamesApplication {

    private static final Logger log = LoggerFactory.getLogger(SteamGroupGamesApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SteamGroupGamesApplication.class, args);
    }

    //Load the list of apps into the game repository on load
    @Bean
    public CommandLineRunner demo(GameRepository repository) {
        return (args) -> {
            GameService gameService = new GameService(repository);
            log.info("Make Get Request for all Steam games");
            List<Game> gamesList = gameService.saveAllGamesToDB();
            System.out.println(gamesList.size() + " games saved");

        };
    }

}
