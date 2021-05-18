/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JPanel;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.colors.XChartSeriesColors;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.SeriesMarkers;

/**
 *
 * @author Odorifqi
 */
public class ChartPanel extends JPanel {

    private XYChart chart;
    private XYSeries series1;
    private XYSeries series2;
    private XChartPanel<XYChart> chartBox;

    public ChartPanel() {
        chart = new XYChartBuilder().width(400).height(250).title("Grafik").xAxisTitle("Time (Month)").yAxisTitle("Price (Point)").build();
        chart.getStyler().setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.GREY));
        chart.getStyler().setPlotGridLinesColor(new Color(255, 255, 255));
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setLegendBackgroundColor(Color.PINK);
        chart.getStyler().setChartFontColor(Color.BLACK);
        chart.getStyler().setChartTitleBoxBackgroundColor(Color.CYAN);
        chart.getStyler().setChartTitleBoxVisible(true);
        chart.getStyler().setChartTitleBoxBorderColor(Color.BLACK);
        chart.getStyler().setPlotGridLinesVisible(false);
        chart.getStyler().setAxisTickPadding(3);
        chart.getStyler().setAxisTickMarkLength(5);
        chart.getStyler().setPlotMargin(5);
        chart.getStyler().setChartTitleFont(new Font("Helvetica", Font.BOLD, 11));
        chart.getStyler().setLegendFont(new Font("Helvetica", Font.PLAIN, 10));
        chart.getStyler().setLegendPosition(LegendPosition.InsideNE);
        chart.getStyler().setLegendPadding(3);
        chart.getStyler().setLegendSeriesLineLength(10);
        chart.getStyler().setAxisTitleFont(new Font("Helvetica", Font.BOLD, 10));
        chart.getStyler().setAxisTickLabelsFont(new Font("Helvetica", Font.PLAIN, 10));
        chart.getStyler().setDatePattern("MM/yy");
        chart.getStyler().setDecimalPattern("#0.0");
        chart.getStyler().setLocale(Locale.ENGLISH);
        chart.getStyler().setXAxisTickMarkSpacingHint(50);
        chart.getStyler().setYAxisTickMarkSpacingHint(50);
    }
    
    public XChartPanel<XYChart> EmptyChart(){
        chart.setTitle("Grafik");
        removeSeries();
        chart.getStyler().setAxisTicksVisible(false);
        chartBox = new XChartPanel<XYChart>(chart);
        setLayout(new BorderLayout());
        add(chartBox, BorderLayout.CENTER);
            
        return chartBox;
    }
    
    public XChartPanel<XYChart> RawChart(List<Date> listDate, List<Double> listDouble, File file ){
        chart.setTitle("" + file);
        removeSeries();
        chart.getStyler().setAxisTicksVisible(true);
        setLayout(new BorderLayout());
        chartBox = new XChartPanel<>(chart);
        series1 = chart.addSeries("Raw Data", listDate, listDouble);
        setSeries1();
        add(chartBox, BorderLayout.CENTER);
            
        return chartBox;
    }
    
    public XChartPanel<XYChart> PredictChart(List<Date> listDate, List<Double> listDouble, List<Double> listPredict, int interval){
        chart.setTitle("Interval: " + interval);
        removeSeries();
        chart.getStyler().setAxisTicksVisible(true);
        
        setLayout(new BorderLayout());
        chartBox = new XChartPanel<XYChart>(chart);
        
        series1 = chart.addSeries("Raw Data", listDate, listDouble);
        series2 = chart.addSeries("Prediction Data", listDate, listPredict);
        
        setSeries1();
        series2.setLineColor(XChartSeriesColors.RED);
        series2.setMarkerColor(Color.BLACK);
        series2.setMarker(SeriesMarkers.NONE);
        series2.setLineStyle(SeriesLines.DOT_DOT);
        add(chartBox, BorderLayout.CENTER);
            
        return chartBox;
    }
    
    public void removeSeries(){
        chart.removeSeries("Raw Data");
        chart.removeSeries("Prediction Data");
    }
    
    public void setSeries1(){
        series1.setLineColor(XChartSeriesColors.BLUE);
        series1.setMarkerColor(Color.WHITE);
        series1.setMarker(SeriesMarkers.NONE);
        series1.setLineStyle(SeriesLines.SOLID);
    }
}
    
