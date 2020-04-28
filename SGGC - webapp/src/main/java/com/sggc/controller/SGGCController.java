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

@RestController
@RequestMapping("api/sggc")
@RequiredArgsConstructor
public class SGGCController {

    private final GameService gameService;
    private final UserService userService;

    @CrossOrigin
    @PostMapping(value = "/")
    public ResponseEntity<List<Game>> getGamesAllUsersOwn(@Valid @RequestBody Request request) throws IOException, UserHasNoGamesException {
        return new ResponseEntity<>(gameService.getCommonGames(userService.getIdsOfGamesOwnedByAllUsers(request.getSteamIds())), HttpStatus.OK);
    }


}
