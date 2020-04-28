package com.sggc.service;

import com.sggc.exception.UserHasNoGamesException;
import com.sggc.model.User;
import com.sggc.repository.UserRepository;
import com.sggc.util.GsonParser;
import com.sggc.util.HttpRequestCreator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String KEY = "B88AF6D15A99EF5A4E01075EF63E5DF2";
    private static final String GET_OWNED_GAMES_API_URI = "https://api.steampowered.com/IPlayerService/GetOwnedGames/v1/?key="+KEY+"&steamid=";

    private final UserRepository userRepository;
    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @NonNull
    private final GameService gameService;


    public List<Integer> findUsersGameIdsById(String userId) throws IOException, UserHasNoGamesException {
        LOGGER.debug("Attempting to find user with id: " + userId);
        if (userRepository.findUserByid(userId) != null) {
            LOGGER.debug("User with matching id has been found in Mongo Repo");
            return userRepository.findUserByid(userId).getOwnedGameIds();
        } else {
            LOGGER.debug("User with matching id hasnt been found in Mongo Repo, will request details from Steam API");
            List<Integer> usersOwnedGameIds = null;
            try {
                usersOwnedGameIds = getUsersOwnedGameIds(userId);
            } catch (UserHasNoGamesException e) {
                e.setUserId(userId);
                throw e;
            }
            //Cache the user to speed up searches, in future this should be cleared out more than once daily
            userRepository.save(new User(userId, usersOwnedGameIds));
            return usersOwnedGameIds;
        }
    }

    public List<Integer> getIdsOfGamesOwnedByAllUsers(List<String> userIds) throws IOException, UserHasNoGamesException {
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
        LOGGER.debug("Contacting "+gamesURI+" to get owned games of user "+userId);
        HttpRequestCreator requestCreator = new HttpRequestCreator(gamesURI);
        gameList = new GsonParser().parseUserGameList(requestCreator.getAll());
        return gameList;
    }


    public User save(User user) {
        userRepository.save(user);
        return user;
    }

    public User findUserById(String id) {
        return userRepository.findUserByid(id);
    }
}
