package org.com;

import org.com.entity.Symbol;
import org.com.entity.TimeSeriesDaily;
import org.com.pipeline.Pipeline;
import org.com.pipeline.extract.Extract;
import org.com.pipeline.load.Load;
import org.com.pipeline.transform.Transform;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Pipeline etlPipeline = new Pipeline();

        //etlPipeline.processTimeSeriesDaily();

        etlPipeline.processCompanyOverviewData();

        //TODO make this enabled/disabled from config or cli
        //processSymbols();
    }

}