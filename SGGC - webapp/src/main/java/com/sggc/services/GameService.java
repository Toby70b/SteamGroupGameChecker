package com.sggc.services;

import com.sggc.models.Game;
import com.sggc.repositories.GameRepository;
import com.sggc.util.GsonParser;
import com.sggc.util.HttpRequestCreator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    @NonNull
    private final GameRepository gameRepository;
    private GsonParser gsonParser = new GsonParser();
    private static final String KEY = "B88AF6D15A99EF5A4E01075EF63E5DF2";
    private HttpRequestCreator requestCreator = new HttpRequestCreator("");
    private Logger logger = LoggerFactory.getLogger(GameService.class);
    private static final int MULTIPLAYER_ID = 1;

    public Set<Integer> removeNonMultiplayerGamesFromList(Set<Integer> gameIds) {
        gameIds = gameIds.stream().filter(
                gameId -> {
                    try {
                        return isGameMultiplayer(gameId);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
        ).collect(Collectors.toSet());
        return gameIds;
    }

    private boolean isGameMultiplayer(Integer gameId) throws IOException {
        Game game = gameRepository.findGameByAppId(gameId);
        if (game.getMultiplayer() != null) {
            return game.getMultiplayer();
        } else {
            //Make an api call for the gameId
            String URI = "http://store.steampowered.com/api/appdetails/?appids=" + gameId;
            logger.debug("Contacting " + URI + " to get details of game " + gameId);
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

    public Set<Game> getCommonGames(Set<Integer> gameIds) {
        return removeNonMultiplayerGamesFromList(gameIds).stream().map(gameRepository::findGameByAppId).collect(Collectors.toSet());
    }

    //TODO: move this to a cron job using amazon lambda or something
    public Set<Game> saveAllGamesToDB() throws IOException {
        //clear the repo
        gameRepository.deleteAll();
        Set<Game> gameList;
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
        return gameRepository.findGameByAppId(appid);
    }
}
