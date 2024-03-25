package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.cache.InMemoryCache;
import com.example.sunriseandsunsetservice.dto.TimezoneDTO;
import com.example.sunriseandsunsetservice.exceptions.MyRuntimeException;
import com.example.sunriseandsunsetservice.model.Timezone;
import com.example.sunriseandsunsetservice.repository.TimezoneRepository;
import com.example.sunriseandsunsetservice.service.TimezoneService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class TimezoneServiceImpl implements TimezoneService {

    private final TimezoneRepository timezoneRepository;

    private final InMemoryCache cache;

    @Override
    @Transactional
    public TimezoneDTO createTimezone(String newTimezone) {

        Timezone timezone;
        if ((timezone = timezoneRepository.findByTimezone(newTimezone)) == null)
            timezone = timezoneRepository.save(new Timezone(newTimezone));

        cache.put("Timezone" + timezone.getId().toString(), timezone);

        return new TimezoneDTO(newTimezone);
    }

    @Override
    public List<TimezoneDTO> readAllTimezones() { return timezoneRepository.findAllTimezones(); }

    @Override
    public TimezoneDTO getById(Integer id) {

        Timezone tempTimezone = (Timezone) cache.get("Timezone" + id.toString());

        if(tempTimezone == null) {
            tempTimezone = timezoneRepository.findById(id).orElseThrow(
                    () -> new MyRuntimeException("Timezone not found."));

            cache.put("Timezone" + id, tempTimezone);
        }

        return new TimezoneDTO(tempTimezone.getTimezone());
    }

    @Override
    @Transactional
    public TimezoneDTO updateTimezone(Integer id, String newTimezone) {

        Timezone timezone = (Timezone) cache.get("Timezone" + id);
        if(timezone == null)
            timezone = timezoneRepository.findById(id).orElseThrow(
                () -> new MyRuntimeException("Wrong id."));

        cache.remove("Timezone" + id);

        timezone.setTimezone(newTimezone);
        timezoneRepository.save(timezone);

        cache.put("Timezone" + id, timezone);

        return new TimezoneDTO(newTimezone);
    }

    @Override
    @Transactional
    public TimezoneDTO deleteTimezone(Integer id) {

        Timezone timezone = (Timezone) cache.get("Timezone" + id);
        if(timezone == null)
            timezone = timezoneRepository.findById(id).orElseThrow(
                () -> new MyRuntimeException("Wrong id."));

        if (timezone.getLocations().isEmpty()) {
            timezoneRepository.deleteById(id);
            cache.remove("Timezone" + id);
        }
        else throw new MyRuntimeException("Timezone has connections.");

        return new TimezoneDTO(timezone.getTimezone());
    }
}
