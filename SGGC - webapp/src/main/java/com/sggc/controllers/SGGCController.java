package com.sggc.controllers;

import com.sggc.exceptions.UserHasNoGamesException;
import com.sggc.models.Game;
import com.sggc.models.GetCommonGamesRequest;
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
    public ResponseEntity<Set<Game>> getGamesAllUsersOwn(@Valid @RequestBody GetCommonGamesRequest request) throws IOException, UserHasNoGamesException {
        Set<String> steamUserIds = request.getSteamIds();
        Set<String> commonGameIdsBetweenUsers = userService.getIdsOfGamesOwnedByAllUsers(steamUserIds);
        Set<Game> commonGames = gameService.getCommonGames(commonGameIdsBetweenUsers,request.isMultiplayerOnly());
        return new ResponseEntity<>(commonGames, HttpStatus.OK);
    }

    //Endpoint to populate db, Obvisoly not how it should be done in a real prod env but I'm okay for it to be in this project
    @CrossOrigin
    @GetMapping(value = "/populatedb/")
    public ResponseEntity<String> populateDB(){
        gameService.saveAllGamesToDB();
        return new ResponseEntity<>("Database successfully updated", HttpStatus.OK);
    }


}
