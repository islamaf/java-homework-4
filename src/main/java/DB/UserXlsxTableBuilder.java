package DB;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.openxmlformats.schemas.drawingml.x2006.main.STAdjAngle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Creates Excel tables representing results of QueryRunner
 */
public class UserXlsxTableBuilder {
    private final static String OUTPUT_XLS_PATH = "./Reports/";
    private final QueryRunner queryRunner;

    public UserXlsxTableBuilder(QueryRunner runner) {
        queryRunner = runner;
    }

    private void buildExcelTable(String heading, String[] columnNames,
                                 Iterable<String[]> data, String filePath) throws IOException {
        try(Workbook book = new HSSFWorkbook()){
            Sheet sheet = book.createSheet(heading);
            Row firstRow = sheet.createRow(0);

            CellStyle style = book.createCellStyle();
            Font font = book.createFont();
            font.setFontHeightInPoints((short) 10);
            font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
            style.setFont(font);
            style.setLocked(true);

            // Fill first row with columns names
            int j = 0;
            for (String rawCell : columnNames) {
                Cell currentCell = firstRow.createCell(j++);
                currentCell.setCellValue(rawCell);
                currentCell.setCellStyle(style);
            }

            // Fill all the rest rows
            int i = 1;
            for (String[] rawRow : data) {
                Row row = sheet.createRow(i++);
                j = 0;
                for (String rawCell : rawRow) {
                    Cell currentCell = row.createCell(j++);
                    currentCell.setCellValue(rawCell);
                }
            }

            // Resize columns to fit their data
            for (int x = 0; x < sheet.getRow(0).getPhysicalNumberOfCells(); x++) {
                sheet.autoSizeColumn(x);
            }

            // Save the table
            book.write(new FileOutputStream(new File(filePath)));
        }
    }

    public void b1CreateTable(int N) throws IOException {
        buildExcelTable("Top Active users", new String[]{"User_ip", "Gender", "Age", "Count"},
                queryRunner.TopNActive(N), OUTPUT_XLS_PATH + "task_1.xlsx");
    }

    public void b2CreateTable(int date) throws IOException {
        buildExcelTable("Total records for genders in a certain date", new String[]{"Count", "Date"},
                queryRunner.TotalRecordsPerDay(date), OUTPUT_XLS_PATH + "task_2.xlsx");
    }

    public void b3CreateTable(int age, int day) throws IOException {
        buildExcelTable("Shortest routes", new String[]{"Website", "ips"},
                queryRunner.MostPopular3Websites(age, day), OUTPUT_XLS_PATH + "task_3.xlsx");
    }
}