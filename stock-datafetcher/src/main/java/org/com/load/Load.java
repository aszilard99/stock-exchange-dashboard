package org.com.load;

import org.com.entity.Symbol;
import org.com.entity.TimeSeriesDaily;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public void loadTimeSeriesDailyForSymbol(List<TimeSeriesDaily> timeSeriesDailies, String symbol) {
        String sql = "INSERT INTO time_series_daily (symbol_code, date, open, close, volume) VALUES (?,?,?,?,?)";
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement ps = connection.prepareStatement(sql);
            for (TimeSeriesDaily tsd : timeSeriesDailies) {
                ps.setString(1, symbol);
                ps.setString(2, tsd.date());
                ps.setDouble(3, tsd.open());
                ps.setDouble(4, tsd.close());
                ps.setLong(5, tsd.volume());
                ps.addBatch();
            }

            ps.executeBatch();
        } catch (SQLException e) {
            logger.severe(String.format("Database batch insert failed with error: %s", e.getMessage()));
        }
    }
}
