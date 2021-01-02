package com.sggc.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @Id
    private BigInteger id;
    @NonNull
    private int appId;
    @NonNull
    private String name;
    @JsonIgnore
    private Boolean multiplayer;
}

