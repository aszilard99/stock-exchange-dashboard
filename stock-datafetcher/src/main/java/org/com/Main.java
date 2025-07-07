package org.com;

import org.com.enums.ProcessResult;
import org.com.pipeline.Pipeline;

public class Main {

    public static void main(String[] args) {
        Pipeline etlPipeline = new Pipeline();

        var result = etlPipeline.processTimeSeriesDaily();

        if (result == ProcessResult.SHUTDOWN_REQUESTED) {
            return;
        }

        //etlPipeline.processCompanyOverviewData();

        //TODO make this enabled/disabled from config or cli
        //processSymbols();
    }

}