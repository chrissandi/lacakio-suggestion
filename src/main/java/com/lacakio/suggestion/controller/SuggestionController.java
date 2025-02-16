package com.lacakio.suggestion.controller;

import com.lacakio.suggestion.dto.response.SuggestionResponse;
import com.lacakio.suggestion.service.SuggestionService;
import com.lacakio.suggestion.service.implementation.SuggestionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.lacakio.suggestion.constant.WebConstant.SUGGESTION_PATH;

@RestController
@RequestMapping(SUGGESTION_PATH)
public class SuggestionController {

    private final SuggestionService suggestionService = new SuggestionServiceImpl();

    @GetMapping
    public SuggestionResponse getSuggestion(
            @RequestParam("q") String query,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude){
        SuggestionResponse response = suggestionService.findSuggestion(query, latitude, longitude);
        return ResponseEntity.ok(response).getBody();
    }
}
