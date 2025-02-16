package com.lacakio.suggestion.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import com.lacakio.suggestion.dto.response.SuggestionResponse;
import com.lacakio.suggestion.entity.Suggestion;
import com.lacakio.suggestion.service.SuggestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SuggestionControllerTest {

    @Mock
    private SuggestionService suggestionService;

    @InjectMocks
    private SuggestionController suggestionController;

    @BeforeEach
    void setUp() {
        when(suggestionService.findSuggestion(anyString(), anyDouble(), anyDouble()))
                .thenReturn(Collections.emptyList());
    }

    @Test
    void testGetSuggestions() {
        List<Suggestion> mockSuggestions = List.of(
                new Suggestion("London, ON, Canada", "42.98339", "-81.23304", 0.9),
                new Suggestion("London, OH, USA", "39.88645", "-83.44825", 0.5)
        );

        when(suggestionService.findSuggestion("Londo", 43.70011, -79.4163)).thenReturn(mockSuggestions);

        SuggestionResponse response = suggestionController.getSuggestion("Londo", 43.70011, -79.4163);

        assertNotNull(response.getSuggestions());
        assertEquals(response.getSuggestions().size(), mockSuggestions.size());
        assertEquals("London, ON, Canada", response.getSuggestions().get(0).getName());
    }
}
