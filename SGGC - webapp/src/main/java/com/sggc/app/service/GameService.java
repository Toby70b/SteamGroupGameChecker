package com.sggc.app.service;

import com.sggc.app.model.Game;
import com.sggc.app.repository.GameRepository;
import com.sggc.app.util.GsonParser;
import com.sggc.app.util.HttpRequestCreator;
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

    public Game findByAppid(int appid) {
        return gameRepository.findGameByAppid(appid);
    }
}
