package com.app.demo.service;

import com.app.demo.model.Game;
import com.app.demo.repository.GameRepository;
import com.app.demo.util.GsonParser;
import com.app.demo.util.HttpRequestCreator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class GameService {

    private static final String KEY = "B88AF6D15A99EF5A4E01075EF63E5DF2";
    @NonNull
    private final GameRepository gameRepository;
    private GsonParser gsonParser = new GsonParser();

    private HttpRequestCreator requestCreator = new HttpRequestCreator("");

    public List<Game> saveAllGamesToDB() throws IOException {
        //clear the repo
        gameRepository.deleteAll();
        List<Game> gameList = new ArrayList<>();
        String gamesURI = "https://api.steampowered.com/ISteamApps/GetAppList/v2/?key=" + KEY;
        requestCreator.setURI(gamesURI);
        gameList = gsonParser.parseGameList(requestCreator.getAll());

        for (Game game : gameList
        ) {
            gameRepository.save(game);
        }

        return gameList;
    }

    public Game save(Game game) {
        gameRepository.save(game);
        return game;
    }


    public Game findGameById(int id) {
        return gameRepository.findById(id);
    }
}
