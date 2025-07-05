package org.com;

import org.com.entity.Symbol;
import org.com.entity.TimeSeriesDaily;
import org.com.extract.Extract;
import org.com.load.Load;
import org.com.transform.Transform;

import java.io.IOException;
import java.util.List;

public class Main {
    //TODO move to config
    private static final String BASE_URL = "https://www.alphavantage.co/query";
    private static final String API_KEY = "LB532WEWA7GSCK9E";
    private static final String SYMBOLS_FUNCTION = "LISTING_STATUS";
    private static final String COMPANY_DATA_FUNCTION = "OVERVIEW";
    private static final String TIME_SERIES_DAILY_FUNCTION = "TIME_SERIES_DAILY";
    private static final String OUTPUT_SIZE_FULL = "full";

    private static final String DB_URI = "jdbc:postgresql://localhost:5432/stock-exchange-dashboard";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "admin";

    public static void main(String[] args) {

        processTimeSeriesDaily();

        //TODO make this enabled/disabled from config or cli
        //processSymbols();
    }

    private static void processTimeSeriesDaily() {
        //TODO make the extractor iterate over 25 predefined symbols. Only 25 because of the api limit.
        String symbol = "IBM";
        Load load = new Load(DB_URI,DB_USERNAME, DB_PASSWORD);

        try {
            String responseJson = Extract.extractTimeSeriesDailyData(symbol, BASE_URL, API_KEY, TIME_SERIES_DAILY_FUNCTION, OUTPUT_SIZE_FULL);
            List<TimeSeriesDaily> timeSeriesDailies = Transform.transformTimeSeriesDaily(responseJson, symbol);
            load.loadTimeSeriesDailyForSymbol(timeSeriesDailies, symbol);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processSymbols() {
        try {
            List<Symbol> symbols = Extract.extractSymbols(BASE_URL, API_KEY, SYMBOLS_FUNCTION);
            Load load = new Load(DB_URI,DB_USERNAME, DB_PASSWORD);
            load.loadSymbols(symbols);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}