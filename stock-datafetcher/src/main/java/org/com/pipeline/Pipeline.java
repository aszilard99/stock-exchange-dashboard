package org.com.pipeline;

import org.com.entity.CompanyOverview;
import org.com.entity.Symbol;
import org.com.entity.TimeSeriesDaily;
import org.com.pipeline.extract.Extract;
import org.com.pipeline.load.Load;
import org.com.pipeline.transform.Transform;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Pipeline {
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
    private final Load load = new Load(DB_URI,DB_USERNAME, DB_PASSWORD);


    public void processTimeSeriesDaily() {
        //List<String> symbols = List.of("AMZN", "DIS", "NVDA", "WMT", "WTBA", "WTFC", "AAPL", "COST", "MCD", "BGR");
        //List<String> symbols = List.of("ROK", "ROOT", "RPAY", "RPHS", "RPRX", "RRBI", "RSG", "RSSS", "RSVRW", "SABR", "SABSW");
        List<String> symbols = List.of("SAVA", "SGMO", "SLAB", "SLF", "SLYG", "SLYV", "SMDV", "SMH", "SOHU", "SPTM", "SPTN");

        symbols.forEach(symbol -> {
            processTimeSeriesDailyForSymbol(symbol);
            try {
                Thread.sleep(Duration.of(10, ChronoUnit.SECONDS));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void processTimeSeriesDailyForSymbol(String symbol) {
        try {
            String responseJson = Extract.extractTimeSeriesDailyData(symbol, BASE_URL, API_KEY, TIME_SERIES_DAILY_FUNCTION, OUTPUT_SIZE_FULL);
            List<TimeSeriesDaily> timeSeriesDailies = Transform.transformTimeSeriesDaily(responseJson, symbol);
            load.loadTimeSeriesDailyForSymbol(timeSeriesDailies, symbol);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void processSymbols() {
        try {
            List<Symbol> symbols = Extract.extractSymbols(BASE_URL, API_KEY, SYMBOLS_FUNCTION);

            load.loadSymbols(symbols);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void processCompanyOverviewData() {
        //List<String> symbols = List.of("AMZN", "DIS", "NVDA", "WMT", "WTBA", "WTFC", "AAPL", "COST", "MCD", "BGR");
        //List<String> symbols = List.of("ROK", "ROOT", "RPAY", "RPHS", "RPRX", "RRBI", "RSG", "RSSS", "RSVRW", "SABR", "SABSW");
        List<String> symbols = List.of("SAVA", "SGMO", "SLAB", "SLF", "SLYG", "SLYV", "SMDV", "SMH", "SOHU", "SPTM", "SPTN");

        symbols.forEach(symbol -> {
            processCompanyOverviewDataForSymbol(symbol, load);
            try {
                Thread.sleep(Duration.of(10, ChronoUnit.SECONDS));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void processCompanyOverviewDataForSymbol(String symbol, Load load) {
        try {
            String responseJson = Extract.extractCompanyOverviewData(symbol, BASE_URL, API_KEY, COMPANY_DATA_FUNCTION);
            CompanyOverview companyOverview = Transform.transformCompanyOverviewData(responseJson, symbol);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
