package org.com.extract;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.com.utils.CsvUtils;
import org.com.utils.UriUtils;

import java.io.IOException;
import java.util.List;

public class Extract {
    public List<List<String>> extractSymbols() throws IOException {
        var uri = UriUtils.getUriForSymbolQuery("https://www.alphavantage.co/query", "LB532WEWA7GSCK9E", "LISTING_STATUS");
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(uri);
            var response = client.execute(request);

            return CsvUtils.parseCsv(response.getEntity().getContent().toString());
        }
    }
}
