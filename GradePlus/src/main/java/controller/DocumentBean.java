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

    private int pruefungsordnung;
    private boolean wiederholungspruefung;
    private String vak;
    private String lecture;
    private String name;
    private String givenname;
    private String matrnr;
    private String ort;
    private String date;
    private String start;
    private String examiner;
    private String coexaminer;
    private boolean beisitzer;
    private String content;
    private String end;
    private String grade;

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
            if(wiederholungspruefung == true) {
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
            if(beisitzer == true) {
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
}
