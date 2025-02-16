package com.lacakio.suggestion.service;

import com.lacakio.suggestion.dto.response.SuggestionResponse;

public interface SuggestionService {
    SuggestionResponse findSuggestion(String query, Double latitude, Double longitude);
}
