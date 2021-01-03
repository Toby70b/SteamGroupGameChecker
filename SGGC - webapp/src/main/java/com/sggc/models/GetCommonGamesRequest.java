package com.sggc.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class GetCommonGamesRequest {
    @NonNull
    @Size(min = 2, message = "{com.sggc.lessThanTwoGameIds.message}")
    private Set<@Pattern(regexp = "^\\d{17}$", message = "{com.sggc.invalidSteamId.message}") String> steamIds;
}
