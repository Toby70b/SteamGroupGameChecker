package com.sggc.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class GameData {
    private final Set<GameCategory> categories;
}
