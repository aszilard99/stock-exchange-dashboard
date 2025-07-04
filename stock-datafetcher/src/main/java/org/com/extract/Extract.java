package org.com.extract;

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
    public static List<Symbol> extractSymbols() throws IOException {
        var uri = UriUtils.getUriForSymbolQuery("https://www.alphavantage.co/query", "LB532WEWA7GSCK9E", "LISTING_STATUS");
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
}
