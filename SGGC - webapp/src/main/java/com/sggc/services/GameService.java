package com.sggc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.google.gson.*;
import com.sggc.models.Game;
import com.sggc.models.GameCategory;
import com.sggc.models.GameData;
import com.sggc.models.GetAppListResponse;
import com.sggc.repositories.GameRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    @Value("${steamapi.key}")
    private String key;

    @Value("${steamapi.endpoints.getAppListEndpoint}")
    private String getAppListEndpoint;

    @Value("${steamapi.endpoints.getAppDetailsEndpoint}")
    private String getAppDetailsEndpoint;


    private final GameRepository gameRepository;
    private final WebClient.Builder webClientBuilder;

    private final Logger logger = LoggerFactory.getLogger(GameService.class);
    private static final int MULTIPLAYER_ID = 1;

    public Set<String> removeNonMultiplayerGamesFromList(Set<String> gameIds) throws IOException {
        Set<String> set = new HashSet<>();
        for (String gameId : gameIds) {
            if (isGameMultiplayer(gameId)) {
                set.add(gameId);
            }
        }
        gameIds = set;
        return gameIds;
    }

    private boolean isGameMultiplayer(String gameId) throws IOException {
        Game game = gameRepository.findGameByAppId(gameId);
        if (game.getMultiplayer() != null) {
            return game.getMultiplayer();
        } else {
            String URI = getAppDetailsEndpoint + "?appids=" + gameId;
            logger.debug("Contacting " + URI + " to get details of game " + gameId);
            String response = requestAppDetailsFromSteamApi(gameId);
            GameData gameData = parseGameDetailsList(response);
            //Check for presence of multiplayer category
            for (GameCategory category : gameData.getCategories()) {
                if (category.getId() == MULTIPLAYER_ID) {
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

    public Set<Game> getCommonGames(Set<String> gameIds) throws IOException {
        return removeNonMultiplayerGamesFromList(gameIds).stream().map(gameRepository::findGameByAppId).collect(Collectors.toSet());
    }

    //TODO: move this to a cron job using aws lambda or something
    public void saveAllGamesToDB() {
        //clear the repo
        gameRepository.deleteAll();
        GetAppListResponse response = requestAllSteamAppsFromSteamApi();
        gameRepository.saveAll(response.getApplist().getApps());
    }

    private GetAppListResponse requestAllSteamAppsFromSteamApi() {
        return webClientBuilder.build().get()
                .uri(getAppListEndpoint, uriBuilder -> uriBuilder
                        .queryParam("key", key)
                        .build())
                .retrieve()
                .bodyToMono(GetAppListResponse.class)
                .block();
    }

    private String requestAppDetailsFromSteamApi(String appId) {
        return webClientBuilder.build().get()
                .uri(getAppDetailsEndpoint, uriBuilder -> uriBuilder
                        .queryParam("appids", appId)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public GameData parseGameDetailsList(String stringToParse) throws IOException {
        Gson gson = new Gson();
        JsonElement jsonTree = parseResponseStringToJson(stringToParse);
        JsonObject obj = jsonTree.getAsJsonObject();
        // The root of the response is a id of the game thus get the responses root value
        String gameId = obj.keySet().iterator().next();
        obj = obj.getAsJsonObject(gameId);
        boolean responseSuccess = Boolean.parseBoolean(obj.get("success").toString());
        /*
        Sometimes steam no longer has info on the Game Id e.g. 33910 ARMA II, this is probably because the devs of the games
        in question may have created a new steam product for the exact same game (demo perhaps?), so to avoid crashing if the game no longer
        has any details, we'll pass it through as a multiplayer game, better than excluding games that could be multiplayer
        */
        if (!responseSuccess) {
            return new GameData(Collections.singleton(new GameCategory(MULTIPLAYER_ID)));
        }
        obj = obj.getAsJsonObject("data");
        return gson.fromJson(obj.toString(), GameData.class);
    }

    public JsonElement parseResponseStringToJson(String stringToParse) throws IOException {
        try {
            return JsonParser.parseString(stringToParse);

        } catch (JsonSyntaxException e) {
            throw new IOException("Error when parsing response string into JSON object, this is likely due an invalid user id", e);
        }
    }
}


