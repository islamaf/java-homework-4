package DB;

import DB.Tables.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provides methods to work with the Users database
 */
public class UsersDB {
    private final static String TXT_PATH = "./csv_data/";
    private final static String QUERIES_PATH = "./src/main/sqlQueries/";
    private final static String DATA_SOURCE_1 = "https://drive.google.com/uc?id=1ifEvF8w2z9InLelvGmoiAiZg2LJ6K1me";
//    https://drive.google.com/uc?id=1ifEvF8w2z9InLelvGmoiAiZg2LJ6K1me
    private final static String DATA_SOURCE_2 = "https://drive.google.com/uc?id=1kjF7z-eEU6kP8er_BRGC_I9JXb4e2xNu";
//    https://drive.google.com/uc?id=1kjF7z-eEU6kP8er_BRGC_I9JXb4e2xNu
    private List<BaseTable> tables;

    public static String getQueriesPath(){
        return QUERIES_PATH;
    }

    public UsersDB(){
        try {
            tables = new ArrayList<>(Arrays.asList(new UsersLog(), new UserData()));

            File dbFolder = new File("./db");
            if (dbFolder.exists()) {
                deleteFile(dbFolder);
            }
            assert dbFolder.mkdir();

            downloadTxt();
            dropAllTables();
            createTables();
            loadTablesFromCsv();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            UsersDBConnection.closeConnection();
        }
    }

    public void dropAllTables() {
        UsersDBConnection.executeSqlUpdateQueryFromFile(new File(QUERIES_PATH + "DropAllTables.sql"));
    }

    public void createTables() {
        for (BaseTable table : tables) {
            table.createTable();
        }
    }

    private void loadTablesFromCsv() {
        for (BaseTable table : tables) {
            table.insertDataFromTxt(TXT_PATH + table.getTableName() + ".txt ");
        }
    }

    private void downloadFile(String file_url, String fileName) throws IOException {
        URL url = new URL(file_url);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla 5.0 (Windows; U; " + "Windows NT 5.1; en-US; rv:1.8.0.11) ");
        InputStream inputStream = connection.getInputStream();
        Path path = new File(UsersDB.TXT_PATH + fileName).toPath();
        Files.copy(inputStream, path);
    }

    public void downloadTxt() throws IOException {
        File csvFolder = new File(TXT_PATH);
        if (csvFolder.exists()) {
            deleteFile(csvFolder);
        }
        assert csvFolder.mkdir();
        for (BaseTable table : tables) {
            String tableName = table.getTableName();
            if(tableName.equals("users_logs")) {
                downloadFile(DATA_SOURCE_1, tableName + ".txt");
            } else {
                downloadFile(DATA_SOURCE_2, tableName + ".txt");
            }
        }
    }

    public static boolean deleteFile(File fileToDelete) {
        File[] allContents = fileToDelete.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteFile(file);
            }
        }
        return fileToDelete.delete();
    }

    public ResultSet executeSelectQuery(String sqlFileName) {
        return UsersDBConnection.executeSqlSelectQueryFromFile(new File(QUERIES_PATH + sqlFileName));
    }

    public ResultSet justExec(String sqlFileName) {
        return UsersDBConnection.justExec(sqlFileName);
    }
}