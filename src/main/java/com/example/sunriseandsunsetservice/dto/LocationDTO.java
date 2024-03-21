package com.example.sunriseandsunsetservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LocationDTO {
    private String location;
    private Double latitude;
    private Double longitude;
}
