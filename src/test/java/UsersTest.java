import DB.UsersChartBuilder;
import DB.UsersDB;
import DB.UserXlsxTableBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import DB.QueryRunner;
import org.junit.jupiter.api.*;

import static DB.UsersDB.deleteFile;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsersTest {
    static QueryRunner queryRunner;

    @BeforeAll
    static void setup() {
        queryRunner = new QueryRunner(new UsersDB());
        System.out.println("DB is ready");
    }

//    @AfterAll
//    static void removeDB() {
//        assertTrue(deleteFile(new File("./db")));
//        assertTrue(deleteFile(new File("./csv_data")));
//        System.out.println("DB deleted");
//    }

    @Test
    void queryB1Test() {
        ArrayList<String[]> result = queryRunner.TopNActive(5);
        assertNotNull(result);
        for (String[] row : result) {
            System.out.println(row[0] + " " + row[1] + " " + row[2] + " " + row[3] + " ");
        }
    }

    @Test
    void queryB2Test() {
        ArrayList<String[]> result = queryRunner.TotalRecordsPerDay(20140106);
        assertNotNull(result);
        for (String[] row : result) {
            System.out.println(row[0] + " " + row[1]);
        }
    }

    @Test
    void queryB3Test() {
        ArrayList<String[]> result = queryRunner.MostPopular3Websites(60, 25);
        assertNotNull(result);
        for (String[] row : result) {
            System.out.println(row[0] + " " + row[1]);
        }
    }

    @Test
    void queryB4Test() {
        int result = queryRunner.NewInsertion("1.1.1.1", "2015-06-25 06:11:09", "http://news.rambler.ru/3105790", 456, 400, "Chrome/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)n");
        System.out.println(String.format("Inserted %d rows\n", result));
    }

    @Test
    void queryB5Test() {
        int result = queryRunner.DeleteValuesBeforeSpecificDate("2014-01-06 00:00:00");
        System.out.println(String.format("Deleted %d rows\n", result));
    }

    @Test
    void getAllTables() {
        try {
            UserXlsxTableBuilder tableBuilder = new UserXlsxTableBuilder(queryRunner);
            tableBuilder.b1CreateTable(5);
            tableBuilder.b2CreateTable(20140106);
            tableBuilder.b3CreateTable(30, 5);
        } catch (IOException e) {
            assert false;
        }
    }

    @Test
    void buildAllCharts() {
        final String OUTPUT_PNG_PATH = "./Charts/";
        try {
            UsersChartBuilder chartBuilder = new UsersChartBuilder(queryRunner);
            chartBuilder.query1CreateBarChart(OUTPUT_PNG_PATH + "query1BarChart.png", 5);
            chartBuilder.query2CreateBarCharts(OUTPUT_PNG_PATH + "query2BarChart.png", 20140106);
            chartBuilder.query3CreateBarCharts(OUTPUT_PNG_PATH + "query3BarChart.png", 30, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}