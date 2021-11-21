package main.database;

import main.model.Constructor;
import main.model.Race;
import main.util.PrintablePreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";

    private static DatabaseConnectionHandler instance;
    private Connection connection = null;

    private DatabaseConnectionHandler() {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            login("ora_kbarutcu", "a25056623");
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public static DatabaseConnectionHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseConnectionHandler();
        }
        return instance;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    public List<Race> getRaceResults() {
        List<Race> races = new ArrayList<>();

        try {
            String query = "SELECT * " +
                           "FROM RACE, DRIVER, DRIVEPLACESINRACE " +
                           "WHERE RACE.RACENAME = DRIVEPLACESINRACE.RACENAME AND DRIVEPLACESINRACE.RANK = 1 " +
                           "AND DRIVER.DRIVERNUMBER = DRIVEPLACESINRACE.DRIVERNUMBER";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Race model = new Race(rs.getString("RaceName"), rs.getInt("Laps"),
                                      rs.getDate("EndDate"), rs.getFloat("FastestLapAverageSpeed"),
                                      rs.getString("CircuitName"), rs.getString("DriverName"),
                                      rs.getString("ConstructorName"));
                races.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return races;
    }

    public List<Constructor> getConstructorResults() {
        List<Constructor> constructors = new ArrayList<>();

        try {
            String query = "SELECT * " +
                    "FROM CONSTRUCTOR ";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Constructor model = new Constructor(rs.getString("ConstructorName"), rs.getInt("Position"), rs.getFloat("Points"));

                constructors.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return constructors;
    }




    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }


}
