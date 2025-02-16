package com.lacakio.suggestion.dto.response;

import com.lacakio.suggestion.entity.Suggestion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter @Setter
public class SuggestionResponse {

    private List<Suggestion> suggestions;

}
