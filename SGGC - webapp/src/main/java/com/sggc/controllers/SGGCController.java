package com.sggc.controllers;

import com.sggc.exceptions.UserHasNoGamesException;
import com.sggc.models.Game;
import com.sggc.models.Request;
import com.sggc.services.GameService;
import com.sggc.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("api/sggc")
@RequiredArgsConstructor
public class SGGCController {

    private final GameService gameService;
    private final UserService userService;

    @CrossOrigin
    @PostMapping(value = "/")
    public ResponseEntity<Set<Game>> getGamesAllUsersOwn(@Valid @RequestBody Request request) throws IOException, UserHasNoGamesException {
        Set<String> userSteamIds = request.getSteamIds();
        Set<Integer> commonGameIds = userService.getIdsOfGamesOwnedByAllUsers(userSteamIds);
        Set<Game> commonGames = gameService.getCommonGames(commonGameIds);
        return new ResponseEntity<>(commonGames, HttpStatus.OK);
    }


}
