package com.example.sunriseandsunsetservice.service.impl;

import com.example.sunriseandsunsetservice.dto.TimezoneDTO;
import com.example.sunriseandsunsetservice.exceptions.MyRuntimeException;
import com.example.sunriseandsunsetservice.model.TimezoneModel;
import com.example.sunriseandsunsetservice.repository.TimezoneRepository;
import com.example.sunriseandsunsetservice.service.TimezoneService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TimezoneServiceImpl implements TimezoneService {
    private final TimezoneRepository timezoneRepository;

    @Override
    @Transactional
    public TimezoneDTO createTimezone(String timezone) {

        if (timezoneRepository.findByTimezone(timezone) == null)
            timezoneRepository.save(new TimezoneModel(timezone));

        return new TimezoneDTO(timezone);
    }

    @Override
    public List<TimezoneDTO> readAllTimezones() {

        List<TimezoneDTO> timezones = new ArrayList<>();

        for(TimezoneModel tm : timezoneRepository.findAll())
            timezones.add(new TimezoneDTO(tm.getTimezone()));

        return timezones;
    }

    @Override
    @Transactional
    public TimezoneDTO updateTimezone(int id, String timezone) {

        TimezoneModel timezoneModel = timezoneRepository.findById(id);
        if(timezoneModel == null)
            throw new MyRuntimeException("Wrong id.");

        timezoneModel.setTimezone(timezone);
        timezoneRepository.save(timezoneModel);

        return new TimezoneDTO(timezone);
    }

    @Override
    @Transactional
    public TimezoneDTO deleteTimezone(int id) {

        TimezoneModel timezoneModel = timezoneRepository.findById(id);
        if(timezoneModel == null)
            throw new MyRuntimeException("Wrong id.");

        if (timezoneModel.getLocations().isEmpty())
            timezoneRepository.deleteById(id);
        else throw new MyRuntimeException("Timezone has connections.");

        return new TimezoneDTO(timezoneModel.getTimezone());
    }
}
