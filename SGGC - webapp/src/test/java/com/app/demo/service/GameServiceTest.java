package com.app.demo.service;

import com.app.demo.model.Game;
import com.app.demo.repository.GameRepository;
import com.app.demo.util.GsonParser;
import com.app.demo.util.HttpRequestCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static customAssertions.GameAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    private static final int ID = 100;
    private static final List<Game> GAME_LIST_RESPONSE = Arrays.asList(new Game(1101190, "Dungeon Defenders: Awakened"),
            new Game(1122100, "Giraffe and Annika"),
            new Game(1053190, "Lover Bands"));
    @Mock
    private GameRepository gameRepository;
    @Mock
    private HttpRequestCreator requestCreator;
    @Mock
    private GsonParser gsonParser;
    @InjectMocks
    private GameService gameService;

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
    void savedGameCanBeFoundSuccessfully() {
        Game game = new Game(ID, "name");
        when(gameRepository.findGameByAppid(ID)).thenReturn(game);
        Game foundGame = gameService.findByAppid(ID);
        assertThat(ID).isEqualTo(foundGame.getId());
        assertThat("name").isEqualTo(foundGame.getName());
    }
}

