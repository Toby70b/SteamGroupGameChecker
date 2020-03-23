package com.sggc.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Request {
    @NonNull
    private List<String> steamIds;
}
