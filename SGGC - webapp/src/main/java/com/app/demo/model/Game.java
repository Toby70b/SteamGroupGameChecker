package com.app.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Document(collection = "Game")
public class Game {
    @Id
    private BigInteger id;
    @NonNull
    private int appid;
    @NonNull
    private String name;

    private Boolean multiplayer;
}

