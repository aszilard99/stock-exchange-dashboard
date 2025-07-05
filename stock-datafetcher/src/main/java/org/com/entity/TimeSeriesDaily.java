package org.com.entity;

public record TimeSeriesDaily (String symbol, String date, Double open, Double close, Long volume){}
