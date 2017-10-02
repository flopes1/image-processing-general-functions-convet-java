package com.poli.model.util;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;

import com.poli.model.Image;

public class Histogram
{

    private Image image;
    private JFrame histogramaFrame;
    private HistogramDataset dataset;
    private XYBarRenderer renderer;

    public Histogram(Image image)
    {
        this.image = image;
        this.dataset = new HistogramDataset();
        this.buildLayout();
    }

    private ChartPanel createChartPanel()
    {

        this.dataset = new HistogramDataset();

        double[] pixelsDistribuition = new double[this.image.getCols() * this.image.getRows()];
        pixelsDistribuition = this.image.getSource().getRaster().getSamples(0, 0, this.image.getCols(),
                this.image.getRows(), 0, pixelsDistribuition);

        this.dataset.addSeries("Gray", pixelsDistribuition, 256);

        JFreeChart chart = ChartFactory.createHistogram("Histograma", "Pixel", "Quantidade", this.dataset,
                PlotOrientation.VERTICAL, true, true, false);

        XYPlot plot = (XYPlot) chart.getPlot();
        this.renderer = (XYBarRenderer) plot.getRenderer();
        this.renderer.setBarPainter(new StandardXYBarPainter());
        this.renderer.setSeriesVisible(0, true);

        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);

        return panel;
    }

    private void buildLayout()
    {
        this.histogramaFrame = new JFrame("Histograma");
        this.histogramaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.histogramaFrame.add(this.createChartPanel());
        this.histogramaFrame.pack();
        this.histogramaFrame.setLocationRelativeTo(null);
    }

    public void display()
    {
        this.histogramaFrame.setVisible(true);
    }

}
