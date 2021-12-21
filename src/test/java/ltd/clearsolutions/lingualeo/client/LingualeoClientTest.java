package ltd.clearsolutions.lingualeo.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LingualeoClientTest {

    @Test
    void auth_ResponseFromApi_IfLoggedIn() throws JsonProcessingException {

        LingualeoClient login = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");

        ObjectMapper objectMapper = new ObjectMapper();

        String fromApi = """
                {
                  "apiVersion": "1.0.0",
                  "rev": 0,
                  "status": "ok",
                  "user": {
                    "address": "Skadovsk",
                    "autologin_key": "ac5507e6a304f0e23dc821305c137c19",
                    "avatar": "https://contentcdn.lingualeo.com/uploads/avatar/noavatar.png",
                    "avatar_mini": "https://contentcdn.lingualeo.com/uploads/avatar/noavatar.png",
                    "birth": "2023-09-12",
                    "config": {
                      "audioTrainings": "enable",
                      "expressCourses": "enable",
                      "glossaries": "enable",
                      "grammarCourses": "enable",
                      "grammarTrainings": "enable",
                      "readingTrainings": "enable",
                      "thematicCourses": "enable",
                      "userInterests": "enable",
                      "welcomeTest": "enable",
                      "wordAudioTrainings": "enable",
                      "wordTrainings": "enable"
                    },
                    "create_at": "2021-12-20",
                    "denied_services": [],
                    "email": "vasyll.danylenko@gmail.com",
                    "fname": "",
                    "fullname": "vasyll.danylenko",
                    "hasMap": 1,
                    "hungry_max_points": 150,
                    "hungry_pct": 2,
                    "hungry_points": 3,
                    "is_gold": false,
                    "is_hold": false,
                    "lang_interface": "ru",
                    "lang_native": "ru",
                    "langlevel": 0,
                    "meatballs": 173,
                    "nickname": "",
                    "offlineDictLink": "https://contentcdn.lingualeo.com/mobile/dictionary/en_ru.zip",
                    "offlineDictVersion": 3,
                    "premium_level": "lite",
                    "premium_unlimited": 0,
                    "premium_until": "2021-12-22T00:00:00+0000",
                    "sex": 2,
                    "sname": "",
                    "targetLang": "en",
                    "targetLangList": [
                      {
                        "id": "en",
                        "name": "Английский"
                      }
                    ],
                    "user_id": 43211767,
                    "welcomeLevel": -1,
                    "welcomeLevelName": "",
                    "words_cnt": 0,
                    "words_known": 0,
                    "xp_level": 2,
                    "xp_max_points": 50,
                    "xp_min_points": 26,
                    "xp_points": 27,
                    "xp_title": "Ловкий новичок"
                  }
                }""";

        Map<String, Object> actual = objectMapper.readValue(fromApi,
                new TypeReference<>() {
                });

        Map<String, Object> expected = login.auth();
        assertEquals(actual, expected);
    }

    @Test
    void auth_ResponseFromApi_IfDataNotValid() throws JsonProcessingException {

        LingualeoClient login = new LingualeoClient("vasyll.danylenko@gmail.com", "645644Df");

        ObjectMapper objectMapper = new ObjectMapper();

        String fromApi = """
                {
                  "error_code": 404,
                  "error_msg": "Incorrect authorization data"
                }""";

        Map<String, Object> actual = objectMapper.readValue(fromApi,
                new TypeReference<>() {
                });

        Map<String, Object> expected = login.auth();
        assertEquals(actual, expected);
    }

    @Test
    void getTranslates_ResponseFromApi_IfWordInTheDictionary() throws JsonProcessingException {

        LingualeoClient login = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");

        ObjectMapper objectMapper = new ObjectMapper();

        String translateForCat = """
                {
                  "apiVersion": "1.0.0",
                  "destLang": 1,
                  "directionEnglish": false,
                  "id": null,
                  "is_user": 0,
                  "sound_url": "https://audiocdn.lingualeo.com/v2/0/Y2F01bb80f23.mp3",
                  "srcLang": 0,
                  "status": "ok",
                  "transcription": "kæt",
                  "translate": [
                    {
                      "id": 16,
                      "is_user": null,
                      "pic_url": "https://contentcdn.lingualeo.com/uploads/picture/139166.png",
                      "pos": "verb",
                      "pr": "user",
                      "tr": "кошка",
                      "translate_value": "кошка",
                      "twordId": "136526969",
                      "ut": null,
                      "value": "кошка",
                      "votes": 10331080,
                      "vt": 10331080
                    },
                    {
                      "id": 32,
                      "is_user": null,
                      "pic_url": "https://contentcdn.lingualeo.com/uploads/picture/327427.png",
                      "pos": "verb",
                      "pr": "user",
                      "tr": "кот",
                      "translate_value": "кот",
                      "twordId": "136524815",
                      "ut": null,
                      "value": "кот",
                      "votes": 10331080,
                      "vt": 10331080
                    }
                  ],
                  "translation": "кошка",
                  "wordLemmaId": 7253,
                  "wordLemmaValue": "cat",
                  "word_forms": [
                    {
                      "type": "",
                      "word": "\\"Cat\\""
                    }
                  ],
                  "word_id": null,
                  "word_top": 0,
                  "word_type": 1,
                  "word_value": "\\"Cat\\""
                }""";

        DataWord actual = objectMapper.readValue(translateForCat, DataWord.class);

        List<TranslatedWord> expected = login.getTranslates("Cat");
        assertEquals(actual.translate(), expected);
    }

    @Test
    void addWord(){
        LingualeoClient login = new LingualeoClient("vasyll.danylenko@gmail.com", "6485644Df");
        String word = "Cat";
        String translate = "Кот";
        String context = "Это мой кот.";

        login.addWord(word, translate, context);
    }
}