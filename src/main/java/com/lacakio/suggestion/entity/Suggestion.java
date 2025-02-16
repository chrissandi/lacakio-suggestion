package com.lacakio.suggestion.entity;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class Suggestion {
    private String name;
    private String Latitude;
    private String Longitude;
    private double score;
}
