package org.com.pipeline.extract;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.com.utils.UriUtils;

import java.io.IOException;
import java.util.logging.Logger;

public class Extract {
    private static final Logger logger = Logger.getLogger(Extract.class.getName());

    public static String extractSymbolData(String baseUrl, String apiKey, String function) throws IOException {
        var uri = UriUtils.getUriForRequest(baseUrl, apiKey, function);
        logger.info("URL used for symbol request: " + uri.toString());
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(uri);
            try (CloseableHttpResponse response = client.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }

    public static String extractTimeSeriesDailyData(String symbol, String baseUrl, String apiKey, String function, String outputSize) throws IOException {
        var uri = UriUtils.getUriForTimeSeriesDailyRequest(baseUrl, apiKey, function, symbol, outputSize);
        logger.info("URL used for time series daily request: " + uri.toString());
        String responseString = null;

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(uri);
            try (CloseableHttpResponse response = client.execute(request)) {
                responseString = EntityUtils.toString(response.getEntity());
            }
        }
        return responseString;
    }

    public static String extractCompanyOverviewData(String symbol, String baseUrl, String apiKey, String function) throws IOException {
        var uri = UriUtils.getUriForRequestWithSymbol(baseUrl, apiKey, function, symbol);
        logger.info("URL used for company overview request: " + uri.toString());
        String responseString = null;

        try(CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet reqquest = new HttpGet(uri);
            try(CloseableHttpResponse response = client.execute(reqquest)) {
                responseString = EntityUtils.toString(response.getEntity());
            }
        }
        return responseString;
    }
}
