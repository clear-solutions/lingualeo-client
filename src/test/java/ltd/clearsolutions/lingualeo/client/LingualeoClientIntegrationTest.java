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

import static org.junit.jupiter.api.Assertions.assertEquals;

class LingualeoClientIntegrationTest {

    private final LingualeoClient lingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void auth_PutCorrectCredentials_shouldReturnMap() throws IOException {

        BufferedReader readFile = getActualResultFromFile("/response/authorizationResponse.json");

        Map<String, Object> actual = objectMapper.readValue(readFile,
                new TypeReference<>() {
                });

        Map<String, Object> expected = lingualeoClient.auth();
        assertEquals(actual, expected);
    }

    @Test
    void auth_PutIncorrectCredentials_shouldReturnMap() throws IOException {

        LingualeoClient wrongLingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "645644Df");

        BufferedReader readFile = getActualResultFromFile("/response/authorizationError.json");

        Map<String, Object> actual = objectMapper.readValue(readFile,
                new TypeReference<>() {
                });

        Map<String, Object> expected = wrongLingualeoClient.auth();
        assertEquals(actual, expected);
    }

    @Test
    void getTranslates_PutCorrectCredentials_DataWordClass() throws IOException {

        BufferedReader readFile = getActualResultFromFile("/response/translatesResponse.json");

        DataWord actual = objectMapper.readValue(readFile, DataWord.class);

        List<TranslatedWord> expected = lingualeoClient.getTranslates("Cat");
        assertEquals(actual.translate(), expected);
    }

    private BufferedReader getActualResultFromFile(String fileName) {
        InputStream inputFile = getClass().getResourceAsStream(fileName);
        BufferedReader readFile = null;
        if (inputFile != null) {
            readFile = new BufferedReader(new InputStreamReader(inputFile, StandardCharsets.UTF_8));
        }
        return readFile;
    }

}