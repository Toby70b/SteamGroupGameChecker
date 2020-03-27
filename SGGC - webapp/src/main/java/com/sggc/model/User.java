package com.sggc.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.ElementCollection;
import javax.persistence.Id;
import java.util.List;

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
    private List<Integer> ownedGameIds;

}
