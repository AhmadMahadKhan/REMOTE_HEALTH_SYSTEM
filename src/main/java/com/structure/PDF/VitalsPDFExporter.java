package com.structure.PDF;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.structure.HealthDataHandling.VitalSign;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VitalsPDFExporter {

    public static void exportVitalsToPDF(String patientId, List<VitalSign> vitals, String savePath) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(savePath));
        document.open();

        document.add(new Paragraph("Patient Vitals Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        document.add(new Paragraph("Patient ID: " + patientId));
        document.add(new Paragraph("Date Generated: " + java.time.LocalDate.now()));
        document.add(new Paragraph("\n"));

        // Add all graphs
        document.add(new Paragraph("Temperature Chart:"));
        addChart(document, vitals, "Temperature (°C)", "Temperature", "temp");

        document.add(new Paragraph("Heart Rate Chart:"));
        addChart(document, vitals, "Heart Rate (BPM)", "Heart Rate", "heartRate");

        document.add(new Paragraph("Oxygen Level Chart:"));
        addChart(document, vitals, "Oxygen (%)", "Oxygen Level", "oxygen");

        document.add(new Paragraph("Blood Pressure Chart:"));
        addBPChart(document, vitals);

        // Summary table
        document.add(new Paragraph("\nSummary:"));
        double avgTemp = vitals.stream().mapToDouble(v -> v.getTemp()).average().orElse(0);
        double avgHR = vitals.stream().mapToInt(v -> v.getHeartRate()).average().orElse(0);
        double avgOxy = vitals.stream().mapToInt(v -> v.getOxygenLevel()).average().orElse(0);

        document.add(new Paragraph("Avg Temperature: " + String.format("%.2f", avgTemp) + " °C"));
        document.add(new Paragraph("Avg Heart Rate: " + String.format("%.2f", avgHR) + " BPM"));
        document.add(new Paragraph("Avg Oxygen Level: " + String.format("%.2f", avgOxy) + " %"));

        document.close();
    }

    private static void addChart(Document document, List<VitalSign> vitals, String yAxis, String seriesName, String field) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (VitalSign v : vitals) {
            String date = v.getDateTime().format(formatter);
            switch (field) {
                case "temp" -> dataset.addValue(v.getTemp(), seriesName, date);
                case "heartRate" -> dataset.addValue(v.getHeartRate(), seriesName, date);
                case "oxygen" -> dataset.addValue(v.getOxygenLevel(), seriesName, date);
            }
        }

        JFreeChart chart = ChartFactory.createLineChart(
                seriesName + " Over Time", "Date", yAxis, dataset,
                PlotOrientation.VERTICAL, true, true, false
        );

        BufferedImage image = chart.createBufferedImage(450, 300);
        File imgFile = File.createTempFile(field + "_chart", ".png");
        ImageIO.write(image, "png", imgFile);
        Image chartImg = Image.getInstance(imgFile.getAbsolutePath());
        chartImg.scaleToFit(450, 300);
        document.add(chartImg);
        imgFile.delete();
    }

    private static void addBPChart(Document document, List<VitalSign> vitals) throws Exception {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (VitalSign v : vitals) {
            String date = v.getDateTime().format(formatter);
            dataset.addValue(v.getSystolicPressure(), "Systolic", date);
            dataset.addValue(v.getDiastolicPressure(), "Diastolic", date);
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Blood Pressure Over Time", "Date", "mmHg", dataset,
                PlotOrientation.VERTICAL, true, true, false
        );

        BufferedImage image = chart.createBufferedImage(450, 300);
        File imgFile = File.createTempFile("bp_chart", ".png");
        ImageIO.write(image, "png", imgFile);
        Image chartImg = Image.getInstance(imgFile.getAbsolutePath());
        chartImg.scaleToFit(450, 300);
        document.add(chartImg);
        imgFile.delete();
    }
}
