package com.sggc.controller;

import com.sggc.exception.UserHasNoGamesException;
import com.sggc.model.Game;
import com.sggc.model.Request;
import com.sggc.model.User;
import com.sggc.service.GameService;
import com.sggc.service.UserService;
import com.sggc.util.GsonParser;
import com.sggc.util.HttpRequestCreator;
import com.sggc.util.RuntimeWrappablePredicateMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
