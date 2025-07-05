package org.com.utils;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class UriUtils {
    public static URI getUriForRequest(String url, String apiKey, String function) {
        try {
            var builder = new URIBuilder(url);
            builder.addParameter("function", function);
            builder.addParameter("apikey", apiKey);

            return builder.build();
        } catch (URISyntaxException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static URI getUriForTimeSeriesDailyRequest(String url, String apiKey, String function, String symbol, String outputSize) {
        try {
            var builder = new URIBuilder(url);
            builder.addParameter("function", function);
            builder.addParameter("apikey", apiKey);
            builder.addParameter("symbol", symbol);
            builder.addParameter("outputsize", outputSize);

            return builder.build();
        } catch (URISyntaxException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
