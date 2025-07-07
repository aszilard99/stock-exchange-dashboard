package org.com.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.com.entity.Symbol;
import org.com.utils.DateUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class SymbolCsvReader {
    private static final Logger logger = Logger.getLogger(SymbolCsvReader.class.getName());

    public static List<Symbol> readSymbols(String csvData) {
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
        } catch (IOException e) {
            logger.severe("Could not parse csv symbol data.");
            throw new RuntimeException(e);
        }
    }

    @Nullable
    private static Symbol createSymbol(CSVRecord csvRecord) {
        try {
            String code = csvRecord.get("symbol");
            String name = csvRecord.get("name");
            String assetType = csvRecord.get("assetType");
            LocalDate ipoDate = DateUtils.parse(csvRecord.get("ipoDate"));
            String status = csvRecord.get("status");

            return new Symbol(code, name, Objects.equals(status, "Active"), ipoDate, assetType);
        } catch (Exception e) {
            logger.severe(String.format("Could not create symbol from csvRecord %s. Exception message: %s", csvRecord.toString(), e.getMessage()));
            return null;
        }
    }

    private static boolean isValid(Symbol symbol) {
        return symbol != null && symbol.code() != null;
    }
}

