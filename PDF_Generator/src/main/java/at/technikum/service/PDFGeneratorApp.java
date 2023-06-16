package at.technikum.service;

import at.technikum.util.JsonHelper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class PDFGeneratorApp implements MessageHandler{
    private static final String INPUT_QUEUE_NAME = "pdf_generator_queue";
    private static final String OUTPUT_DIRECTORY = Paths.get(System.getProperty("user.home"), "..", "Public").toString();
    private final MessagingQueue messagingQueue;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    public PDFGeneratorApp() throws IOException, TimeoutException {
        this.messagingQueue = new RabbitMQService(this);
    }

    @Override
    public void handleMessage(String message) throws Exception {
        Map<String, Object> messageMap = JsonHelper.deserialize(message);

        int customerId = (int) messageMap.get("customerId");
        String firstName = (String) messageMap.get("first_name");
        String lastName = (String) messageMap.get("last_name");
        double totalKwh = (double) messageMap.get("total_kwh");
        List<Map<String, Object>> stationsList = (List<Map<String, Object>>) messageMap.get("stations");
        long currentTime = (long) messageMap.get("currentTime");


        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
                contentStream.newLineAtOffset(25, 700);
                contentStream.showText("Charging Station Invoice");
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(400, 0);
                contentStream.showText("Date: " + DATE_FORMAT.format(new Date(currentTime)));
                contentStream.newLineAtOffset(-400, -20);
                contentStream.showText("Customer: " + firstName + " " + lastName);
                contentStream.endText();
                drawLine(contentStream, 25, 660, 575, 660);
                contentStream.beginText();
                contentStream.newLineAtOffset(25, 630);
                contentStream.showText("Stations:");
                for (Map<String, Object> station : stationsList) {
                    int stationId = (int) station.get("stationId");
                    List<Double> kwhList = (List<Double>) station.get("kwhList");
                    double stationTotalKwh = (double) station.get("total_kwh");
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("- " + stationId + ": " + kwhList.toString());
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Subtotal: " + stationTotalKwh + " kWh");
                    contentStream.endText();
                    contentStream.beginText();
                    contentStream.newLineAtOffset(25, 570 - 60 * stationsList.indexOf(station));
                }
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(0, -10 * stationsList.size());
                contentStream.showText("Total kWh used: " + totalKwh);
                contentStream.endText();
                drawLine(contentStream, 25, 25, 575, 25);
                contentStream.beginText();
                contentStream.endText();
            }
            document.save(OUTPUT_DIRECTORY + "\\customer_" + customerId + ".pdf");
            System.out.println("PDF generated for customer " + customerId);
            System.out.println("PDF saved in " + OUTPUT_DIRECTORY);
            System.out.println("-----------------------------------");
            System.out.println("Waiting for messages. To exit press CTRL+C");
        }
    }
    private void drawLine(PDPageContentStream contentStream, float startX, float startY, float endX, float endY) throws IOException {
        contentStream.moveTo(startX, startY);
        contentStream.lineTo(endX, endY);
        contentStream.stroke();
    }

    public void run() {
        try {
            System.out.println("PDF Generator App started");
            System.out.println("Waiting for messages in: " + INPUT_QUEUE_NAME);
            messagingQueue.consume(INPUT_QUEUE_NAME);
        } catch (Exception e) {
            System.out.println("Error consuming in "+ INPUT_QUEUE_NAME + ": " + e.getMessage());
        }
    }
}
