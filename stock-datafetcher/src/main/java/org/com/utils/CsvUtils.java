package org.com.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {
    public static List<List<String>> parseCsv(String csvData) throws IOException {
        List<List<String>> records = new ArrayList<>();
        try(CSVParser parser = new CSVParser(new StringReader(csvData),CSVFormat.DEFAULT)) {
            for (CSVRecord csvRecord : parser) {
                List<String> record = new ArrayList<>();
                csvRecord.forEach(record::add);
                records.add(record);
            }
            return records;
        }
    }
}
