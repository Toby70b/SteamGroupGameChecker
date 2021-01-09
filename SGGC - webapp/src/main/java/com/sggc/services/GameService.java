package com.sggc.services;

import com.sggc.models.Game;
import com.sggc.models.GameCategory;
import com.sggc.models.GameData;
import com.sggc.models.GetAppListResponse;
import com.sggc.repositories.GameRepository;
import com.sggc.util.SteamRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sggc.util.CommonUtil.MULTIPLAYER_ID;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final SteamRequestHandler steamRequestHandler;

    public Set<String> removeNonMultiplayerGamesFromList(Set<String> gameIds) throws IOException {
        Set<String> set = new HashSet<>();
        for (String gameId : gameIds) {
            if (isGameMultiplayer(gameId)) {
                set.add(gameId);
            }
        }
        return set;
    }

    private boolean isGameMultiplayer(String gameId) throws IOException {
        Game game = gameRepository.findGameByAppId(gameId);
        if (game.getMultiplayer() != null) {
            return game.getMultiplayer();
        } else {

            String response = steamRequestHandler.requestAppDetailsFromSteamApi(gameId);
            GameData parsedResponse = steamRequestHandler.parseGameDetailsList(response);
            //Check for presence of multiplayer category
            for (GameCategory category : parsedResponse.getCategories()) {
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

    public Set<Game> getCommonGames(Set<String> gameIds, boolean multiplayerOnly) throws IOException {
        if(multiplayerOnly){
            gameIds = removeNonMultiplayerGamesFromList(gameIds);
        }
        return gameIds.stream().map(gameRepository::findGameByAppId).collect(Collectors.toSet());
    }

    //TODO: move this to a cron job using aws lambda or something
    public void saveAllGamesToDB() {
        //clear the repo
        gameRepository.deleteAll();
        GetAppListResponse response = steamRequestHandler.requestAllSteamAppsFromSteamApi();
        gameRepository.saveAll(response.getApplist().getApps());
    }


}


