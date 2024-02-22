package com.example.SunriseAndSunsetService.service.Impl;

import com.example.SunriseAndSunsetService.exceptions.MyRuntimeException;
import com.example.SunriseAndSunsetService.model.SunriseData;
import com.example.SunriseAndSunsetService.repository.SunriseRepository;
import com.example.SunriseAndSunsetService.service.SunriseService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SunriseServiceImpl implements SunriseService {
    private final SunriseRepository repository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<SunriseData> findAllData() {
        return repository.findAll();
    }

    @SneakyThrows
    @Override
    public SunriseData findTime(Double lat, Double lng, LocalDate date) {
        if (!isValidLat(lat) || !isValidLng(lng))
            throw new MyRuntimeException("Wrong parameters.");
        String[] timezoneAndPlace = getTimezoneAndPlace(lat, lng);
        String url = "https://api.sunrise-sunset.org/json?lat=" + lat.toString() + "&lng=" + lng.toString()
                + "&date=" + date.toString() + "&formatted=0&tzid=" + timezoneAndPlace[0];
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (!response.getStatusCode().is2xxSuccessful())
            throw new MyRuntimeException("Converting failure.");
        String responseBody = response.getBody();
        JsonNode root = objectMapper.readTree(responseBody);
        SunriseData data = new SunriseData(lat, lng, date, timezoneAndPlace[1], LocalTime.parse(root.path("results").path("sunrise").asText().substring(11, 19)),
                LocalTime.parse(root.path("results").path("sunset").asText().substring(11, 19)));
        repository.save(data);
        return data;
    }

    @SneakyThrows
    private String[] getTimezoneAndPlace(Double lat, Double lng) {
        String url = "https://htmlweb.ru/json/geo/timezone/" + lat.toString() + "," + lng.toString();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (!response.getStatusCode().is2xxSuccessful())
            throw new MyRuntimeException("Wrong parameters.");
        String responseBody = response.getBody();
        JsonNode root = objectMapper.readTree(responseBody);
        return new String[]{root.path("name").asText(), root.path("city").path("name").asText()};
    }

    private Boolean isValidLat(Double lat){
        return lat >= -90 && lat <= 90;
    }

    private Boolean isValidLng(Double lng){
        return lng >= -180 && lng <= 180;
    }
}
