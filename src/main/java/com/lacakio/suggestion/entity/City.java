package com.lacakio.suggestion.entity;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Getter @Setter
public class City {
    private String id;
    private String name;
    private String ascii;
    private String altName;
    private double longitude;
    private double latitude;
    private String admin1;
    private String country;
}
