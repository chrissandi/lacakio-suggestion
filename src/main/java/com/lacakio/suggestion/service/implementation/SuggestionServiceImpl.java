package com.lacakio.suggestion.service.implementation;

import com.lacakio.suggestion.entity.City;
import com.lacakio.suggestion.entity.Suggestion;
import com.lacakio.suggestion.service.FileReaderService;
import com.lacakio.suggestion.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuggestionServiceImpl implements SuggestionService {
    private final FileReaderService fileReaderService;

    @Override
    public List<Suggestion> findSuggestion(String query, Double latitude, Double longitude) {

        // Normalize the query for case-insensitive matching
        List<City> cities = fileReaderService.getListOfCities();
        String normalizedQuery = query.toLowerCase();

        List<Suggestion> suggestions = cities.stream()
                .filter(city -> city.getName().toLowerCase().contains(normalizedQuery)
                        || city.getAscii().toLowerCase().contains(normalizedQuery))
                .map(city -> {
                    // Compute a basic name match score (e.g., based on string similarity)
                    double nameScore = computeNameScore(city.getName(), query);

                    // If coordinates are provided, adjust the score using a simple distance bias
                    double geoScore = 0;
                    if (latitude != null && longitude != null) {
                        geoScore = computeGeoScore(latitude, longitude, city.getLatitude(), city.getLongitude());
                    }

                    // Combine the scores. (Weighting may be adjusted as needed.)
                    double combinedScore = combineScores(nameScore, geoScore);

                    Suggestion suggestion = new Suggestion();
                    String suggestionName = city.getName()+","+city.getAdmin1()+","+city.getCountry();
                    suggestion.setName(suggestionName);
                    suggestion.setLatitude(String.valueOf(city.getLatitude()));
                    suggestion.setLongitude(String.valueOf(city.getLongitude()));
                    suggestion.setScore(roundScore(combinedScore));
                    return suggestion;
                })
                // Filter out low-score suggestions if desired.
                .filter(s -> s.getScore() > 0)
                // Sort descending by score
                .sorted(Comparator.comparing(Suggestion::getScore).reversed())
                .collect(Collectors.toList());

        return suggestions;
    }

    // A simple scoring function based on name match
    private double computeNameScore(String cityName, String query) {
        cityName = cityName.toLowerCase();
        query = query.toLowerCase();
        // If the name starts with the query, give a high score; else, use a basic proportion.
        if (cityName.startsWith(query)) {
            return 1.0;
        } else if (cityName.contains(query)) {
            return 0.8;
        }
        return 0;
    }

    // Compute a score based on geographic distance.
    // Here we use a very simple formula to convert distance into a score between 0 and 1.
    private double computeGeoScore(double userLat, double userLon, double cityLat, double cityLon) {
        double distance = haversine(userLat, userLon, cityLat, cityLon);
        // For example, if distance is 0, score is 1; if distance is greater than 2000 km, score is near 0.
        double score = Math.max(0, 1 - (distance / 2000.0));
        return score;
    }

    // Combine name and geographic scores.
    private double combineScores(double nameScore, double geoScore) {
        // If geoScore is zero (or not provided), use nameScore.
        if (geoScore == 0) return nameScore;
        // Otherwise, combine them. For example, take an average weighted 70% on name match and 30% on geo.
        return (0.7 * nameScore) + (0.3 * geoScore);
    }

    // Utility method: Haversine formula to calculate distance (in km) between two lat/lon points.
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6378; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }

    private double roundScore(double score) {
        return Math.round(score * 10.0) / 10.0;
    }
}
