package ltd.clearsolutions.lingualeo.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TranslatedWord(
        Integer id,
        String value,
        Integer votes,
        @SerializedName("is_user")
        Integer isUser) {
}
