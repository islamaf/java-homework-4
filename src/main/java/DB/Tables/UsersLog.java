package DB.Tables;

import DB.UsersDBConnection;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;

public class UsersLog extends BaseTable{
    public UsersLog(){
        super("users_logs");
    }

    public void createTable() {
        File createTableQuery = new File("./src/main/sqlQueries/UsersLogCreateTable.sql");
        if (!UsersDBConnection.executeSqlUpdateQueryFromFile(createTableQuery)){
            logger.log(Level.SEVERE ,"Failed to create UsersLog table");
        } else{
            logger.info("Created UsersLog table");
        }
    }

    public PreparedStatement getStatement() throws SQLException {
        String query = String.format("INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?)", tableName);
        return UsersDBConnection.getConnection().prepareStatement(query);
    }

    public void addInsertionToBatch(PreparedStatement preparedStatement, String[] values){
        try {
            Date format = new SimpleDateFormat("yyyyMMddHHmmss").parse(values[1]);
            String format_new = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(format);

            String[] browser_string = values[5].split(" ");
            preparedStatement.setString(1, values[0]);
//            preparedStatement.setTimestamp(2, BaseTable.parseTimestampWithTimezone(values[1]));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(format_new));
            preparedStatement.setString(3, values[2]);
            preparedStatement.setInt(4, Integer.parseInt(values[3]));
            preparedStatement.setInt(5, Integer.parseInt(values[4]));
            preparedStatement.setString(6, browser_string[0]);
            preparedStatement.addBatch();
        } catch (SQLException | ParseException e) {
            logger.log(Level.SEVERE , Arrays.toString(e.getStackTrace()));
        }
    }
}