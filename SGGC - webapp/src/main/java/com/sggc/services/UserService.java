package com.sggc.services;

import com.sggc.exceptions.UserHasNoGamesException;
import com.sggc.models.Game;
import com.sggc.models.GetOwnedGamesResponseDetails;
import com.sggc.models.User;
import com.sggc.repositories.UserRepository;
import com.sggc.util.SteamRequestHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    private final SteamRequestHandler steamRequestHandler;

    public Set<String> findOwnedGamesByUserId(String userId) throws UserHasNoGamesException {
        logger.debug("Attempting to find user with id: " + userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            logger.debug("User with matching id has been found in Mongo Repo");
            return user.get().getOwnedGameIds();
        } else {
            logger.debug("User with matching id hasnt been found in Mongo Repo, will request details from Steam API");
            Set<String> usersOwnedGameIds;
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

    public Set<String> getIdsOfGamesOwnedByAllUsers(Set<String> userIds) throws UserHasNoGamesException {
        Set<String> combinedGameIds = new HashSet<>();
        for (String userId : userIds) {
            Set<String> usersOwnedGameIds = findOwnedGamesByUserId(userId);
            if (combinedGameIds.isEmpty()) {
                combinedGameIds = usersOwnedGameIds;
            } else {
                combinedGameIds.retainAll(usersOwnedGameIds);
            }
        }
        return combinedGameIds;
    }

    private Set<String> getUsersOwnedGameIds(String userId) throws UserHasNoGamesException {
        Set<String> gameIdList;
        GetOwnedGamesResponseDetails response = steamRequestHandler.requestUsersOwnedGamesFromSteamApi(userId).getResponse();
        if(response.getGameCount()==0){
            throw new UserHasNoGamesException();
        }
        gameIdList = parseGameIdsFromResponse(response);
        return gameIdList;
    }

    private Set<String> parseGameIdsFromResponse(GetOwnedGamesResponseDetails response) {
        return response.getGames()
                .stream()
                .map(Game::getAppid)
                .collect(Collectors.toSet());
    }
}
