package org.com;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {
        try (HttpClient client = HttpClient.newHttpClient()){


            String result = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            System.out.println(result);
        } catch (InterruptedException | IOException e) {
            System.out.println("There was an exception" + e);
        }
    }
}