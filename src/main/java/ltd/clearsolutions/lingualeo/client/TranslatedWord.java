package ltd.clearsolutions.lingualeo.client;

public record TranslatedWord(
        Integer id,
        String value,
        Integer votes,
        Integer is_user) {
}
