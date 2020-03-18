package com.example.demo;

import models.Game;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import repositories.GameRepository;
import services.GameService;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"controllers"} )
@EnableJpaRepositories(basePackages = {"repositories"})
@EntityScan(basePackages = {"models"})
public class DemoApplication {

    private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    //Load the list of apps into the game repository on load
    @Bean
    public CommandLineRunner demo(GameRepository repository) {
        return (args) -> {
            GameService gameService = new GameService(repository);
            log.info("Make Get Request for all Steam games");
            List<Game> gamesList = gameService.saveAllGamesToDB();
            System.out.println(gamesList.size()+" games saved");

        };
    }

}
