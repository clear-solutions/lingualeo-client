package ltd.clearsolutions.lingualeo.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataWord(
        String errorMsg,
        Integer isUser,
        List<TranslatedWord> translate,
        String transcription) {
}
