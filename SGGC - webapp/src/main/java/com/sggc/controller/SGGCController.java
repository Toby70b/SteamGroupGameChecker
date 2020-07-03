package com.sggc.controller;

import com.sggc.exception.UserHasNoGamesException;
import com.sggc.model.Game;
import com.sggc.model.Request;
import com.sggc.service.GameService;
import com.sggc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/sggc")
@RequiredArgsConstructor
public class SGGCController {

    private final GameService gameService;
    private final UserService userService;

    @CrossOrigin
    @PostMapping(value = "/")
    public ResponseEntity<List<Game>> getGamesAllUsersOwn(@Valid @RequestBody Request request) throws IOException, UserHasNoGamesException {
        Set<String> userSteamIds = request.getSteamIds();
        List<Integer> commonGameIds = userService.getIdsOfGamesOwnedByAllUsers(userSteamIds);
        List<Game> commonGames = gameService.getCommonGames(commonGameIds);
        return new ResponseEntity<>(commonGames, HttpStatus.OK);
    }


}
