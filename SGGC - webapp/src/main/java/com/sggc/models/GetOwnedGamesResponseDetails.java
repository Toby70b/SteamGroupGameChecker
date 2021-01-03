package com.sggc.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class GetOwnedGamesResponseDetails {
    private int game_count;
    private Set<Game> games;
}
