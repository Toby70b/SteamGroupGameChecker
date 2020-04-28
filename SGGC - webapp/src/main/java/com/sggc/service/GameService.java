package com.sggc.service;

import com.sggc.controller.SGGCController;
import com.sggc.exception.UserHasNoGamesException;
import com.sggc.model.Game;
import com.sggc.model.User;
import com.sggc.repository.GameRepository;
import com.sggc.util.GsonParser;
import com.sggc.util.HttpRequestCreator;
import com.sggc.util.RuntimeWrappablePredicateMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    @NonNull
    private final GameRepository gameRepository;
    private GsonParser gsonParser = new GsonParser();
    private static final String KEY = "B88AF6D15A99EF5A4E01075EF63E5DF2";
    private HttpRequestCreator requestCreator = new HttpRequestCreator("");
    private static Logger LOGGER = LoggerFactory.getLogger(GameService.class);
    private static final int MULTIPLAYER_ID = 1;

    public List<Integer> removeNonMultiplayerGamesFromList(List<Integer> gameIds) {
        gameIds = gameIds.stream().filter(
                RuntimeWrappablePredicateMapper.wrap(this::isMultiplayer)
        ).collect(Collectors.toList());
        return gameIds;
    }

    private boolean isMultiplayer(Integer gameId) throws IOException {
        Game game = gameRepository.findGameByAppid(gameId);
        if (game.getMultiplayer() != null) {
            return game.getMultiplayer();
        } else {
            //Make an api call for the gameId
            String URI = "http://store.steampowered.com/api/appdetails/?appids=" + gameId;
            LOGGER.debug("Contacting " + URI + " to get details of game " + gameId);
            //Create the request
            HttpRequestCreator requestCreator = new HttpRequestCreator(URI);
            //Parse the response to get list of categories
            List<Integer> categoryIds = new GsonParser().parseGameDetailsList(requestCreator.getAll());
            //Check for presence of multiplayer category
            for (int i : categoryIds) {
                if (i == MULTIPLAYER_ID) {
                    game.setMultiplayer(true);
                    gameRepository.save(game);
                    return true;
                }
            }
            game.setMultiplayer(false);
            gameRepository.save(game);
            return false;
        }
    }

    public List<Game> getCommonGames(List<Integer> gameIds) {
        return removeNonMultiplayerGamesFromList(gameIds).stream().map(gameRepository::findGameByAppid).collect(Collectors.toList());
    }

    //TODO: move this to a cron job using amazon lambda or something
    private List<Game> saveAllGamesToDB() throws IOException {
        //clear the repo
        gameRepository.deleteAll();
        List<Game> gameList = new ArrayList<>();
        String gamesURI = "https://api.steampowered.com/ISteamApps/GetAppList/v2/?key=" + KEY;
        requestCreator.setURI(gamesURI);
        gameList = gsonParser.parseGameList(requestCreator.getAll());

        for (Game game : gameList) {
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
