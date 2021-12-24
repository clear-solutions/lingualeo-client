package ltd.clearsolutions.lingualeo.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LingualeoClientIntegrationTest {

    ObjectMapper objectMapper = new ObjectMapper();

    LingualeoClient correctLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");

    LingualeoClient IncorrectLingualeoClient;

    @Test
    void auth_PutCorrectCredentials_Cookie() {
        correctLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");
        correctLingualeoClient.auth();

        assertNotNull(correctLingualeoClient.authCookie);
    }

    @Test
    void auth_PutIncorrectCredentials_Null() {
        IncorrectLingualeoClient = new LingualeoClient("vasyll.danyleil.com", "64");
        correctLingualeoClient.auth();

        assertNull(IncorrectLingualeoClient.authCookie);
    }

    @Test
    void getTranslates_PutCorrectWord_DataWordClass() throws IOException {
        DataWord expected = getActualResultFromFile("/response/getTranslates/getTranslates_PutCorrectWord_CorrectResponse.json",
                new TypeReference<>() {
                });

        List<TranslatedWord> actual = correctLingualeoClient.getTranslates("Set");
        assertEquals(expected.translate(), actual);
    }

    @Test
    void getTranslates_PutIncorrectWord_DataWordClass() throws IOException {

        DataWord actual = getActualResultFromFile("/response/getTranslates/getTranslates_PutIncorrectWord_CorrectResponse.json",
                new TypeReference<>() {
                });

        List<TranslatedWord> expected = correctLingualeoClient.getTranslates("fdsafasdafasdf");
        assertEquals(actual.translate(), expected);
    }


    @Test
    void addWord_PutCorrectWordIfMissing_MapStringObject() throws IOException {

        correctLingualeoClient.auth();

        Map<String, Object> expected = getActualResultFromFile("/response/addWord/addWord_PutCorrectWordIfMissing_CorrectResponse.json",
                new TypeReference<>() {
                });

        Map<String, Object> actual = correctLingualeoClient.addWord("Set", "Положить");
        assertEquals(expected.get("added_translate_count"), actual.get("added_translate_count"));
        assertEquals(expected.get("lang"), actual.get("lang"));
    }

    @Test
    void addWord_PutIncorrectWordIfMissing_MapStringObject() throws IOException {
        correctLingualeoClient.auth();

        Map<String, Object> expected = getActualResultFromFile("/response/addWord/addWord_PutIncorrectWordIfMissing_CorrectResponse.json",
                new TypeReference<>() {
                });

        Map<String, Object> actual = correctLingualeoClient.addWord("fdasfdfasdf", "фдасфдфасдф");
        assertEquals(expected.get("added_translate_count"), actual.get("added_translate_count"));
        assertEquals(expected.get("lang"), actual.get("lang"));
    }

    private <T> T getActualResultFromFile(String fileName, TypeReference<T> T) throws IOException {
        InputStream inputFile = getClass().getResourceAsStream(fileName);
        BufferedReader readFile = null;
        if (inputFile != null) {
            readFile = new BufferedReader(new InputStreamReader(inputFile, StandardCharsets.UTF_8));
        }

        return objectMapper.readValue(readFile, T);
    }
}