package org.com.transform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.entity.TimeSeriesDaily;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Transform {
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
        return timeSeriesDailies;
    }
}
