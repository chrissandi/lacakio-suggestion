package com.lacakio.suggestion.service.implementation;

import com.lacakio.suggestion.entity.City;
import com.lacakio.suggestion.service.FileReaderService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.lacakio.suggestion.constant.Index.*;
import static com.lacakio.suggestion.constant.Index.COUNTRY;

@Service
public class FileReaderServiceImpl implements FileReaderService {
    private final String fileName = "data/cities_canada-usa.tsv";

    @Override
    public List<City> getListOfCities() {
        List<City> cities = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(fileName);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream())))  {

            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] records = line.split("\t");

                City city = new City();
                city.setId(records[ID]);
                city.setName(records[NAME]);
                city.setAscii(records[ASCII]);
                city.setAltName(records[ALT_NAME]);
                city.setLatitude(Double.parseDouble(records[LAT]));
                city.setLongitude(Double.parseDouble(records[LONG]));
                city.setAdmin1(records[ADMIN1]);
                city.setCountry(records[COUNTRY]);

                cities.add(city);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load city data", e);
        }
        return cities;
    }
}
