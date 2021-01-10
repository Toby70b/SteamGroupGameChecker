package com.sggc;

import com.sggc.models.Game;
import com.sggc.repositories.GameRepository;
import com.sggc.services.GameService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SteamGroupGamesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SteamGroupGamesApplication.class, args);
    }

}
