package org.com;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {
        try (HttpClient client = HttpClient.newHttpClient()){
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=IBMK&apikey=LB532WEWA7GSCK9E"))
                    .build();
            String result = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            System.out.println(result);
        } catch (InterruptedException | IOException e) {
            System.out.println("There was an exception" + e);
        }
    }
}