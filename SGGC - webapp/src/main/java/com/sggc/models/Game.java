package com.sggc.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigInteger;
import java.util.Objects;

@Data
@RequiredArgsConstructor
@Document(collection = "Game")
public class Game {
    @JsonIgnore
    @Id
    private BigInteger id;
    @JsonProperty("appid")
    private String appid;
    private String name;
    @JsonIgnore
    private Boolean multiplayer;


}

