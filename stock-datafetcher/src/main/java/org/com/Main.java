package org.com;

import org.com.extract.Extract;
import org.com.load.Load;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            var symbols = Extract.extractSymbols();
            Load load = new Load("jdbc:postgresql://localhost:5432/stock-exchange-dashboard","postgres", "admin");
            load.loadSymbols(symbols);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}