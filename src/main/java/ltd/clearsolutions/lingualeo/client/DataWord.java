package ltd.clearsolutions.lingualeo.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataWord(
        @SerializedName("error_msg")
        String errorMsg,
        @SerializedName("is_user")
        Integer isUser,
        List<TranslatedWord> translate,
        String transcription) {
}
