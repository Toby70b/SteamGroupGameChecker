package com.sggc.services;

import com.google.api.client.util.Value;
import com.sggc.exceptions.UserHasNoGamesException;
import com.sggc.models.User;
import com.sggc.repositories.UserRepository;
import com.sggc.util.GsonParser;
import com.sggc.util.HttpRequestCreator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("steamapi.key")
    private static String KEY;

    @Value("steamapi.endpoints.getOwnedGamesEndpoint")
    private static String GET_OWNED_GAMES_API_URI;

    private final UserRepository userRepository;
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    public Set<Integer> findOwnedGamesByUserId(String userId) throws IOException, UserHasNoGamesException {
        logger.debug("Attempting to find user with id: " + userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            logger.debug("User with matching id has been found in Mongo Repo");
            return user.get().getOwnedGameIds();
        } else {
            logger.debug("User with matching id hasnt been found in Mongo Repo, will request details from Steam API");
            Set<Integer> usersOwnedGameIds;
            try {
                usersOwnedGameIds = getUsersOwnedGameIds(userId);
                /*
                    Cache the user to speed up searches. in a proper prod environment this would be cleaned regularly
                    to catch changes in users owned games
                */
                userRepository.save(new User(userId, usersOwnedGameIds));
                return usersOwnedGameIds;
            } catch (UserHasNoGamesException e) {
                e.setUserId(userId);
                throw e;
            }
        }
    }

    public Set<Integer> getIdsOfGamesOwnedByAllUsers(Set<String> userIds) throws IOException, UserHasNoGamesException {
        Set<Integer> combinedGameIds = new HashSet<>();
        for (String userId : userIds) {
            Set<Integer> usersOwnedGameIds = findOwnedGamesByUserId(userId);
            if (combinedGameIds.isEmpty()) {
                combinedGameIds = usersOwnedGameIds;
            } else {
                combinedGameIds.retainAll(usersOwnedGameIds);
            }
        }
        return combinedGameIds;
    }

    private Set<Integer> getUsersOwnedGameIds(String userId) throws IOException, UserHasNoGamesException {
        Set<Integer> gameList;
        String gamesURI = GET_OWNED_GAMES_API_URI+"?key=" + KEY + "&steamid=" + userId;
        logger.debug("Contacting " + gamesURI + " to get owned games of user " + userId);
        HttpRequestCreator requestCreator = new HttpRequestCreator(gamesURI);
        gameList = new GsonParser().parseUserGameList(requestCreator.getAll());
        return gameList;
    }

}
