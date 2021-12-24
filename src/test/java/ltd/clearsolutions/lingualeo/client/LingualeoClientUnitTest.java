package ltd.clearsolutions.lingualeo.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest
public class LingualeoClientUnitTest {

    CloseableHttpClient client;

    @BeforeEach
    void init() {
        client = HttpClientBuilder.create()
                .useSystemProperties()
                .build();
    }

    @Test
    void configures_jvm_proxy_and_enables_browser_proxying() throws Exception {
        stubFor(get("gettranslates?word=Set")
                .withHost(equalTo("api.lingualeo.com/"))
                .willReturn(ok("200")));

        assertEquals(getContent("http://one.my.domain/things"), "1");
    }

    private String getContent(String url) throws Exception {
        try (CloseableHttpResponse response = client.execute(new HttpGet(url))) {
            return EntityUtils.toString(response.getEntity());
        }
    }
}
