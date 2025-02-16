package com.lacakio.suggestion.service.implementation;

import com.lacakio.suggestion.entity.City;
import com.lacakio.suggestion.entity.Suggestion;
import com.lacakio.suggestion.service.FileReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SuggestionServiceImplTest {

    @Mock
    private FileReaderService fileReaderService;

    private SuggestionServiceImpl suggestionService;

    private List<City> mockCities;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        suggestionService = new SuggestionServiceImpl(fileReaderService);

        // Setup mock cities data
        mockCities = Arrays.asList(
                createCity("New York", "NY", "US", 40.7128, -74.0060),
                createCity("London", "ENG", "GB", 51.5074, -0.1278),
                createCity("New Castle", "ENG", "GB", 54.9783, -1.6178),
                createCity("Newdelhi", "DL", "IN", 28.6139, 77.2090)
        );

        when(fileReaderService.getListOfCities()).thenReturn(mockCities);
    }

    @Test
    void findSuggestion_ExactMatch() {
        // Test exact match
        List<Suggestion> suggestions = suggestionService.findSuggestion("New York", null, null);

        assertNotNull(suggestions);
        assertFalse(suggestions.isEmpty());
        assertEquals("New York,NY,US", suggestions.get(0).getName());
        assertEquals(1.0, suggestions.get(0).getScore());
    }

    @Test
    void findSuggestion_PartialMatch() {
        // Test partial match "New"
        List<Suggestion> suggestions = suggestionService.findSuggestion("New", null, null);

        assertNotNull(suggestions);
        assertEquals(3, suggestions.size());
    }

    @Test
    void findSuggestion_WithLocation() {
        // Test with location near London
        double londonLat = 51.5074;
        double londonLon = -0.1278;

        List<Suggestion> suggestions = suggestionService.findSuggestion("New", londonLat, londonLon);

        assertNotNull(suggestions);
        assertFalse(suggestions.isEmpty());

        // Newcastle should have a higher score than New Delhi due to proximity to London
        double newcastleScore = suggestions.stream()
                .filter(s -> s.getName().contains("New Castle"))
                .findFirst()
                .map(Suggestion::getScore)
                .orElse(0.0);

        double newDelhiScore = suggestions.stream()
                .filter(s -> s.getName().contains("Newdelhi"))
                .findFirst()
                .map(Suggestion::getScore)
                .orElse(0.0);
        assertTrue(newcastleScore < newDelhiScore);
    }

    @Test
    void findSuggestion_NoMatch() {
        List<Suggestion> suggestions = suggestionService.findSuggestion("XYZ", null, null);

        assertNotNull(suggestions);
        assertTrue(suggestions.isEmpty());
    }

    @Test
    void findSuggestion_CaseInsensitive() {
        List<Suggestion> suggestions = suggestionService.findSuggestion("london", null, null);

        assertNotNull(suggestions);
        assertFalse(suggestions.isEmpty());
        assertEquals("London,ENG,GB", suggestions.get(0).getName());
    }

    // Helper method to create City objects
    private City createCity(String name, String admin1, String country, double latitude, double longitude) {
        City city = new City();
        city.setName(name);
        city.setAdmin1(admin1);
        city.setCountry(country);
        city.setLatitude(latitude);
        city.setLongitude(longitude);
        city.setAscii(name); // Setting ASCII name same as name for simplicity
        return city;
    }

    @Test
    void testNoResults() {
        List<Suggestion> results = suggestionService.findSuggestion("NonExistentCity", null, null);

        assertTrue(results.isEmpty());
    }


    @Test
    void testCoordinateFormat() {
        List<Suggestion> results = suggestionService.findSuggestion("New York", null, null);

        assertFalse(results.isEmpty());
        Suggestion suggestion = results.get(0);

        // Verify coordinate format
        assertNotNull(suggestion.getLatitude());
        assertNotNull(suggestion.getLongitude());
        assertTrue(Double.parseDouble(suggestion.getLatitude()) > 0);
        assertTrue(Double.parseDouble(suggestion.getLongitude()) < 0);
    }

    @Test
    void testScoreRounding() {
        List<Suggestion> results = suggestionService.findSuggestion("Lond", 51.452, -0.0278);

        assertFalse(results.isEmpty());
        double score = results.get(0).getScore();
        // Verify that score has at most one decimal place
        assertEquals(Math.round(score * 10.0) / 10.0, score);
    }
}