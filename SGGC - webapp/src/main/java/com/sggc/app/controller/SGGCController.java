package com.sggc.app.controller;

import com.sggc.app.exception.UserHasNoGamesException;
import com.sggc.app.model.Game;
import com.sggc.app.model.Request;
import com.sggc.app.model.User;
import com.sggc.app.service.GameService;
import com.sggc.app.service.UserService;
import com.sggc.app.util.GsonParser;
import com.sggc.app.util.HttpRequestCreator;
import com.sggc.app.util.RuntimeWrappablePredicateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/sggc")
@RequiredArgsConstructor
public class SGGCController {

    private static final String GET_OWNED_GAMES_API_URI = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v1/?key=B88AF6D15A99EF5A4E01075EF63E5DF2&steamid=";
    private static final int MULTIPLAYER_ID = 1;
    private final GameService gameService;
    private final UserService userService;

    @CrossOrigin
    @PostMapping(value = "/")
    public ResponseEntity<List<Game>> getGamesAllUsersOwn(@RequestBody Request request) {
        List<String> userIds = request.getSteamIds();
        List<Integer> combinedGameIds = null;
        try {
            combinedGameIds = getIdsOfGamesOwnedByAllUsers(userIds);
        } catch (IOException | UserHasNoGamesException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
        List<Integer> combinedMultiplayerGameIds = removeNonMultiplayerGamesFromList(combinedGameIds);
        return new ResponseEntity<>(getCombinedGames(combinedMultiplayerGameIds), HttpStatus.OK);
    }

    private List<Integer> removeNonMultiplayerGamesFromList(List<Integer> combinedGameIds) {
        combinedGameIds = combinedGameIds.stream().filter(
                RuntimeWrappablePredicateMapper.wrap(this::isMultiplayer)
        ).collect(Collectors.toList());
        return combinedGameIds;
    }

    public  <T> T uncheckCall(Callable<T> callable) {
        try { return callable.call(); }
        catch (RuntimeException e) { throw e; }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    private boolean isMultiplayer(Integer gameId) throws IOException {
        Game game = gameService.findByAppid(gameId);
        if (game.getMultiplayer() != null) {
            return game.getMultiplayer();
        } else {
            //Make an api call for the gameId
            String URI = "http://store.steampowered.com/api/appdetails/?appids=" + gameId;
            //Create the request
            HttpRequestCreator requestCreator = new HttpRequestCreator(URI);
            //Parse the response to get list of categories
            List<Integer> categoryIds = new GsonParser().parseGameDetailsList(requestCreator.getAll());
            //Check for presence of multiplayer category
            for (int i : categoryIds) {
                if (i == MULTIPLAYER_ID) {
                    game.setMultiplayer(true);
                    gameService.save(game);
                    return true;
                }
            }
            game.setMultiplayer(false);
            gameService.save(game);
            return false;
        }
    }

    private List<Integer> getIdsOfGamesOwnedByAllUsers(List<String> userIds) throws IOException, UserHasNoGamesException {
        //for each other id entered make a get call to the steam api to get users owned games then remove from the combined list
        //any that dont appear in the new users list
        List<Integer> combinedGameIds = new ArrayList<>();
        for (String userId : userIds) {
            List<Integer> usersOwnedGameIds = findUsersGameIdsById(userId);
            if (combinedGameIds.isEmpty()) {
                combinedGameIds = usersOwnedGameIds;
            }
            combinedGameIds.removeIf(gameId -> !usersOwnedGameIds.contains(gameId));
        }
        return combinedGameIds;
    }

    private List<Integer> getUsersOwnedGameIds(String userId) throws IOException, UserHasNoGamesException {
        List<Integer> gameList;
        String gamesURI = GET_OWNED_GAMES_API_URI + userId;
        HttpRequestCreator requestCreator = new HttpRequestCreator(gamesURI);
        gameList = new GsonParser().parseUserGameList(requestCreator.getAll());
        return gameList;
    }

    private List<Game> getCombinedGames(List<Integer> gameIds) {
        List<Game> combinedGames = gameIds.stream().map(gameService::findByAppid).collect(Collectors.toList());
        return combinedGames;
    }

    private List<Integer> findUsersGameIdsById(String userId) throws IOException, UserHasNoGamesException {
        if (userService.findUserById(userId) != null) {
            return userService.findUserById(userId).getOwnedGameIds();
        } else {
            List<Integer> usersOwnedGameIds = null;
            try {
                usersOwnedGameIds = getUsersOwnedGameIds(userId);
            } catch (UserHasNoGamesException e) {
                e.setUserId(userId);
                throw e;
            }
            //Cache the user to speed up searches, in future this should be cleared out more than once daily
            userService.save(new User(userId, usersOwnedGameIds));
            return usersOwnedGameIds;
        }
    }
}
