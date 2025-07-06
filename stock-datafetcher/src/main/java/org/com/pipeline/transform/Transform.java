package org.com.pipeline.transform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.entity.CompanyOverview;
import org.com.entity.TimeSeriesDaily;
import org.com.utils.SymbolCsvParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class Transform {
    private static final Logger logger = Logger.getLogger(Transform.class.getName());

    public static List<TimeSeriesDaily> transformTimeSeriesDaily(String json, String symbol) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<TimeSeriesDaily> timeSeriesDailies = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode timeSeriesNode = rootNode.path("Time Series (Daily)");

            Iterator<String> dates = timeSeriesNode.fieldNames();

            while(dates.hasNext()){
                String date = dates.next();

                JsonNode dailyData = timeSeriesNode.path(date);

                Double open = dailyData.path("1. open").asDouble();
                Double close = dailyData.path("4. close").asDouble();
                Long volume = dailyData.path("5. volume").asLong();

                //TODO validation
                TimeSeriesDaily tsd = new TimeSeriesDaily(symbol, date, open, close, volume);
                timeSeriesDailies.add(tsd);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        logger.info(String.format("Successfully processed %d records for the %s symbol", timeSeriesDailies.size(), symbol));

        return timeSeriesDailies;
    }


    public static CompanyOverview transformCompanyOverviewData(String json, String symbol) {
        ObjectMapper objectMapper = new ObjectMapper();
        CompanyOverview companyOverview = null;
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            String sector = rootNode.path("Sector").asText();
            String latestQuarter = rootNode.path("LatestQuarter").asText();
            String country = rootNode.path("Country").asText();
            Double analystTargetPrice = rootNode.path("AnalystTargetPrice").asDouble();
            Integer analystStrongBuy = rootNode.path("AnalystRatingStrongBuy").asInt();
            Integer analystBuy = rootNode.path("AnalystRatingBuy").asInt();
            Integer analystHold = rootNode.path("AnalystRatingHold").asInt();
            Integer analystSell = rootNode.path("AnalystRatingSell").asInt();
            Integer analystStrongSell = rootNode.path("AnalystRatingStrongSell").asInt();
            Double percentInsiders = rootNode.path("PercentInsiders").asDouble();
            Double percentInstitutions = rootNode.path("PercentInstitutions").asDouble();
            Double profitMargin = rootNode.path("ProfitMargin").asDouble();
            Double beta = rootNode.path("Beta").asDouble();

            companyOverview =
                  new CompanyOverview(
                        symbol,
                        latestQuarter,
                        country,
                        sector,
                        analystTargetPrice,
                        analystStrongBuy,
                        analystBuy,
                        analystHold,
                        analystSell,
                        analystStrongSell,
                        percentInsiders,
                        percentInstitutions,
                        profitMargin,
                        beta
                  );

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        logger.info(String.format("Successfully processed company overview for the %s symbol", symbol));

        return companyOverview;
    }
}
