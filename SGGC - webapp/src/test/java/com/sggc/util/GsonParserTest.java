package com.sggc.util;

import com.sggc.exception.UserHasNoGamesException;
import com.sggc.model.Game;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class GsonParserTest {
    private static final String ALL_GAMES_LIST_RESPONSE_EXAMPLE = "{\"applist\":{\"apps\":[{\"appid\":1101190,\"name\":\"Dungeon Defenders: Awakened\"},{\"appid\":1122100,\"name\":\"Giraffe and Annika\"},{\"appid\":1053190,\"name\":\"Lover Bands\"}]}}";
    private static final List<Game> ALL_GAMES_LIST_EXPECTED_RESULT = Arrays.asList(new Game(1101190, "Dungeon Defenders: Awakened"),
            new Game(1122100, "Giraffe and Annika"),
            new Game(1053190, "Lover Bands"));
    private static final String TESTING_STEAM_ID = "76561198020205224";
    private static final String TESTING_USER_OWNED_GAME_IDS_RESPONSE_EXAMPLE = "{\"response\":{\"game_count\":28,\"games\":[{\"appid\":550,\"playtime_forever\":892,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":22370,\"playtime_forever\":1054,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":33910,\"playtime_forever\":10,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":33930,\"playtime_forever\":894,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":219540,\"playtime_forever\":1071,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":34330,\"playtime_forever\":1151,\"playtime_windows_forever\":23,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":730,\"playtime_forever\":911,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":49520,\"playtime_forever\":6230,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":205790,\"playtime_forever\":0,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":209870,\"playtime_forever\":824,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":227480,\"playtime_forever\":116,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":242760,\"playtime_forever\":793,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":252410,\"playtime_forever\":1102,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":253980,\"playtime_forever\":318,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":245620,\"playtime_2weeks\":10,\"playtime_forever\":95,\"playtime_windows_forever\":10,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":8930,\"playtime_forever\":278,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":291480,\"playtime_forever\":71,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":227940,\"playtime_forever\":0,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":321040,\"playtime_forever\":19,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":261640,\"playtime_forever\":2334,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":333930,\"playtime_forever\":672,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":325610,\"playtime_forever\":0,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":377160,\"playtime_forever\":4490,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":236110,\"playtime_forever\":3,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":364360,\"playtime_forever\":498,\"playtime_windows_forever\":71,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":292730,\"playtime_forever\":174,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":550040,\"playtime_forever\":117,\"playtime_windows_forever\":0,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0},{\"appid\":629760,\"playtime_2weeks\":35,\"playtime_forever\":804,\"playtime_windows_forever\":290,\"playtime_mac_forever\":0,\"playtime_linux_forever\":0}]}}";
    private static final List<Integer> TESTING_USER_OWNED_GAME_IDS_EXPECTED_RESULT = Arrays.asList(550, 22370, 33910, 33930, 219540, 34330, 730, 49520, 205790, 209870, 227480, 242760, 252410, 253980
            , 245620, 8930, 291480, 227940, 321040, 261640, 333930, 325610, 377160, 236110, 364360, 292730, 550040, 629760);
    private GsonParser gsonParser;

    @BeforeEach
    void initUseCase() {
        gsonParser = new GsonParser();
    }

    @Test
    public void parseStringOfAllGamesIntoListOfGames() {
        List<Game> actualResult = gsonParser.parseGameList(ALL_GAMES_LIST_RESPONSE_EXAMPLE);
        for (int i = 0; i > actualResult.size(); i++) {
            Assert.assertEquals(actualResult.get(i).getId(), ALL_GAMES_LIST_EXPECTED_RESULT.get(i).getId());
            Assert.assertEquals(actualResult.get(i).getName(), ALL_GAMES_LIST_EXPECTED_RESULT.get(i).getName());
            Assert.assertEquals(actualResult.get(i).getMultiplayer(), ALL_GAMES_LIST_EXPECTED_RESULT.get(i).getMultiplayer());
        }
    }

    @Test
    public void parseStringOfUsersOwnedGamesIntoListOfGames() throws UserHasNoGamesException, IOException {
        Assert.assertEquals(TESTING_USER_OWNED_GAME_IDS_EXPECTED_RESULT,
                gsonParser.parseUserGameList(TESTING_USER_OWNED_GAME_IDS_RESPONSE_EXAMPLE));
    }

    @Test
    public void parseStringOfGameDetailsWhenAllGamesEnteredHaveDetailsAvailable() {

    }

    @Test
    public void parseStringOfGameDetailsWhenSomeGamesEnteredDoNotHaveDetailsAvailable() {

    }
}
