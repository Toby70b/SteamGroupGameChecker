package com.sggc.app.util;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sggc.app.exception.UserHasNoGamesException;
import com.sggc.app.model.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GsonParser {
    private static final int MULTIPLAYER_ID = 1;
    private List<Game> gameList;
    private ArrayList<Integer> gameIdList;

    public List<Game> parseGameList(String stringToParse) {
        JsonElement jsonTree = JsonParser.parseString(stringToParse);
        JsonObject obj = jsonTree.getAsJsonObject();
        obj = obj.getAsJsonObject("applist");
        JsonElement apps = obj.get("apps");

        Iterator<JsonElement> iterator = apps.getAsJsonArray().iterator();

        gameList = new ArrayList();
        while (iterator.hasNext()) {
            JsonElement element = iterator.next();
            Game game = new Game(element.getAsJsonObject().get("appid").getAsInt(), element.getAsJsonObject().get("name").getAsString());
            gameList.add(game);
        }
        return gameList;
    }

    public List<Integer> parseUserGameList(String stringToParse) throws UserHasNoGamesException, IOException {
        JsonElement jsonTree = null;

        try {
            jsonTree = JsonParser.parseString(stringToParse);
        } catch (JsonSyntaxException e) {
            throw new IOException(e);
        }

        JsonObject obj = jsonTree.getAsJsonObject().getAsJsonObject("response");

        JsonElement games = obj.get("games");

        if (games == null) {
            throw new UserHasNoGamesException();
        }

        Iterator<JsonElement> iterator = games.getAsJsonArray().iterator();

        gameIdList = new ArrayList<Integer>();
        while (iterator.hasNext()) {
            JsonElement element = iterator.next();
            gameIdList.add(element.getAsJsonObject().get("appid").getAsInt());
        }
        return gameIdList;
    }

    public List<Integer> parseGameDetailsList(String stringToParse) throws IOException {
        JsonElement jsonTree = null;
        try {
            jsonTree = JsonParser.parseString(stringToParse);
        } catch (JsonSyntaxException e) {
            throw new IOException(e);
        }

        JsonObject obj = jsonTree.getAsJsonObject();
        // The root of the response is a id of the game thus get the responses root value
        String gameId = obj.keySet().iterator().next();
        obj = obj.getAsJsonObject(gameId);
          /*
            Sometimes steam no longer has info on the Game Id e.g. 33910 ARMA II, this is probably because the devs of the games
            in question messed up and created a new steam product for the exact same game, so to avoid crashing if the game no longer
            has any details, we'll pass it through as a multiplayer game, better than excluding games that could be multiplayer
        */
        if (!(Boolean.parseBoolean(obj.get("success").toString()))) {
            return Arrays.asList(MULTIPLAYER_ID);
        }

        obj = obj.getAsJsonObject("data");
        JsonElement categories = obj.get("categories");
        Iterator<JsonElement> iterator = categories.getAsJsonArray().iterator();

        List<Integer> categoryIdsList = new ArrayList<Integer>();
        while (iterator.hasNext()) {
            JsonElement element = iterator.next();
            categoryIdsList.add(element.getAsJsonObject().get("id").getAsInt());
        }
        return categoryIdsList;
    }
}