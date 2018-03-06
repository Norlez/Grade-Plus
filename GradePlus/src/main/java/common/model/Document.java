package common.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

public class Document {

    private String name;

    private int matrnr;

    private String lecture;

    private int ects;

    private String vak;

    private String time;

    private String hours;

    private String course;

    private String details;

    private boolean singeGroup;

    private String content;

    private String area;

    private String grade;

    private String date;

    File file;

    PDDocument document;

    public static void printBlank () throws IOException {

        //Creating PDF document object
        PDDocument document = new PDDocument();

        //Saving the document
        document.save("C:/PdfBox_Examples/my_doc.pdf");

        System.out.println("PDF created");

        //Closing the document
        document.close();

    }


    public void printCertificate (String pInputFile, String pOutputFile) throws IOException {
        String inputTestPath = "Macintosh HD/Users/andreasestenfelder/Projects/GradePlus/GradePlus/src/main/webapp/resources/templates.certificate.pdf";


        //Loading an existing document
        File file = new File(inputTestPath);
System.out.println("Methode printCertificate wird angesprochen");
        String outputTestPath = "Macintosh\\ HD//Users/andreasestenfelder/Desktop/newFile.pdf";
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
