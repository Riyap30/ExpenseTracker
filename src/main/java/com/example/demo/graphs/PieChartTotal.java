package com.example.demo.graphs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;

import com.example.demo.DTOs.CategoryExpenseDTO;

public class PieChartTotal {
    public String saveChartAsImage(List<CategoryExpenseDTO> data) {
        int width = 800;
        int height = 600;
        int padding = 50;

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        double totalValue = data.stream().mapToDouble(CategoryExpenseDTO::total).sum();

        int startAngle = 0;

        Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.MAGENTA,
            Color.CYAN, Color.PINK, Color.YELLOW, Color.GRAY, Color.LIGHT_GRAY};

        int pieX = padding;
        int pieY = padding;
        int pieWidth = width - 2 * padding - 200; 
        int pieHeight = height - 2 * padding;

        for (int i = 0; i < data.size(); i++) {
            CategoryExpenseDTO dto = data.get(i);

            int angle = (int) Math.round((dto.total() / totalValue) * 360);

            g2d.setColor(colors[i % colors.length]);

            g2d.fillArc(pieX, pieY, pieWidth, pieHeight, startAngle, angle);

            g2d.setColor(Color.BLACK);
            g2d.drawArc(pieX, pieY, pieWidth, pieHeight, startAngle, angle);

            startAngle += angle;
        }

        int legendX = pieX + pieWidth + 20; 
        int legendY = padding;

        
        for (int i = 0; i < data.size(); i++) {
            CategoryExpenseDTO dto = data.get(i);

            g2d.setColor(colors[i % colors.length]);

            g2d.fillRect(legendX, legendY, 20, 20);

            g2d.setColor(Color.BLACK);
            g2d.drawRect(legendX, legendY, 20, 20);

            g2d.setColor(Color.BLACK);
            g2d.drawString(dto.name() + " --> $" + String.format("%.1f", dto.total()), legendX + 30, legendY + 15);

            legendY += 30;
        }

        g2d.dispose();

        // Save the image to a file
        try {
            ImageIO.write(image, "png", outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }    
}
