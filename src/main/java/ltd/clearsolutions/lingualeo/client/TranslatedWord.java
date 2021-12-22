package ltd.clearsolutions.lingualeo.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TranslatedWord(
        Integer id,
        String value,
        Integer votes,
        Integer isUser) {
}
