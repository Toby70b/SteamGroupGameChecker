package com.example.demo.services;

import com.example.demo.models.Game;
import static customAssertions.GameAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.repositories.GameRepository;
import com.example.demo.utils.GsonParser;
import com.example.demo.utils.HttpRequestCreator;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

     @Mock
     private GameRepository gameRepository;
     @Mock
     private HttpRequestCreator requestCreator;
     @Mock
     private GsonParser gsonParser;
     @InjectMocks
     private GameService gameService;
     private static final int ID = 100;
     private static final List<Game> GAME_LIST_RESPONSE = Arrays.asList(new Game(1101190,"Dungeon Defenders: Awakened"),
                                                                       new Game(1122100,"Giraffe and Annika"),
                                                                       new Game(1053190,"Lover Bands"));

     @BeforeEach
     void initUseCase() {
          gameService = new GameService(gameRepository);
     }

     @Test
     void saveMultipleGamesToDbWorkCorrectly() throws IOException {

          final ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
          initMocks(this);
          when(requestCreator.getAll()).thenReturn("");
          when(gsonParser.parseGameList(any(String.class))).thenReturn(GAME_LIST_RESPONSE);
          assertThat(gameService.saveAllGamesToDB()).isEqualTo(GAME_LIST_RESPONSE);
     }

     @Test
     void savedGameHasCorrectDetailsSaved() {
          Game game = new Game(ID, "name");
          when(gameRepository.save(any(Game.class))).then(returnsFirstArg());
          Game savedGame = gameService.save(game);
          assertThat(game).hasDetails();
     }

     @Test
     void savedGameCanBeFoundSuccessfully(){
          Game game = new Game(ID, "name");
          when(gameRepository.findById(ID)).thenReturn(game);
          Game foundGame = gameService.findGameById(ID);
          assertThat(ID).isEqualTo(foundGame.getId());
          assertThat("name").isEqualTo(foundGame.getName());
     }
}

