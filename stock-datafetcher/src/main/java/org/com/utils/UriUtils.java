package org.com.utils;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class UriUtils {
    public static URI getUriForSymbolQuery(String url, String apiKey, String function) {
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
}
