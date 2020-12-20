package DB;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class QueryRunner {
    private final static String QUERIES_PATH = UsersDB.getQueriesPath();
    static Logger logger;
    private final UsersDB usersDB;
    static{
        logger = Logger.getLogger(QueryRunner.class.getName());
    }

    public QueryRunner(UsersDB db){
        usersDB = db;
    }

    /**
     * @return Top N active users
     */
    public ArrayList<String[]> TopNActive(int N) {
        ArrayList<String[]> returnValue = new ArrayList<>();
        try {
            String query = UsersDBConnection.parseQueryFromFile(new File(QUERIES_PATH + "Tasks/task_1.sql"))[0];
            PreparedStatement preparedStatement = UsersDBConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, N);
            ResultSet queryResult = preparedStatement.executeQuery();
            queryResult.beforeFirst();
            while (queryResult.next()) {
                String[] result = new String[4];
                result[0] = queryResult.getString("age");
                result[1] = queryResult.getString("top_count");
                result[2] = queryResult.getString("user_ip");
                result[3] = queryResult.getString("gender");
                returnValue.add(result);
            }
            queryResult.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    /**
     * @return Total records in a day
     */
    public ArrayList<String[]> TotalRecordsPerDay(int date) {
        ArrayList<String[]> returnValue = new ArrayList<>();
        try {
            String query = UsersDBConnection.parseQueryFromFile(new File(QUERIES_PATH + "Tasks/task_2.sql"))[0];
            PreparedStatement preparedStatement = UsersDBConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, date);
            ResultSet queryResult = preparedStatement.executeQuery();
            queryResult.beforeFirst();
            while (queryResult.next()) {
                String[] result = new String[2];
                result[0] = queryResult.getString("query_time");
                result[1] = queryResult.getString("top_count");
                returnValue.add(result);
            }
            queryResult.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    /**
     * @return Most popular 3 websites
     */
    public ArrayList<String[]> MostPopular3Websites(int age, int day) {
        ArrayList<String[]> returnValue = new ArrayList<>();
        try {
            String query = UsersDBConnection.parseQueryFromFile(new File(QUERIES_PATH + "Tasks/task_3.sql"))[0];
            PreparedStatement preparedStatement = UsersDBConnection.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, age);
            preparedStatement.setInt(2, day);
            ResultSet queryResult = preparedStatement.executeQuery();
            queryResult.beforeFirst();
            while (queryResult.next()) {
                String[] result = new String[2];
                result[0] = queryResult.getString("website");
                result[1] = queryResult.getString("ips");
                returnValue.add(result);
            }
            queryResult.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    /**
     * @return Number of inserted rows
     */
    public int NewInsertion(String ip, String date, String website, int size, int status, String browser) {
        try {
            String[] query = UsersDBConnection.parseQueryFromFile(new File(QUERIES_PATH + "Tasks/task_4.sql"));
            PreparedStatement updateTable = UsersDBConnection.getConnection().prepareStatement(query[0]);
            updateTable.setString(1, ip);
            updateTable.setString(2, date);
            updateTable.setString(3, website);
            updateTable.setInt(4, size);
            updateTable.setInt(5, status);
            updateTable.setString(6, browser);
            return updateTable.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @return Number of deleted rows
     */
    public int DeleteValuesBeforeSpecificDate(String date) {
        try {
            String query = UsersDBConnection.parseQueryFromFile(new File(QUERIES_PATH + "Tasks/task_5.sql"))[0];
            PreparedStatement preparedStatement = UsersDBConnection.getConnection().prepareStatement(query);
            preparedStatement.setString(1, date);
            return preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}