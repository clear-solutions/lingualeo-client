package ltd.clearsolutions.lingualeo.client;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    @Test
    void auth_PutCorrectCredentials_shouldReturnMap() throws IOException {

        LingualeoClient lingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");

        ObjectMapper objectMapper = new ObjectMapper();

        InputStream inputFile = getClass().getResourceAsStream("/AuthorizationResponse.json");
        BufferedReader readFile = null;
        if (inputFile != null) {
            readFile = new BufferedReader(new InputStreamReader(inputFile, StandardCharsets.UTF_8));
        }

        Map<String, Object> actual = objectMapper.readValue(readFile,
                new TypeReference<>() {
                });

        Map<String, Object> expected = lingualeoClient.auth();
        assertEquals(actual, expected);
    }

    @Test
    void auth_PutIncorrectCredentials_shouldReturnMap() throws JsonProcessingException {

        LingualeoClient lingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "645644Df");

        ObjectMapper objectMapper = new ObjectMapper();

        String fromApi = """
                {
                  "error_code": 404,
                  "error_msg": "Incorrect authorization data"
                }""";

        Map<String, Object> actual = objectMapper.readValue(fromApi,
                new TypeReference<>() {
                });

        Map<String, Object> expected = lingualeoClient.auth();
        assertEquals(actual, expected);
    }

    @Test
    void getTranslates_PutCorrectCredentials_DataWordClass() throws IOException {

        LingualeoClient lingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");

        ObjectMapper objectMapper = new ObjectMapper();

        InputStream inputFile = getClass().getResourceAsStream("/TranslatesResponse.json");
        BufferedReader readFile = null;
        if (inputFile != null) {
            readFile = new BufferedReader(new InputStreamReader(inputFile, StandardCharsets.UTF_8));
        }

        DataWord actual = objectMapper.readValue(readFile, DataWord.class);

        List<TranslatedWord> expected = lingualeoClient.getTranslates("Cat");
        assertEquals(actual.translate(), expected);
    }

    @Test
    void addWord_PutWord_WordInDictionary() {
        LingualeoClient lingualeoClient = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");
        String word = "Cat";
        String translate = "Кот";
        String context = "Это мой кот.";

        lingualeoClient.addWord(word, translate, context);
    }
}