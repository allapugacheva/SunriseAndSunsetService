package com.example.sunriseandsunsetservice.service;

import com.example.sunriseandsunsetservice.exceptions.MyRuntimeException;
import com.example.sunriseandsunsetservice.model.DateModel;
import com.example.sunriseandsunsetservice.model.LocationModel;
import com.example.sunriseandsunsetservice.model.TimeModel;
import com.example.sunriseandsunsetservice.model.TimezoneModel;
import com.example.sunriseandsunsetservice.repository.DateRepository;
import com.example.sunriseandsunsetservice.repository.LocationRepository;
import com.example.sunriseandsunsetservice.repository.TimeRepository;
import com.example.sunriseandsunsetservice.repository.TimezoneRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Component
@AllArgsConstructor
public class CommonService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TimeRepository timeRepository;
    private final DateRepository dateRepository;
    private final LocationRepository locationRepository;
    private final TimezoneRepository timezoneRepository;

    @SneakyThrows
    public String[] getTimezoneAndPlace(Double lat, Double lng) {

        String url = "https://htmlweb.ru/json/geo/timezone/" + lat.toString() + "," + lng.toString();

        JsonNode root = objectMapper.readTree(restTemplate.getForEntity(url, String.class).getBody());

        return new String[]{root.path("name").asText(), root.path("city").path("name").asText()};
    }

    public boolean notValidLat(Double lat) {
        return lat < -90 || lat > 90;
    }

    public boolean notValidLng(Double lng) {
        return lng < -180 || lng > 180;
    }

    @SneakyThrows
    public TimeModel getSunriseAndSunsetTime(Double lat, Double lng, LocalDate date, String timezone) {

        String url = "https://api.sunrise-sunset.org/json?lat=" + lat + "&lng=" + lng
                + "&date=" + date + "&formatted=0&tzid=" + timezone;

        JsonNode root = objectMapper.readTree(restTemplate.getForEntity(url, String.class).getBody());

        String[] sunriseAndSunsetTime = new String[]{root.path("results").path("sunrise").asText().substring(11, 19),
                root.path("results").path("sunset").asText().substring(11, 19)};

        TimeModel timeModel = timeRepository.findBySunriseTimeAndSunsetTime(LocalTime.parse(sunriseAndSunsetTime[0]),
                LocalTime.parse(sunriseAndSunsetTime[1]));
        if(timeModel == null)
            timeModel = timeRepository.save(new TimeModel(LocalTime.parse(sunriseAndSunsetTime[0]),
                    LocalTime.parse(sunriseAndSunsetTime[1])));

        return timeModel;
    }

    public TimeModel getCommonTime(DateModel dateModel, LocationModel locationModel) {
        return locationModel.getTimes().stream()
                .filter(dateModel.getTimes()::contains)
                .findFirst().orElse(null);
    }

    public void clearTime(DateModel dm, LocationModel lm, TimeModel tm) {
        dm.deleteTime(tm);
        lm.deleteTime(tm);
        tm.deleteDate(dm);
        tm.deleteLocation(lm);

        if(tm.getLocations().isEmpty() && tm.getDates().isEmpty())
            timeRepository.delete(tm);
    }

    public void updateDate(Integer id, LocalDate date) {
        DateModel dateModel = dateRepository.findById(id).orElseThrow(
                () -> new MyRuntimeException("Wrong id."));

        boolean flagDeleteDate = true;

        DateModel dm;
        if ((dm = dateRepository.findByDate(date)) == null) {
            dateModel.setDate(date);

            flagDeleteDate = false;
        }

        for (LocationModel lm : dateModel.getLocations()) {

            clearTime(dateModel, lm, getCommonTime(dateModel, lm));

            TimeModel timeModel = getSunriseAndSunsetTime(lm.getLatitude(),
                    lm.getLongitude(), dateModel.getDate(), lm.getTimezone().getTimezone());

            if (flagDeleteDate) {
                lm.addDate(dm);
                dm.addLocation(lm);
            }

            lm.addTime(timeModel);
            timeModel.addLocation(lm);

            if (flagDeleteDate) {
                dm.addTime(timeModel);
                timeModel.addDate(dm);
            } else {
                dateModel.addTime(timeModel);
                timeModel.addDate(dateModel);
            }
        }

        if(flagDeleteDate) {
            dateRepository.delete(dateModel);
            dateRepository.save(dm);
        } else
            dateRepository.save(dateModel);
    }

    public String updateLocation(Integer id, Double lat, Double lng) {
        LocationModel locationModel = locationRepository.findById(id).orElseThrow(
                () -> new MyRuntimeException("Wrong id."));

        boolean flagDeleteLocation = true;

        LocationModel lm;
        TimezoneModel tm;
        if ((lm = locationRepository.findByLatitudeAndLongitude(lat, lng)) == null) {
            String[] timezoneAndPlace = getTimezoneAndPlace(lat, lng);

            if ((tm = timezoneRepository.findByTimezone(timezoneAndPlace[0])) == null)
                tm = timezoneRepository.save(new TimezoneModel(timezoneAndPlace[0]));

            tm.addLocation(locationModel);
            locationModel.setTimezone(tm);
            locationModel.setLocation(timezoneAndPlace[1]);
            locationModel.setLatitude(lat);
            locationModel.setLongitude(lng);

            flagDeleteLocation = false;
        } else
            tm = lm.getTimezone();

        for (DateModel dm : locationModel.getDates()) {

            clearTime(dm, locationModel, getCommonTime(dm, locationModel));

            TimeModel timeModel = getSunriseAndSunsetTime(locationModel.getLatitude(),
                    locationModel.getLongitude(), dm.getDate(), tm.getTimezone());

            if (flagDeleteLocation) {
                lm.addDate(dm);
                dm.addLocation(lm);
            }

            dm.addTime(timeModel);
            timeModel.addDate(dm);

            if (flagDeleteLocation) {
                dm.addLocation(lm);
                lm.addTime(timeModel);
            } else {
                dm.addLocation(locationModel);
                locationModel.addTime(timeModel);
            }
        }

        if(flagDeleteLocation) {
            locationRepository.delete(locationModel);
            locationRepository.save(lm);

            return lm.getLocation();
        } else {
            locationRepository.save(locationModel);

            return locationModel.getLocation();
        }
    }
}
