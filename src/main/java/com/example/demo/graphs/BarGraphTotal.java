package com.example.demo.graphs;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import com.example.demo.DTOs.CategoryExpenseDTO;

import java.util.Base64;
import java.util.List;

public class BarGraphTotal extends JPanel {
    public String saveChartAsImage(List<CategoryExpenseDTO> data) {
        int width = 800;
        int height = 600;
        int padding = 50;
        int labelPadding = 50;
        int barWidth = 30;
        int barSpacing = 30; 
        int numberOfBars = data.size();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.BLACK);
        g2d.drawLine(padding, height - padding, width - padding, height - padding); 
        g2d.drawLine(padding, padding, padding, height - padding); 

        double maxValue = data.stream().mapToDouble(CategoryExpenseDTO::total).max().orElse(1);
        double scale = (height - 2 * padding) / maxValue;

        Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.MAGENTA,
                          Color.CYAN, Color.PINK, Color.YELLOW, Color.GRAY, Color.LIGHT_GRAY};

        for (int i = 0; i < numberOfBars; i++) {
            CategoryExpenseDTO dto = data.get(i);
            int x = padding + labelPadding + i * (barWidth + barSpacing);
            int y = height - padding - (int) (dto.total() * scale);
            int barHeight = (int) (dto.total() * scale);

            g2d.setColor(colors[i % colors.length]);
            g2d.fillRect(x, y, barWidth, barHeight);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, barWidth, barHeight);

            Font originalFont = g2d.getFont();
            if (dto.name().length() > 6) {
                g2d.setFont(originalFont.deriveFont(originalFont.getSize() - 2.0f));
            }

            FontMetrics fontMetrics = g2d.getFontMetrics();
            int labelWidth = fontMetrics.stringWidth(dto.name());

            AffineTransform originalTransform = g2d.getTransform();

            g2d.rotate(Math.toRadians(-45), x + barWidth / 2, height - padding + 20);

            g2d.drawString(dto.name(), x + (barWidth - labelWidth) / 2, height - padding + fontMetrics.getHeight() + 5);

            g2d.setTransform(originalTransform);
            g2d.setFont(originalFont);
        }

        // Y-axis
        for (int i = 0; i <= 10; i++) {
            int y = height - padding - i * (height - 2 * padding) / 10;
            String yLabel = String.format("%.1f", maxValue * i / 10);
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int labelWidth = fontMetrics.stringWidth(yLabel);
            g2d.drawString("$" + yLabel, padding - labelWidth - 10, y + fontMetrics.getHeight() / 2);
            g2d.drawLine(padding + 1, y, padding + 5, y); 
        }
        g2d.dispose();

        // Send image to front end client for display in base64 string
        try {
            ImageIO.write(image, "png", outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
