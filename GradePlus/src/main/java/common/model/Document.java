package common.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

public class Document {
    File file;

    PDDocument document;


    public void printCertificate (String pInputFile, String pOutputFile) throws IOException {
        String inputTestPath = "Volumes/Macintosh HD/Users/andreasestenfelder/Projects/GradePlus/GradePlus/src/main/webapp/resources/templates.certificate.pdf";


        //Loading an existing document
        File file = new File(inputTestPath);
System.out.println("Methode printCertificate wird angesprochen");
        String outputTestPath = "Volumes/Macintosh\\ HD//Users/andreasestenfelder/Desktop/newFile.pdf";
        try {
            PDDocument document = PDDocument.load(file);
            PDPage page = document.getPage(1);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // NAME (Vor und Nach)
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            // MATRIKELNUMMER
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            // LEHRVERANSTALTUNG NAME
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            // ECTS
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            // VAK
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            // ZEIT
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            // WOCHENSTUNDEN
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            // STUDIENGANG
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            // DETAILS
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            // EINZELLEISTUNG
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            // GRUPPENLEISTUNG
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            // INHALT UND FORM DER LEISTUNG
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            // PRUEFUNGSGEBIET
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            // NOTE
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            // DATUM
            contentStream.beginText();
            contentStream.setFont(PDType1Font.COURIER, 11);
            contentStream.newLineAtOffset(25, 500);
            contentStream.showText("Sollte vielleicht doch Bean werden");
            contentStream.endText();

            contentStream.close();
            document.save(new File(outputTestPath));

            document.close();

        } catch (IOException e) {
            throw new IOException("Da ist wohl was schiefgegangen");
        }

    }
}
