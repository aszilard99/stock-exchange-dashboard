package org.com.pipeline;

import org.com.entity.CompanyOverview;
import org.com.entity.Symbol;
import org.com.entity.TimeSeriesDaily;
import org.com.enums.ProcessResult;
import org.com.pipeline.extract.Extract;
import org.com.pipeline.load.Load;
import org.com.pipeline.transform.Transform;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;

public class Pipeline {
    private static final Logger logger = Logger.getLogger(Pipeline.class.getName());
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


    public ProcessResult processTimeSeriesDaily() {
        List<String> symbols = load.getSymbolsWithoutTimeSeriesDailyUpdate();

        for (var symbol: symbols) {
            var result = processTimeSeriesDailyForSymbol(symbol);

            if (result == ProcessResult.SHUTDOWN_REQUESTED) {
                return result;
            }

            try {
                Thread.sleep(Duration.of(10, ChronoUnit.SECONDS));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return ProcessResult.SUCCESS;
    }

    private ProcessResult processTimeSeriesDailyForSymbol(String symbol) {
        try {
            String responseJson = Extract.extractTimeSeriesDailyData(symbol, BASE_URL, API_KEY, TIME_SERIES_DAILY_FUNCTION, OUTPUT_SIZE_FULL);
            if (isEmptyResponse(responseJson)) {
                logger.info(String.format("No response recieved for time series daily request for the symbol %s. No company data exists for this symbol yet, or the daily api usage limit is reached. Skipping this round.", symbol));
                return ProcessResult.SKIPPED;
            }
            List<TimeSeriesDaily> timeSeriesDailies = Transform.transformTimeSeriesDaily(responseJson, symbol);
            if (timeSeriesDailies.isEmpty()){
                //TODO Maybe implement a normal shutdown procedure, without controlling expected application flow with exceptions.
                logger.info("Processed time series daily update count is zero, the daily api limit is reached. Exiting the application.");
                return ProcessResult.SHUTDOWN_REQUESTED;
            }

            load.loadTimeSeriesDailyForSymbol(timeSeriesDailies, symbol);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ProcessResult.SUCCESS;
    }

    public void processSymbols() {
        try {
            String symbolData = Extract.extractSymbolData(BASE_URL, API_KEY, SYMBOLS_FUNCTION);
            List<Symbol> symbols = Transform.transformSymbolData(symbolData);

            load.loadSymbols(symbols);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void processCompanyOverviewData() {
        List<String> symbols = List.of("SAVA", "SGMO", "SLAB", "SLF", "SLYG", "SLYV", "SMDV", "SMH", "SOHU", "SPTM", "SPTN");

        symbols.forEach(symbol -> {
            processCompanyOverviewDataForSymbol(symbol, load);
            try {
                Thread.sleep(Duration.of(3, ChronoUnit.SECONDS));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void processCompanyOverviewDataForSymbol(String symbol, Load load) {
        try {
            String responseJson = Extract.extractCompanyOverviewData(symbol, BASE_URL, API_KEY, COMPANY_DATA_FUNCTION);
            if (isEmptyResponse(responseJson)) {
                logger.info(String.format("No response recieved for company overview request for the symbol %s.No company data exists for this symbol yet, or the daily api usage limit is reached. Skipping this round.", symbol));
                return;
            }
            CompanyOverview companyOverview = Transform.transformCompanyOverviewData(responseJson, symbol);
            load.loadCompanyOverview(companyOverview, symbol);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static boolean isEmptyResponse(String response) {
        return response == null || response.length() <= 2;
    }
}
