package com.app.demo.service;

import com.app.demo.model.User;
import com.app.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static customAssertions.UserAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final static String TOBY_STEAM_ID = "76561198045206229";

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void initUseCase() {
        userService = new UserService(userRepository);
    }

    @Test
    void savedUserHasCorrectDetailsSaved() {
        User user = new User(TOBY_STEAM_ID, Arrays.asList(1, 2, 3));
        when(userRepository.save(any(User.class))).then(returnsFirstArg());
        User savedUser = userService.save(user);
        assertThat(savedUser).hasDetails();

    }

    @Test
    void savedUserCanBeFoundSuccessfully() {
        User user = new User(TOBY_STEAM_ID, Arrays.asList(1, 2, 3));
        when(userRepository.findById(TOBY_STEAM_ID)).thenReturn(user);
        User foundUser = userService.findUserById(TOBY_STEAM_ID);
        assertThat(TOBY_STEAM_ID).isEqualTo(foundUser.getId());
        assertThat(Arrays.asList(1, 2, 3)).isEqualTo(foundUser.getOwnedGameIds());
    }
}
