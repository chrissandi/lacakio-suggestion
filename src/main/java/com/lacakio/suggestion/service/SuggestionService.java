package com.lacakio.suggestion.service;

import com.lacakio.suggestion.entity.Suggestion;

import java.util.List;

public interface SuggestionService {
    List<Suggestion> findSuggestion(String query, Double latitude, Double longitude);
}
