package ltd.clearsolutions.lingualeo.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LingualeoClient implements ApiClient {

    private static final String API_URL = "https://api.lingualeo.com/";

    private static String login;
    private static String password;


    public LingualeoClient(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public Map<String, Object> auth() {

        String urlParameters = "email=" + login + "&password=" + password;
        String requestUrl = API_URL + "login" + "?" + urlParameters;

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(new HttpGet(requestUrl));

            String body = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(body,
                    new TypeReference<HashMap<String, Object>>() {
                    });

        } catch (
                IOException e) {
            e.getStackTrace();
        }
        return Collections.emptyMap();
    }

    @Override
    public List<TranslatedWord> getTranslates(String word) {
        String urlParameters = "word=" + word;
        String requestUrl = API_URL + "gettranslates" + "?" + urlParameters;

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(new HttpGet(requestUrl));

            String body = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = new ObjectMapper();

            DataWord dataWord = objectMapper.readValue(body, DataWord.class);

            return dataWord.translate();

        } catch (
                IOException | ParseException e) {
            e.getStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public void addWord(String word, String translate, String context) {
        String urlParameters = "word=" + URLEncoder.encode(word, StandardCharsets.UTF_8) +
                "&tword=" + URLEncoder.encode(translate, StandardCharsets.UTF_8) +
                "&context=" + URLEncoder.encode(context, StandardCharsets.UTF_8);

        String requestUrl = API_URL + "addword?" + urlParameters;

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            client.execute(new HttpPost(requestUrl));

        } catch (
                IOException e) {
            e.getStackTrace();
        }
    }

}
