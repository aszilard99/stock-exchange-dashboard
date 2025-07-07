package org.com.pipeline.load;

import org.com.entity.CompanyOverview;
import org.com.entity.Symbol;
import org.com.entity.TimeSeriesDaily;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Load {
    private static final Logger logger = Logger.getLogger(Load.class.getName());
    private final String username;
    private final String password;
    private final String url;

    public Load(String url, String username, String password) {
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public void loadSymbols(List<Symbol> symbols){
        String sql = "INSERT INTO symbol (code, name, status) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            for (Symbol symbol : symbols) {
                ps.setString(1, symbol.code());
                ps.setString(2, symbol.name());
                ps.setBoolean(3, symbol.status());
                ps.addBatch();
            }

            ps.executeBatch();
        } catch (SQLException e) {
            logger.severe(String.format("Database batch insert failed with error: %s", e.getMessage()));
        }
    }

    public void updateSymbols(List<Symbol> symbols){
        String sql = "UPDATE symbol SET ipo_date = ?, asset_type = ? WHERE code = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            for (Symbol symbol : symbols) {
                ps.setDate(1, Date.valueOf(symbol.ipoDate()));
                ps.setString(2, symbol.assetType());
                ps.setString(3, symbol.code());
                ps.addBatch();
            }

            ps.executeBatch();
        } catch (SQLException e) {
            logger.severe(String.format("Database batch insert failed with error: %s", e.getMessage()));
        }
    }

    public void loadTimeSeriesDailyForSymbol(List<TimeSeriesDaily> timeSeriesDailies, String symbol) {
        String sql = "INSERT INTO time_series_daily (symbol_code, date, open, close, volume) VALUES (?,?,?,?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            for (TimeSeriesDaily tsd : timeSeriesDailies) {
                ps.setString(1, symbol);
                ps.setDate(2,Date.valueOf(tsd.date()));
                ps.setDouble(3, tsd.open());
                ps.setDouble(4, tsd.close());
                ps.setLong(5, tsd.volume());
                ps.addBatch();
            }

            ps.executeBatch();
            logger.info(String.format("Successfully saved time series daily records into the database for %s symbol", symbol));

            updateTimeSeriesDailyUpdateStatusForSymbol(symbol, connection);
        } catch (SQLException e) {
            logger.severe(String.format("Database batch insert failed with error: %s", e.getMessage()));
        }
    }

    public void loadCompanyOverview(CompanyOverview companyOverview, String symbol){
        String sql = "INSERT INTO company_overview " +
                     " (symbol_code, sector, analyst_target_price, analysts_strong_buy, " +
                     " analysts_buy, analysts_hold, analysts_sell, analysts_strong_sell, " +
                     " percent_insiders, percent_institutions, profit_margin, beta, latest_quarter) " +
                     " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, symbol);
            ps.setString(2, companyOverview.sector());
            ps.setDouble(3, companyOverview.analystTargetPrice());
            ps.setInt(4, companyOverview.analystStrongBuy());
            ps.setInt(5, companyOverview.analystBuy());
            ps.setInt(6, companyOverview.analystHold());
            ps.setInt(7, companyOverview.analystSell());
            ps.setInt(8, companyOverview.analystStrongSell());
            ps.setDouble(9, companyOverview.percentInsiders());
            ps.setDouble(10, companyOverview.percentInstitutions());
            ps.setDouble(11, companyOverview.profitMargin());
            ps.setDouble(12, companyOverview.beta());
            ps.setDate(13, Date.valueOf(companyOverview.latestQuarter()));

            ps.execute();
            logger.info(String.format("Successfully saved company overview data into the database for %s symbol", symbol));
        } catch (SQLException e) {
            logger.severe(String.format("Database batch insert failed with error: %s", e.getMessage()));
        }
    }

    //TODO move to another class that, this should only do datatbase saves
    public List<String> getSymbolsWithoutTimeSeriesDailyUpdate() {
        String sql = " SELECT code" +
                     " FROM symbol" +
                     " WHERE ipo_date < '2005-06-01'" +
                     " AND status = true" +
                     " AND code NOT IN (SELECT symbol_code FROM time_series_daily_updates)" +
                     " LIMIT 25";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            List<String> symbols = new ArrayList<>();
            while (resultSet.next()) {
                symbols.add(resultSet.getString("code"));
            }
            logger.info("Successfully queried a batch of symbols without time series daily update. Symbols: " + symbols);

            return symbols;
        } catch (SQLException e) {
            logger.severe(String.format("Selecting symbols without time series daily update failed with error: %s", e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    private void updateTimeSeriesDailyUpdateStatusForSymbol(String symbol, Connection connection) {
        String sql = "INSERT INTO time_series_daily_updates (symbol_code, date) VALUES (?, ?)";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, symbol);
            ps.setDate(2, Date.valueOf(LocalDate.now()));

            ps.execute();
            logger.info("Successfully set time series daily updated status for symbol: " + symbol);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
