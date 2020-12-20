package DB.Tables;

import DB.UsersDBConnection;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class UserData extends BaseTable{
    public UserData() throws SQLException {
        super("user_data");
    }

    public void createTable() {
        File createTableQuery = new File("./src/main/sqlQueries/UserDataCreateTable.sql");
        if (!UsersDBConnection.executeSqlUpdateQueryFromFile(createTableQuery)){
            logger.log(Level.SEVERE ,"Failed to create UserData table");
        } else{
            logger.info("Created UserData table");
        }
    }

    public PreparedStatement getStatement() throws SQLException {
        String query = String.format("INSERT INTO %s VALUES (?, ?, ?, ?)", tableName);
        return UsersDBConnection.getConnection().prepareStatement(query);
    }

    public void addInsertionToBatch(PreparedStatement preparedStatement, String[] values) throws SQLException {
        preparedStatement.setString(1, values[0]);
        preparedStatement.setString(2, values[1]);
        preparedStatement.setString(3, values[2]);
        preparedStatement.setInt(4, Integer.parseInt(values[3]));
        preparedStatement.addBatch();
    }
}