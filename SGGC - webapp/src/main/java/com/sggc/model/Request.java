package com.sggc.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Request {
    @NonNull
    @Size(min=2, message ="{com.sggc.lessThanTwoGameIds.message}")
    private List<@Pattern(regexp = "^\\d{17}$", message = "{com.sggc.invalidSteamId.message}") String> steamIds;
}
