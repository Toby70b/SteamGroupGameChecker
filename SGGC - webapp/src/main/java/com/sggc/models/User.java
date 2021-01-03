package com.sggc.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.ElementCollection;
import javax.persistence.Id;
import java.util.Set;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Document(collection = "User")
public class User {
    @NonNull
    @Id
    private String id;
    @ElementCollection
    @NonNull
    private Set<String> ownedGameIds;

}
