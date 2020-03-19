package com.app.demo.model;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class User {
    @Id
    private String id;
    @ElementCollection
    private List<Integer> ownedGameIds;

    protected User() {
    }

    public User(String id, List<Integer> ownedGameIds) {
        this.id = id;
        this.ownedGameIds = ownedGameIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getOwnedGameIds() {
        return ownedGameIds;
    }

    public void setOwnedGameIds(List<Integer> ownedGameIds) {
        this.ownedGameIds = ownedGameIds;
    }
}
