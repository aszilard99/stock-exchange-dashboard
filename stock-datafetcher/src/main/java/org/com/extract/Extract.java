package org.com.extract;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.com.entity.Symbol;
import org.com.utils.SymbolCsvParser;
import org.com.utils.UriUtils;

import java.io.IOException;
import java.util.List;

public class Extract {
    public static List<Symbol> extractSymbols(String baseUrl, String apiKey, String function) throws IOException {
        var uri = UriUtils.getUriForRequest(baseUrl, apiKey, function);
        System.out.println("URL used for symbol query: " + uri.toString());
        String responseString;
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(uri);
            try (CloseableHttpResponse response = client.execute(request)) {
                responseString = EntityUtils.toString(response.getEntity());
            }
            return SymbolCsvParser.parseCsvSymbol(responseString);
        }
    }

    public static String extractTimeSeriesDailyData(String symbol, String baseUrl, String apiKey, String function, String outputSize) throws IOException {
        var uri = UriUtils.getUriForTimeSeriesDailyRequest(baseUrl, apiKey, function, symbol, outputSize);
        System.out.println("URL used for time series daily query: " + uri.toString());
        String responseString = null;
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(uri);
            /*try (CloseableHttpResponse response = client.execute(request)) {
                responseString = EntityUtils.toString(response.getEntity());
            }*/
            return responseString;
        }
    }
}
