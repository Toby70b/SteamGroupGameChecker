package com.example.demo.controllers;

import lombok.RequiredArgsConstructor;
import com.example.demo.models.Game;
import com.example.demo.models.User;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.GameService;
import com.example.demo.services.UserService;
import com.example.demo.utils.GsonParser;
import com.example.demo.utils.HttpRequestCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@ComponentScan(basePackages = {"com.example.demo.services"} )
@RequiredArgsConstructor
public class UserController {

    private static final String KEY = "B88AF6D15A99EF5A4E01075EF63E5DF2";
    private static final int MULTIPLAYER_ID = 1;
    private final GameService gameService;
    private final UserService userService;

    @CrossOrigin
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Game>> getGamesAllUsersOwn(@RequestBody Map<String,List<String>> request) throws IOException {
        try {
            List<String> userIds = request.get("steamIds");
            //Create combined list from first users games list then remove them from the list so not to process them again
            List<Integer> combinedGameIds = getUsersOwnedGameIds(userIds.get(0));
            userIds.remove(0);
            combinedGameIds = getIdsOfGamesOwnedByAllUsers(combinedGameIds, userIds);
            List<Integer> combinedMultiplayerGameIds = removeNonMultiplayerGamesFromList(combinedGameIds);
            return new ResponseEntity<>(getCombinedGames(combinedMultiplayerGameIds), HttpStatus.OK);
        }
        catch (IOException e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Integer> removeNonMultiplayerGamesFromList(List<Integer> combinedGameIds) {
        combinedGameIds = combinedGameIds.stream().filter(gameId -> {
            try {
                return isMultiplayer(gameId);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        return combinedGameIds;
    }

    private boolean isMultiplayer(Integer gameId) throws IOException {
        Game game = gameService.findGameById(gameId);


        if(game.isMultiplayer()!=null){
            return game.isMultiplayer();
        }
        else{
            //Make an api call for the gameId
            String URI = "http://store.steampowered.com/api/appdetails/?appids="+gameId;
            //Create the request
            HttpRequestCreator requestCreator = new HttpRequestCreator(URI);
            //Parse the response to get list of categories
            List<Integer> categoryIds = new GsonParser().parseGameDetailsList(requestCreator.getAll());
            //Check for presence of multiplayer category
            for(int i : categoryIds){
                if(i == MULTIPLAYER_ID){
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

    private List<Integer> getIdsOfGamesOwnedByAllUsers(List<Integer> combinedGameIds, List<String> userIds) throws IOException {
        //for each other id entered make a get call to the steam api to get users owned games then remove from the combined list
        //any that dont appear in the new users list
        List<Integer> combinedGameIdsCopy = new ArrayList<>(combinedGameIds);
        for (String userId : userIds) {
            //If the users owned game ids have been saved, get them from the repo, dont make another api call
            if(userService.findUserById(userId)!=null){
                List<Integer> usersOwnedGameIds = userService.findUserById(userId).getOwnedGameIds();
                combinedGameIdsCopy.removeIf(gameId -> !usersOwnedGameIds.contains(gameId));
            }
            else {
                List<Integer> usersOwnedGameIds = getUsersOwnedGameIds(userId);
                combinedGameIdsCopy.removeIf(gameId -> !usersOwnedGameIds.contains(gameId));
                // save the user to the repo to save on api calls
                userService.save(new User(userId,usersOwnedGameIds));
            }
        }
        return combinedGameIdsCopy;
    }

    private List<Integer> getUsersOwnedGameIds(String userId) throws IOException {
        List<Integer> gameList;
        String gamesURI = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v1/?key="+ KEY +"&steamid="+userId;
        HttpRequestCreator requestCreator = new HttpRequestCreator(gamesURI);
        gameList = new GsonParser().parseUserGameList(requestCreator.getAll());
        return gameList;
    }

    private List<Game> getCombinedGames(List<Integer> gameIds){
        List<Game> combinedGames = new ArrayList<>();
        for (int gameId: gameIds
        ) {
            combinedGames.add(gameService.findGameById(gameId));
        }
        return combinedGames;
    }
}
