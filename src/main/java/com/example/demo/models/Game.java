package com.example.demo.models;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Game {
    @Id
    private int id;
    private String name;
    private Boolean multiplayer;

    public Game(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Game() {}

    public Boolean isMultiplayer() {
        return multiplayer;
    }

    public void setMultiplayer(Boolean multiplayer) {
        this.multiplayer = multiplayer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
