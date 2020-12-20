package DB;

import java.io.*;
import java.sql.*;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.FileReader;

/**
 * Provides methods to manage connection with the Users database implementation
 */
public class UsersDBConnection {
    private static final String DB_URL = "jdbc:h2:./db/Users";
    private static final String DB_Driver = "org.h2.Driver";
    private static Connection connection;

    static Logger logger;

    static{
        logger = Logger.getLogger(UsersDB.class.getName());
        try{
            Class.forName(DB_Driver);
            logger.log(Level.INFO ,"Driver was loaded");
        } catch (ClassNotFoundException e){
            logger.log(Level.SEVERE ,"Driver not Found");
            logger.log(Level.SEVERE , Arrays.toString(e.getStackTrace()));
        }
    }

    public static Connection openConnection() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            logger.log(Level.WARNING ,"Failed to connect to Users DB");
            return null;
        }
        logger.info("Opened connection to Users DB");
        return connection;
    }

    public static void reopenConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = openConnection();
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING ,"Failed to reopen connection to Users DB");
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            logger.log(Level.WARNING ,"Failed to close connection to Users DB");
        }
        logger.info("Closed connection to Users DB");
    }

    public static Connection getConnection(){
        reopenConnection();
        return connection;
    }

    private static boolean rollbackCommit(Savepoint save, boolean transactionComplete){
        if (transactionComplete){
            return true;
        }
        try {
            connection.rollback(save);
        } catch (SQLException sqlException) {
            logger.log(Level.SEVERE ,"UsersDBConnection failed to rollback commit");
            logger.log(Level.SEVERE , Arrays.toString(sqlException.getStackTrace()));
        }
        return false;
    }

    public static String[] parseQueryFromFile(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while((line = bufferedReader.readLine()) != null)
        {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        fileReader.close();

        return stringBuilder.toString().split(";");
    }

    public static boolean executeSqlUpdateQueryFromFile(File file){
        boolean transactionComplete = true;
        Savepoint save;
        try{
            reopenConnection();
            save = connection.setSavepoint();
        } catch (SQLException sqlException){
            logger.log(Level.SEVERE ,"UsersDBConnection failed to create savepoint");
            logger.log(Level.SEVERE , Arrays.toString(sqlException.getStackTrace()));
            return false;
        }

        try{
            String[] queryParts = parseQueryFromFile(file);
            reopenConnection();
            Statement statement = connection.createStatement();

            for (String queryPart : queryParts) {
                String trimmedQueryPart = queryPart.trim();
                if (!trimmedQueryPart.isEmpty()) {
                    statement.executeUpdate(trimmedQueryPart);
                }
            }
            connection.commit();

        } catch (FileNotFoundException fileException){
            transactionComplete = false;
            logger.log(Level.SEVERE ,"UsersDBConnection failed to open file with sql query");
            logger.log(Level.SEVERE , Arrays.toString(fileException.getStackTrace()));
        } catch (IOException ioException){
            transactionComplete = false;
            logger.log(Level.SEVERE ,"UsersDBConnection failed to read file with sql query");
            logger.log(Level.SEVERE , Arrays.toString(ioException.getStackTrace()));
        } catch (SQLException sqlException){
            transactionComplete = false;
            logger.log(Level.SEVERE ,"UsersDBConnection failed to execute query");
            logger.log(Level.SEVERE , Arrays.toString(sqlException.getStackTrace()));
        }
        return rollbackCommit(save, transactionComplete);
    }

    public static ResultSet executeSqlSelectQueryFromFile(File file){
        try{
            reopenConnection();
            Statement statement = connection.createStatement();
            String b = parseQueryFromFile(file)[0].trim();
            System.out.println(b);
            return statement.executeQuery(parseQueryFromFile(file)[0].trim());

        } catch (FileNotFoundException fileException){
            logger.log(Level.SEVERE ,"UsersDBConnection failed to open file with sql query");
            logger.log(Level.SEVERE , Arrays.toString(fileException.getStackTrace()));
        } catch (IOException ioException){
            logger.log(Level.SEVERE ,"UsersDBConnection failed to read file with sql query");
            logger.log(Level.SEVERE , Arrays.toString(ioException.getStackTrace()));
        } catch (SQLException sqlException){
            logger.log(Level.SEVERE ,"UsersDBConnection failed to execute query");
            logger.log(Level.SEVERE , Arrays.toString(sqlException.getStackTrace()));
        }
        return null;
    }

    public static ResultSet justExec(String string){
        try{
            reopenConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(string);
        } catch (SQLException sqlException){
            logger.log(Level.SEVERE ,"UsersDBConnection failed to execute query");
            logger.log(Level.SEVERE , Arrays.toString(sqlException.getStackTrace()));
        }
        return null;
    }

}