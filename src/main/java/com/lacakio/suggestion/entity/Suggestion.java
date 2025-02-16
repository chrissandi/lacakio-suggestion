package com.lacakio.suggestion.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class Suggestion {
    private String name;
    private String latitude;
    private String longitude;
    private double score;
}
