package org.com.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.com.entity.Symbol;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class SymbolCsvParser {
    private static final Logger logger = Logger.getLogger(SymbolCsvParser.class.getName());

    public static List<Symbol> parseCsvSymbol(String csvData) throws IOException {
        List<Symbol> symbols = new ArrayList<>();
        try(CSVParser parser = new CSVParser(new StringReader(csvData),CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord csvRecord : parser) {

                Symbol symbol = createSymbol(csvRecord);
                if (isValid(symbol)) {
                    symbols.add(symbol);
                } else {
                    logger.warning(String.format("Symbol was invalid and was thrown away: %s.", symbol));
                }
            }
            return symbols;
        }
    }

    @Nullable
    private static Symbol createSymbol(CSVRecord csvRecord) {
        List<String> record = new ArrayList<>();
        csvRecord.forEach(record::add);

        if (record.size() < 7) {
            return null;
        }
        String code = record.get(0);
        String name = record.get(1);
        String status = record.get(6);

        return new Symbol(code, name, Objects.equals(status, "Active"));
    }

    private static boolean isValid(Symbol symbol) {
        return symbol != null && symbol.code() != null;
    }
}

