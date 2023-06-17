package at.technikum;

import at.technikum.service.PDFGeneratorApp;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main_pdf {
    public static void main(String[] args) throws IOException, TimeoutException {
        PDFGeneratorApp app = new PDFGeneratorApp();
        System.out.println("Starting DataCollectionDispatcherApp...");
        app.run();
    }
}