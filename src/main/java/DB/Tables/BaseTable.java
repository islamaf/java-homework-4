package DB.Tables;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import DB.UsersDBConnection;

public abstract class BaseTable {
    protected String tableName;
    static Logger logger;

    static {
        logger = Logger.getLogger(BaseTable.class.getName());
    }

    BaseTable(String tableName){
        this.tableName = tableName;
    }

    public String getTableName(){
        return tableName;
    }

    public abstract void createTable();

    public abstract void addInsertionToBatch(PreparedStatement preparedStatement, String[] values) throws SQLException;

    public abstract PreparedStatement getStatement() throws SQLException;

    public void insertDataFromTxt(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            PreparedStatement preparedStatement = getStatement();
            String line;
            String[] new_line;
            while ((line = br.readLine()) != null) {
                new_line = line.split("\\t+");
                addInsertionToBatch(preparedStatement, new_line);
            }
            int[] rows = preparedStatement.executeBatch();
            UsersDBConnection.getConnection().commit();
            int addedLinesNumber = Arrays.stream(rows).sum();
            logger.log(Level.INFO , String.format("Added %d lines to %s", addedLinesNumber, tableName));
        } catch (FileNotFoundException fileNotFound){
            logger.log(Level.SEVERE ,"Failed to read csv file");
        }
        catch (IOException | SQLException exception){
            logger.log(Level.SEVERE, exception.toString());
        }
    }

    public static Timestamp parseTimestampWithTimezone(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String format_new = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(format);
        return Timestamp.valueOf(format_new);
//            return format_new;
    }
}