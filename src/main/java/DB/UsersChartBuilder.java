package DB;

import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Creates charts representing the results of some QueryRunner queries
 */
public class UsersChartBuilder {
    private final QueryRunner queryRunner;
    private static final int chartWidth = 1920;
    private static final int chartHeight = 1080;

    public UsersChartBuilder(QueryRunner runner) {
        queryRunner = runner;
    }

    private DefaultCategoryDataset fillDataset(List<String[]> data) {
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String[] row : data) {
            dataset.addValue(Double.parseDouble(row[1]), "default", row[0]);
        }
        return dataset;
    }

    private void setupChart(JFreeChart barChart, String title) {
        CategoryPlot plot = barChart.getCategoryPlot();
        CategoryAxis axis = plot.getDomainAxis();

        Font font = new Font("Cambria", Font.BOLD, 25);
        axis.setTickLabelFont(font);
        Font font3 = new Font("Cambria", Font.BOLD, 30);
        barChart.setTitle(new org.jfree.chart.title.TextTitle(title, new java.awt.Font("Cambria", java.awt.Font.BOLD, 40)));

        plot.getDomainAxis().setLabelFont(font3);
        plot.getRangeAxis().setLabelFont(font3);
        CategoryPlot categoryPlot = (CategoryPlot) barChart.getPlot();
        BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
    }

    public void query1CreateBarChart(String filepath, int N) throws IOException {
        List<String[]> data = queryRunner.TopNActive(N);
        DefaultCategoryDataset dataset = fillDataset(data);
        String title = String.format("Top %d active users", N);
        String categoryAxis = "Month";
        String valueAxis = "Number of flights";

        JFreeChart barChart = ChartFactory.createBarChart(title, categoryAxis, valueAxis, dataset,
                PlotOrientation.VERTICAL, false, false, false);
        setupChart(barChart, title);
        ChartUtilities.saveChartAsPNG(new File(filepath), barChart, chartWidth, chartHeight);
    }

    public void query2CreateBarCharts(String filepath, int date) throws IOException {
        for (int i = 0; i < 2; i++) {
            List<String[]> data = queryRunner.TotalRecordsPerDay(date);
            DefaultCategoryDataset dataset = fillDataset(data);
            String title = String.format("Total records in %d", date);
            String categoryAxis = "Weekday";
            String valueAxis = "Number of flights";

            JFreeChart barChart = ChartFactory.createBarChart(title, categoryAxis, valueAxis, dataset,
                    PlotOrientation.VERTICAL, false, false, false);
            setupChart(barChart, title);
            ChartUtilities.saveChartAsPNG(new File(filepath), barChart, chartWidth, chartHeight);
        }
    }

    public void query3CreateBarCharts(String filepath, int age, int day) throws IOException {
        List<String[]> data = queryRunner.MostPopular3Websites(age, day);
        DefaultCategoryDataset dataset = fillDataset(data);
        String title = String.format("Most popular 3 websites for age %d and in the last %d days", age, day);
        String categoryAxis = "Date";
        String valueAxis = "Foregone earnings";

        JFreeChart barChart = ChartFactory.createBarChart(title, categoryAxis, valueAxis, dataset,
                PlotOrientation.VERTICAL, false, false, false);
        setupChart(barChart, title);
        ChartUtilities.saveChartAsPNG(new File(filepath), barChart, chartWidth, chartHeight);
    }
}