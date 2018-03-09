package controller;

import common.model.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import persistence.ExamDAO;
import persistence.UserDAO;

import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.io.*;
import java.util.List;

import static common.util.Assertion.*;

/**
 * Diese Bean ist für das Befüllen der Dokumente zuständig.
 *
 * @author Marvin Kampen
 * @version 2018-03-01
 */
@Named
@RequestScoped
public class DocumentBean extends AbstractBean implements Serializable {

    private final UserDAO userDAO;

    private final ExamDAO examDAO;

    // Gibt an, welche Pruefungsordnung benutzt wird
    private int pruefungsordnung;

    // Ist Pruefung Wiederholungspruefung
    private boolean wiederholungspruefung;

    // Beinhaltet VAK
    private String vak;

    // Gibt Name der Lehrveranstaltung an
    private String lecture;

    // Name des Studenten
    private String name;

    // Vorname des Studenten
    private String givenname;

    // Matrikelnummer des Studenten
    private String matrnr;

    // Pruefungsort
    private String ort;

    // Datum der Pruefung
    private String date;

    // Begin der Pruefung
    private String start;

    // Name des Pruefers
    private String examiner;

    // Name des Beisitzers
    private String coexaminer;

    // Wurde auf Beisitzer verzichtet?
    private boolean beisitzer;

    // Inhalt der Veranstaltung
    private String content;

    // Ende der Pruefung
    private String end;

    // Ausgestellte Note der Pruefung
    private String grade;

    // Angabe der sonstigen Pruefungsart
    private String sonstigePruefungsart;

    // Welche Bemerkung ist anzugeben
    private int bemerkungen;

    // Angabe der sonstigen Bemerkung
    private String sonstigeBemerkung;

    // Ausstellungsdatum des Zertifikats
    private String datetoday;

    // Gibt Anzahl der ECTS an
    private String ects;

    // Anzahl der Wochenstunden der Lehrveranstaltung
    private String hours;

    // Winter oder Sommersemester
    private String semester;

    // Jahr der Lehrveranstaltung
    private String year;

    // Name des Studiengangs
    private String course;

    // Ist Pruefung eine Gruppenpruefung
    private boolean groupe;

    // Angabe des Pruefers zum Inhalt
    private String contentExaminer;

    // Name des Pruefungsgebiets
    private String pruefungsgebiet;

    /**
     * Erzeugt eine neue AbstractBean.
     *
     * @param theSession
     *            Die Session der zu erzeugenden AbstractBean.
     * @throws IllegalArgumentException
     *             Falls {@code theSession} {@code null} ist.
     */
    @Inject
    public DocumentBean(Session theSession, UserDAO pUserDao, ExamDAO pExamDao) {
        super(theSession);
        userDAO = assertNotNull(pUserDao);
        examDAO = pExamDao;
    }

    public String getNameOfStudent(User pStudent) {
        assertNotNull(pStudent);
        return userDAO.getUserForUsername(pStudent.getUsername()).getGivenName();
    }

    public String getSurnameOfStudent(User pStudent) {
        assertNotNull(pStudent);
        return userDAO.getUserForUsername(pStudent.getUsername()).getSurname();
    }

    public String getMatrNrOfStudent(User pStudent) {
        assertNotNull(pStudent);
        return userDAO.getUserForUsername(pStudent.getUsername()).getMatrNr();
    }


    /**
     * Methode zum Drucken des Protokolls
     *
     */
    public StreamedContent getProtocol () throws IOException {

        String relativeWebPath = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/resources/") + "/documents/Protocol.pdf";
        File file = new File(relativeWebPath);

        try {

            PDDocument document = PDDocument.load(file);
            PDPage page = document.getPage(0);

            PDFont fontTimes = PDType1Font.TIMES_ROMAN;

            PDPageContentStream contentStream = new PDPageContentStream(document, page, true, true, true);

            // Pruefungsordnung
            if (pruefungsordnung == 1) {
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText("X");
                contentStream.endText();
            } else if(pruefungsordnung == 2) {
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText("X");
                contentStream.endText();
            } else if(pruefungsordnung == 3) {
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText("X");
                contentStream.endText();
            }

            // Wiederholungspruefung
            if(wiederholungspruefung) {
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText("X");
                contentStream.endText();
            }

            // VAK
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(vak);
            contentStream.endText();

            // Lecture
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(lecture);
            contentStream.endText();

            // Name, Vorname
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(name + ", " + givenname);
            contentStream.endText();

            // matrnr
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(matrnr);
            contentStream.endText();

            // Ort
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(ort);
            contentStream.endText();

            // Date
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(lecture);
            contentStream.endText();

            // Start
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(start);
            contentStream.endText();

            // Examiner
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(lecture);
            contentStream.endText();

            // CoExaminer
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(coexaminer);
            contentStream.endText();

            // Beisitzer
            if(beisitzer) {
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText("X");
                contentStream.endText();
            }

            // content
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(content);
            contentStream.endText();

            // End
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(end);
            contentStream.endText();

            // Grade
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(grade);
            contentStream.endText();

            contentStream.close();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            document.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            return new DefaultStreamedContent(inputStream, "application/pdf", "Protocol.pdf");

        } catch (IOException e) {
            throw new IOException("File not saved");
        }
    }

    /**
     * Methode zum Drucken der Quittung
     *
     */
    public StreamedContent getReceipe () throws IOException {

        String relativeWebPath = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/resources/") + "/documents/Receipe.pdf";
        File file = new File(relativeWebPath);

        try {

            PDDocument document = PDDocument.load(file);
            PDPage page = document.getPage(0);

            PDFont fontTimes = PDType1Font.TIMES_ROMAN;

            PDPageContentStream contentStream = new PDPageContentStream(document, page, true, true, true);

            // Name, Vorname
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(name + ", " + givenname);
            contentStream.endText();

            // Matrikelnummer
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(matrnr);
            contentStream.endText();

            // Datum
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(date);
            contentStream.endText();

            // Pruefungsart
            if (pruefungsordnung == 1) {
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText("X");
                contentStream.endText();
            } else if(pruefungsordnung == 2) {
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText("X");
                contentStream.endText();
            } else if(pruefungsordnung == 3) {
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText("X");
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(sonstigePruefungsart);
                contentStream.endText();
            }

            // Lecture
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(lecture);
            contentStream.endText();

            // VAK
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(vak);
            contentStream.endText();

            // Grade
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(grade);
            contentStream.endText();

            // Bemerkungen
            if (bemerkungen == 1) {
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText("X");
                contentStream.endText();
            } else if(bemerkungen == 2) {
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText("X");
                contentStream.endText();
            } else if(bemerkungen == 3) {
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText("X");
                contentStream.endText();
            } else if(bemerkungen == 4) {
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText("X");
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(sonstigeBemerkung);
                contentStream.endText();
            }

            // Ort, Datum
            contentStream.beginText();
            contentStream.setFont(fontTimes,12);
            contentStream.newLineAtOffset(80,500);
            contentStream.showText(ort + ", " + datetoday);
            contentStream.endText();

            contentStream.close();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            document.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            return new DefaultStreamedContent(inputStream, "application/pdf", "Protocol.pdf");

        } catch (IOException e) {
            throw new IOException("File not saved");
        }
    }

    /**
     * Methode zum Drucken des Leistungsnachweise
     *
     */
    public StreamedContent getCertificates (List<Exam> exams) throws IOException {

        String relativeWebPath = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/resources/") + "/documents/Receipe.pdf";
        File file = new File(relativeWebPath);

        try {

            PDDocument document = PDDocument.load(file);
            PDPage page = document.getPage(0);

            PDFont fontTimes = PDType1Font.TIMES_ROMAN;

            PDPageContentStream contentStream = new PDPageContentStream(document, page, true, true, true);

            for(Exam e: exams) {

                document.addPage(page);

                // Name, Vorname
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(givenname + " " + name);
                contentStream.endText();

                // Matrikelnummer
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(matrnr);
                contentStream.endText();

                // Lehrveranstaltung
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(lecture);
                contentStream.endText();

                // ECTS
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(ects);
                contentStream.endText();

                // VAK
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(vak);
                contentStream.endText();

                // Semester
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(semester + " " + year);
                contentStream.endText();

                // Wochenstunden
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(hours);
                contentStream.endText();

                // Studiengang
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(course);
                contentStream.endText();

                // Inhalt
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(content);
                contentStream.endText();

                // Einzel, Gruppenleistung
                if(groupe == false) {
                    contentStream.beginText();
                    contentStream.setFont(fontTimes,12);
                    contentStream.newLineAtOffset(80,500);
                    contentStream.showText("X");
                    contentStream.endText();
                } else if(groupe == true) {
                    contentStream.beginText();
                    contentStream.setFont(fontTimes,12);
                    contentStream.newLineAtOffset(80,500);
                    contentStream.showText("X");
                    contentStream.endText();
                }

                // Inhalt Pruefer
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(contentExaminer);
                contentStream.endText();

                // Pruefungsgebiet
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(pruefungsgebiet);
                contentStream.endText();

                // Note
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(grade);
                contentStream.endText();

                // Aktuelles Datum
                contentStream.beginText();
                contentStream.setFont(fontTimes,12);
                contentStream.newLineAtOffset(80,500);
                contentStream.showText(datetoday);
                contentStream.endText();
            }

            contentStream.close();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            document.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            return new DefaultStreamedContent(inputStream, "application/pdf", "Protocol.pdf");

        } catch (IOException e) {
            throw new IOException("File not saved");
        }
    }
}
