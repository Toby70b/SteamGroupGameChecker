package com.sggc.services;

import com.sggc.models.Game;
import com.sggc.models.GameCategory;
import com.sggc.models.GameData;
import com.sggc.repositories.GameRepository;
import com.sggc.util.SteamRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.sggc.util.CommonUtil.MULTIPLAYER_ID;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final SteamRequestHandler steamRequestHandler;

    private boolean isGameMultiplayer(Game game) throws IOException {
        if (game.getMultiplayer() != null) {
            return game.getMultiplayer();
        } else {

            String response = steamRequestHandler.requestAppDetailsFromSteamApi(game.getAppid());

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

    public Set<Game> getCommonGames(Set<String> gameIds, boolean multiplayerOnly) {
        //Sometimes games have been removed from steam but still appear in users game libraries, thus remove nulls from the list.
        Set<Game> commonGames =  gameIds.stream().map(gameRepository::findGameByAppid).filter(Objects::nonNull).collect(Collectors.toSet());
        if(multiplayerOnly){
            commonGames = commonGames.stream().filter(game -> {
                try {
                    return isGameMultiplayer(game);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }).collect(Collectors.toSet());
        }
        return commonGames;
    }


}


