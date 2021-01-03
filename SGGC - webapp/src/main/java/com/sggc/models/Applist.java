package com.sggc.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class Applist {
    private Set<Game> apps;
}
