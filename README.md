# SteamGroupGameChecker - Tool to find out common steam games between users

 ⛔ NOTE: this still a work in progess, as I discover new things I go back and edit this, also please dont laugh at my code, I'm still learning 

## Rationale for development

I belong to a group of 5-6 20 something friends whose primary hobby is gaming, as such we all have steam accounts with each of us owning at least 200 games. We all prefer to play games together, we dont mind replaying games, including older games, if it means we can play together. We would occasionally ask what games we have in common, however since steam only allows users to compare their lists with one other user at a time this task was very tedious. I wanted more excuses to practice on REST API's so I researched Steam's API and once I discovered it was feasible to make a tool that could compare multiple users lists for common games I decided to make this tool.

## REST API

The REST API is a spring boot project (created using spring initializr) run using a Tomcat server. The API is exposed (by default) on port 8080, it currently only has one mapping, a Post mapping which takes a json object consisting of an array of ids which correspond to Steam User Id's (these are publicly available, either by using the Steam client or through the Steam API) 

An example of the body of the post request is:
```
{"steamIds" : [76561198045206229,76561198014258751,76561198171740181,76561198069846749]}
```

Upon success the API will return a json object consisting of game objects that contain the game' Id on the Steam db, the game's name and whether the game is multiplayer or not. The returned games are multiplyer games the users entered in the input have in common.

An example of the output of ther API is:
```
[
    {
        "id": 730,
        "name": "Counter-Strike: Global Offensive",
        "multiplayer": true
    }
]
```

## UI

The UI is made with React with components from the ant design UI framework, all running on an node.js server. The UI allows the user to enter a number of steam id's and search for the common games between the users, there is some simple validation preventing the user from entering invalid steam id's. I've also added some Qol features such allowing the user to remove users from the search list. Once a search is performed a new panel will appear that, upon success of the fetch will display a scrollable table of the users common games, this table will have two columns; one in which the image of the game will be displayed (pulled from steamcdn-a.akamaihd.net) and the second which will display the games name, this doubles as a link to the game's Steam store page.

## Things to improve

## Thanks to

