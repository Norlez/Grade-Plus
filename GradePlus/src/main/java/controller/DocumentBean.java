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
import persistence.InstanceLectureDAO;
import persistence.JoinExamDAO;
import persistence.UserDAO;

import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static common.util.Assertion.*;

/**
 * Diese Bean ist für das Befüllen der Dokumente zuständig.
 * Es werden darin die Dokumente Protokoll, Quittung und Leistungsnachweis erstellt und befüllt.
 * Wird auf Grades.xhtml genutzt.
 *
 * @author Marvin Kampen
 * @version 2018-03-11
 */
@Named
@SessionScoped
public class DocumentBean extends AbstractBean implements Serializable {

    /**
     * Ist für die Persistierung und das Erhalten von User-DAO-Objekte zuständig.
     */
    private final UserDAO userDAO;

    /**
     * Ist für die Persistierung und das Erhalten von Exam-DAO-Objekte zuständig.
     */
    private final ExamDAO examDAO;

    /**
     * Ist für die Persistierung und das Erhalten von JoinExam-DAO-Objekte zuständig.
     */
    private final JoinExamDAO joinExamDAO;

    /**
     * Ist für die Persistierung und das Erhalten von InstanceLecture-DAO-Objekte zuständig.
     */
    private final InstanceLectureDAO instanceLectureDAO;

    // Gibt an, welche Pruefungsordnung benutzt wird
    private int pruefungsordnung = 3;

    // Ist Pruefung Wiederholungspruefung
    private boolean wiederholungspruefung = false;

    /**
     * Diese Map wird benötigt, um festzustellen, welche Dokumente gedruckt werden sollen.
     */
    private Map<String, Boolean> checked = new HashMap<>();

    private Date startOfTimeFrame;
    private Date endOfTimeFrame;

    // Beinhaltet VAK
    private String vak = "343636343";

    // Gibt Name der Lehrveranstaltung an
    private String lecture = "343636343";

    // Name des Studenten
    private String name = "343636343";

    // Vorname des Studenten
    private String givenname = "343636343";

    // Matrikelnummer des Studenten
    private String matrnr = "343636343";

    // Pruefungsort
    private String ort = "Bremen";

    // Datum der Pruefung
    private String date = "343636343";

    // Begin der Pruefung
    private String start = "343636343";

    // Name des Pruefers
    private String examiner = "343636343";

    // Name des Beisitzers
    private String coexaminer = "343636343";

    // Wurde auf Beisitzer verzichtet?
    private boolean beisitzer;

    // Inhalt der Veranstaltung
    private String content = "343636343";

    // Ende der Pruefung
    private String end = "343636343";

    // Ausgestellte Note der Pruefung
    private String grade = "1,7";

    // Angabe der sonstigen Pruefungsart
    private String sonstigePruefungsart = "343636343";

    // Welche Bemerkung ist anzugeben
    private int bemerkungen;

    // Angabe der sonstigen Bemerkung
    private String sonstigeBemerkung = "";

    // Ausstellungsdatum des Zertifikats
    private String datetoday = "343636343";

    // Gibt Anzahl der ECTS an
    private String ects = "3";

    // Anzahl der Wochenstunden der Lehrveranstaltung
    private String hours = "343636343";

    // Winter oder Sommersemester
    private String semester = "343636343";

    // Jahr der Lehrveranstaltung
    private String year = "2018";

    // Name des Studiengangs
    private String course = "343636343";

    // Ist Pruefung eine Gruppenpruefung
    private boolean groupe;

    // Angabe des Pruefers zum Inhalt
    private String contentExaminer = "343636343";

    // Name des Pruefungsgebiets
    private String pruefungsgebiet = "343636343";

    java.text.SimpleDateFormat todaysDate = new java.text.SimpleDateFormat("dd-MM-yyyy");

    /**
     * Erzeugt eine neue AbstractBean.
     *
     * @param theSession
     *            Die Session der zu erzeugenden AbstractBean.
     * @throws IllegalArgumentException
     *             Falls {@code theSession} {@code null} ist.
     */
    @Inject
    public DocumentBean(Session theSession, UserDAO pUserDao, ExamDAO pExamDao,
            JoinExamDAO pJoinExamDAo, InstanceLectureDAO pInstanceLectureDao) {
        super(theSession);
        userDAO = assertNotNull(pUserDao);
        examDAO = pExamDao;
        joinExamDAO = pJoinExamDAo;
        instanceLectureDAO = pInstanceLectureDao;
    }

    public Map<String, Boolean> getChecked() {
        return checked;
    }

    public void setChecked(final Map<String, Boolean> pChecked) {
        checked = assertNotNull(pChecked);
    }

    public Date getStartOfTimeFrame() {
        return startOfTimeFrame;
    }

    public void setStartOfTimeFrame(final Date startOfTimeFrame) {
        this.startOfTimeFrame = startOfTimeFrame;
    }

    public Date getEndOfTimeFrame() {
        return endOfTimeFrame;
    }

    public void setEndOfTimeFrame(final Date endOfTimeFrame) {
        this.endOfTimeFrame = endOfTimeFrame;
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
     * @param pUser
     *              Der Benutzer für den das Protokoll gedruckt werden soll.
     * @param pExam
     *              Die Prüfung für die das Protokoll gedruckt werden soll.
     *
     * @return Der StreamedContent, welche die erzeugte PDF enthält.
     * @throws IOException, falls ein Fehler beim Speichern der Datei auftritt.
     *
     */
    public StreamedContent getProtocol(final User pUser, final Exam pExam)
            throws IOException {

        final User user = pUser;
        final Exam exam = pExam;

        List<User> u = pExam.getExaminers();
        User examiner = u.get(0);
        String s = new String();
        for (int i = 1; i < u.size(); i++) {
            s += u.get(i).getSurname() + ", " + u.get(i).getGivenName() + "; ";
        }

        grade = "";
        for (JoinExam j : joinExamDAO.getJoinExamsForUser(user)) {
            if (j.getPruefling().getId() == user.getId()
                    && j.getExam().getId() == exam.getId()) {
                if (j.getGrade() != null) {
                    grade = j.getGrade().getMark() + "";
                }
            }
        }

        String relativeWebPath = ((ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext()).getRealPath("/resources/")
                + "/documents/Protocol.pdf";
        File file = new File(relativeWebPath);

        try {

            PDDocument document = PDDocument.load(file);
            PDPage page = document.getPage(0);

            PDFont fontTimes = PDType1Font.TIMES_ROMAN;

            PDPageContentStream contentStream = new PDPageContentStream(document, page,
                    true, true, true);

            // Pruefungsordnung
            if (pruefungsordnung == 1) {
                contentStream.beginText();
                contentStream.setFont(fontTimes, 12);
                contentStream.newLineAtOffset(244, 684);
                contentStream.showText(" ");
                contentStream.endText();
            } else if (pruefungsordnung == 2) {
                contentStream.beginText();
                contentStream.setFont(fontTimes, 12);
                contentStream.newLineAtOffset(244, 670);
                contentStream.showText(" ");
                contentStream.endText();
            } else if (pruefungsordnung == 3) {
                contentStream.beginText();
                contentStream.setFont(fontTimes, 12);
                contentStream.newLineAtOffset(244, 657);
                contentStream.showText(" ");
                contentStream.endText();
            }

            // Wiederholungspruefung
            if (wiederholungspruefung) {
                contentStream.beginText();
                contentStream.setFont(fontTimes, 12);
                contentStream.newLineAtOffset(73, 630);
                contentStream.showText("X");
                contentStream.endText();
            }

            // VAK
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(160, 605);
            contentStream.showText(exam.getInstanceLecture().getLecture().getVak());
            contentStream.endText();

            // Lecture
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(300, 605);
            contentStream.showText(exam.getInstanceLecture().getLecture().getName());
            contentStream.endText();

            // Name, Vorname
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(160, 578);
            contentStream.showText(user.getSurname() + ", " + user.getGivenName());
            contentStream.endText();

            // matrnr
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(400, 578);
            contentStream.showText(user.getMatrNr());
            contentStream.endText();

            // Ort
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(160, 549);
            contentStream.showText(ort);
            contentStream.endText();

            // Date
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(340, 549);
            contentStream.showText(exam.dateToString());
            contentStream.endText();

            // Start
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(520, 549);
            contentStream.showText(exam.timeToString());
            contentStream.endText();

            // Examiner
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(160, 522);
            contentStream
                    .showText(examiner.getGivenName() + ", " + examiner.getSurname());
            contentStream.endText();

            // CoExaminer
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(340, 522);
            contentStream.showText(s);
            contentStream.endText();

            // Beisitzer
            if (beisitzer) {
                contentStream.beginText();
                contentStream.setFont(fontTimes, 12);
                contentStream.newLineAtOffset(274, 508);
                contentStream.showText("X");
                contentStream.endText();
            }

            // content
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(72, 450);
            contentStream.showText(" ");
            contentStream.endText();

            // End
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(180, 87);
            contentStream.showText(exam.endOfExam());
            contentStream.endText();

            // Grade
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(300, 87);
            contentStream.showText(grade);
            contentStream.endText();

            contentStream.close();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            document.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(
                    outputStream.toByteArray());

            return new DefaultStreamedContent(inputStream, "application/pdf",
                    "Protocol.pdf");

        } catch (IOException e) {
            throw new IOException("File not saved");
        }
    }

    /**
     * Methode zum Drucken der Quittung
     *
     */
    public StreamedContent getReceipe(final User pUser, final Exam pExam)
            throws IOException {

        final User user = pUser;
        final Exam exam = pExam;
        grade = "";
        for (JoinExam j : joinExamDAO.getJoinExamsForUser(user)) {
            if (j.getPruefling().getId() == user.getId()
                    && j.getExam().getId() == exam.getId()) {
                if (j.getGrade() != null) {
                    grade = j.getGrade().getMark() + "";
                }
            }
        }

        if (exam.getType().equals("Mündliche Prüfung")) {
            pruefungsordnung = 2;
        } else {
            pruefungsordnung = 1;
        }

        String relativeWebPath = ((ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext()).getRealPath("/resources/")
                + "/documents/Receipe.pdf";
        File file = new File(relativeWebPath);

        try {

            PDDocument document = PDDocument.load(file);
            PDPage page = document.getPage(0);

            PDFont fontTimes = PDType1Font.TIMES_ROMAN;

            PDPageContentStream contentStream = new PDPageContentStream(document, page,
                    true, true, true);

            // Name, Vorname
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(160, 715);
            contentStream.showText(user.getSurname() + ", " + user.getGivenName());
            contentStream.endText();

            // Matrikelnummer
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(160, 682);
            contentStream.showText(user.getMatrNr());
            contentStream.endText();

            // Datum
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(160, 648);
            contentStream.showText(exam.dateToString()); // Vielleicht fehlt die Zeit?
            contentStream.endText();

            // Pruefungsart
            if (pruefungsordnung == 1) {
                contentStream.beginText();
                contentStream.setFont(fontTimes, 12);
                contentStream.newLineAtOffset(73, 612);
                contentStream.showText("X");
                contentStream.endText();
            } else if (pruefungsordnung == 2) {
                contentStream.beginText();
                contentStream.setFont(fontTimes, 12);
                contentStream.newLineAtOffset(73, 589);
                contentStream.showText("X");
                contentStream.endText();
            } else if (pruefungsordnung == 3) {
                contentStream.beginText();
                contentStream.setFont(fontTimes, 12);
                contentStream.newLineAtOffset(73, 566);
                contentStream.showText("X");
                contentStream.newLineAtOffset(120, 566);
                contentStream.showText(sonstigePruefungsart);
                contentStream.endText();
            }

            // Lecture
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(160, 495);
            contentStream.showText(exam.getInstanceLecture().getLecture().getName());
            contentStream.endText();

            // VAK
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(160, 473);
            contentStream.showText(exam.getInstanceLecture().getLecture().getVak());
            contentStream.endText();

            // Grade
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(130, 441);
            contentStream.showText(grade);
            contentStream.endText();

            // Bemerkungen
            if (bemerkungen == 1) {
                contentStream.beginText();
                contentStream.setFont(fontTimes, 12);
                contentStream.newLineAtOffset(73, 382);
                contentStream.showText("X");
                contentStream.endText();
            } else if (bemerkungen == 2) {
                contentStream.beginText();
                contentStream.setFont(fontTimes, 12);
                contentStream.newLineAtOffset(73, 359);
                contentStream.showText("X");
                contentStream.endText();
            } else if (bemerkungen == 3) {
                contentStream.beginText();
                contentStream.setFont(fontTimes, 12);
                contentStream.newLineAtOffset(73, 336);
                contentStream.showText("X");
                contentStream.endText();
            } else if (bemerkungen == 4) {
                contentStream.beginText();
                contentStream.setFont(fontTimes, 12);
                contentStream.newLineAtOffset(73, 313);
                contentStream.showText("X");
                contentStream.newLineAtOffset(120, 313);
                contentStream.showText(sonstigeBemerkung);
                contentStream.endText();
            }

            // Ort, Datum
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(73, 235);
            contentStream.showText(ort + ", "
                    + todaysDate.format((java.util.Calendar.getInstance()).getTime()));
            contentStream.endText();

            contentStream.close();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            document.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(
                    outputStream.toByteArray());

            return new DefaultStreamedContent(inputStream, "application/pdf",
                    "Receipe.pdf");

        } catch (IOException e) {
            throw new IOException("File not saved");
        }
    }

    /**
     * Methode zum Drucken des Leistungsnachweise
     *
     */
    public StreamedContent getCertificates(final User pUser, final Exam pExams)
            throws IOException {

        User user = pUser;
        Exam exam = pExams;

        String relativeWebPath = ((ServletContext) FacesContext.getCurrentInstance()
                .getExternalContext().getContext()).getRealPath("/resources/")
                + "/documents/Certificate.pdf";
        File file = new File(relativeWebPath);

        grade = "";
        for (JoinExam j : joinExamDAO.getJoinExamsForUser(user)) {
            if (j.getPruefling().getId() == user.getId()
                    && j.getExam().getId() == exam.getId()) {
                if (j.getGrade() != null) {
                    grade = j.getGrade().getMark() + "";
                }
            }
        }

        content = "";
        if (exam.getInstanceLecture().getLecture().getDescription() != null) {
            content = exam.getInstanceLecture().getLecture().getDescription();
        }

        if (exam.getParticipants().size() < 1) {
            groupe = true;
        }

        try {

            PDDocument document = PDDocument.load(file);
            PDPage page = document.getPage(0);

            PDFont fontTimes = PDType1Font.TIMES_ROMAN;

            PDPageContentStream contentStream = new PDPageContentStream(document, page,
                    true, true, true);

            // Name, Vorname
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(90, 623);
            contentStream.showText(user.getSurname() + " " + user.getGivenName());
            contentStream.endText();

            // Matrikelnummer
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(475, 623);
            contentStream.showText(user.getMatrNr());
            contentStream.endText();

            // Lehrveranstaltung
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(160, 603);
            contentStream.showText(exam.getInstanceLecture().getLecture().getName());
            contentStream.endText();

            // ECTS
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(475, 563);
            contentStream.showText(exam.getInstanceLecture().getLecture().getEcts() + "");
            contentStream.endText();

            // VAK
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(160, 543);
            contentStream.showText(exam.getInstanceLecture().getLecture().getVak());
            contentStream.endText();

            // Semester
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(290, 543);
            contentStream.showText(exam.getInstanceLecture().getSemester() + " "
                    + exam.getInstanceLecture().getYear());
            contentStream.endText();

            // Wochenstunden
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(475, 543);
            contentStream.showText("");
            contentStream.endText();

            // Studiengang
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(290, 523);
            contentStream.showText(" ");
            contentStream.endText();

            // Inhalt
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(80, 483);
            contentStream.showText(content);
            contentStream.endText();

            // Einzel, Gruppenleistung
            if (groupe == false) {
                contentStream.beginText();
                contentStream.setFont(fontTimes, 12);
                contentStream.newLineAtOffset(122, 338);
                contentStream.showText("X");
                contentStream.endText();
            } else if (groupe == true) {
                contentStream.beginText();
                contentStream.setFont(fontTimes, 12);
                contentStream.newLineAtOffset(282, 338);
                contentStream.showText("X");
                contentStream.endText();
            }

            // Inhalt Pruefer
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(180, 313);
            contentStream.showText(exam.getType());
            contentStream.endText();

            // Pruefungsgebiet
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(180, 193);
            contentStream.showText("");
            contentStream.endText();

            // Note
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(290, 167);
            contentStream.showText(grade);
            contentStream.endText();

            // Aktuelles Datum
            contentStream.beginText();
            contentStream.setFont(fontTimes, 12);
            contentStream.newLineAtOffset(100, 87);
            contentStream.showText(todaysDate.format((java.util.Calendar.getInstance())
                    .getTime()));
            contentStream.endText();

            contentStream.close();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            document.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(
                    outputStream.toByteArray());

            return new DefaultStreamedContent(inputStream, "application/pdf",
                    "Certificate.pdf");

        } catch (IOException e) {
            throw new IOException("File not saved");
        }
    }

    public List<String> getAvailableDocuments() {
        List<String> availableDocuments = new ArrayList<>();
        availableDocuments.add("Protokoll");
        availableDocuments.add("Quittung");
        availableDocuments.add("Zertifikat");
        return availableDocuments;
    }
    

}
