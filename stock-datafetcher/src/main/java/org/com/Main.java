package org.com;

import org.com.pipeline.Pipeline;

public class Main {

    public static void main(String[] args) {
        Pipeline etlPipeline = new Pipeline();

        etlPipeline.processTimeSeriesDaily();

        //etlPipeline.processCompanyOverviewData();

        //TODO make this enabled/disabled from config or cli
        //processSymbols();
    }

}