package controller;

import com.google.common.base.Charsets;
import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
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

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static common.util.Assertion.assertNotNull;

/**
 * Diese Bean ist für das Bearbeiten bestimmter {@link InstanceLecture}-Objekte
 * verantwortlich.
 *
 * @author Torben Groß, Marvin Kampen, Tugce Karakus, Anil Olgun,
 * @version 2018-02-27
 */
@Named
@SessionScoped
public class InstanceLectureEditBean extends AbstractBean implements Serializable {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(UsersBean.class);

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für ILV-Objekte
     * übernimmt.
     */
    private final InstanceLectureDAO instanceLectureDao;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Benutzer-Objekte
     * übernimmt.
     */
    private final UserDAO userDao;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Exam-Objekte
     * übernimmt.
     */
    private final ExamDAO examDAO;

    // Gibt an, welche Pruefungsordnung benutzt wird
    private int pruefungsordnung = 3;

    // Ist Pruefung Wiederholungspruefung
    private boolean wiederholungspruefung = false;

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

    //Setzt das Format für Datum
    java.text.SimpleDateFormat todaysDate = new java.text.SimpleDateFormat("dd-MM-yyyy");

    /**
     * Die aktuell zu bearbeitende ILV.
     */
    private InstanceLecture instanceLecture;

    /**
     * Prüft, ob die Attribute in dieser Klasse verändert werden dürfen.
     */
    private boolean editChecker = false;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Join-Exams-Objekte
     * übernimmt.
     */
    private JoinExamDAO joinExamDAO;

    /**
     * Stellt ein StreamedContent dar, der durch Primefaces per download-tag runtergeladen
     * werden kann.
     */
    private DefaultStreamedContent severalFiles;

    /**
     * Diese Map wird benötigt, um festzustellen, welche Dokumente gedruckt werden sollen.
     */
    private Map<String, Boolean> checked = new HashMap<>();

    /**
     * Liste mit den ILVs des Users
     */
    private List<InstanceLecture> instancesForUser;
    /**
     * Der Beginn der Prüfung
     */
    private Date startOfTimeFrame;
    /**
     * Das Ende der Prüfung
     */
    private Date endOfTimeFrame;

    /**
     * getter vom checked-Attribut
     * @return Die checked Map
     */
    public Map<String, Boolean> getChecked() {
        return checked;
    }

    /**
     * Setzt die Map auf die gegebenen Parameter.
     * @param pChecked
     */
    public void setChecked(final Map<String, Boolean> pChecked) {
        checked = assertNotNull(pChecked);
    }

    /**
     * Gibt die Startzeit der Prüfung zurück.
     * @return Startzeit als Date
     */
    public Date getStartOfTimeFrame() {
        return startOfTimeFrame;
    }

    /**
     * Setzt die Startzeit der Prüfung
     * @param startOfTimeFrame
     */
    public void setStartOfTimeFrame(final Date startOfTimeFrame) {
        this.startOfTimeFrame = startOfTimeFrame;
    }

    /**
     * Gibt den Endzeitpunkt der Prüfung zurück.
     * @return endOfTimeFrame als Date
     */
    public Date getEndOfTimeFrame() {
        return endOfTimeFrame;
    }

    /**
     * Setzt den Endzeitpunkt der Prüfung.
     * @param endOfTimeFrame
     */
    public void setEndOfTimeFrame(final Date endOfTimeFrame) {
        this.endOfTimeFrame = endOfTimeFrame;
    }

    /**
     * Erzeugt eine neue InstanceLectureEditBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden InstanceLectureEditBean.
     *            @param pExamDao
     *            Die ExamDAo der zu erzeugenden InstanceLectureEditBean
     *            @param pJoinExamDAO
     *            Die JoinExamDAo der zu erzeugenden InstanceLectureEditBean
     *            @param pUserDao
     *            Die UserDAo der zu erzeugenden InstanceLectureEditBean
     * @param pInstanceLectureDao
     *            Die InstanceLectureDAO der zu erzeugenden InstanceLectureEditBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} oder {@code pInstanceLectureDao} {@code null}
     *             sind.
     */
    @Inject
    public InstanceLectureEditBean(final Session pSession, final ExamDAO pExamDao,
            final InstanceLectureDAO pInstanceLectureDao, final UserDAO pUserDao,
            final JoinExamDAO pJoinExamDAO) {
        super(pSession);
        instanceLectureDao = assertNotNull(pInstanceLectureDao);
        userDao = assertNotNull(pUserDao);
        joinExamDAO = assertNotNull(pJoinExamDAO);
        examDAO = assertNotNull(pExamDao);
    }

    @PostConstruct
    public void init() {
        instancesForUser = getUniuqueIlvs();
    }

    /**
     * Gibt die aktuell zu bearbeitende ILV zurück.
     *
     * @return Die aktuell zu bearbeitende ILV.
     */
    public InstanceLecture getInstanceLecture() {
        return instanceLecture;
    }

    /**
     * Setzt die aktuell zu bearbeitende ILV auf den gegebenen Wert.
     *
     * @param pInstanceLecture
     *            Die aktuell zu bearbeitende ILV.
     * @return "semestercreate.xhtml", um auf das Facelet der Bearbeitung einer ILV
     *         umzuleiten.
     */
    public String setInstanceLecture(final InstanceLecture pInstanceLecture) {
        instanceLecture = assertNotNull(pInstanceLecture);
        return "exams.xhtml";
    }

    /**
     * Setzt die aktuell zu bearbeitende ILV auf den gegebenen Wert des Studenten.
     *
     * @param pInstanceLecture
     *            Die aktuell zu bearbeitende ILV.
     * @return "semestercreate.xhtml", um auf das Facelet der Bearbeitung einer ILV
     *         umzuleiten.
     */
    public String setInstanceLectureForStudent(final InstanceLecture pInstanceLecture) {
        instanceLecture = assertNotNull(pInstanceLecture);
        return "examregister.xhtml";
    }

    /**
     * Aktualisiert die aktuell ausgewählte ILV in der Liste aller bekannten ILVs unter
     * Verwendung des entsprechenden Data-Access-Objekts.
     *
     * @return "semestercreate.xhtml", um auf das Facelet der Bearbeitung einer ILV
     *         umzuleiten.
     */
    public String update() {
        try {
            instanceLectureDao.update(instanceLecture);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorInputdataIncomplete"));
        } catch (UnexpectedUniqueViolationException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("someError"));
        }
        return "semester.xhtml";
    }

    /**
     * Ist für das Updaten der Studenten und Profs erforderlich.
     * @throws  IllegalArgumentException, falsche Argumente
     * @throws  UnexpectedUniqueViolationException, Wert ist nicht einzigartig
     * @throws  DuplicateEmailException, Emailadresse bereits vorhanden
     * @throws DuplicateUsernameException, Username bereits vorhanden
     * @param pUser, der geupdatet werden soll
     */
    public void update(User pUser) {
        try {
            userDao.update(pUser);
            instanceLectureDao.update(instanceLecture);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorInputdataIncomplete"));
        } catch (UnexpectedUniqueViolationException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("someError"));
        } catch (DuplicateUsernameException e) {
            e.printStackTrace();
        } catch (DuplicateEmailException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Fügt einen Prüfer zur ILV hinzu..
     * @param pExaminer, der hinzugefügt werden soll
     * @return exams.xhtml
     */
    public String addExaminer(final User pExaminer) {
        assertNotNull(pExaminer, "InstanceLectureEditBean: addExaminer(User)");
        if (!instanceLecture.getExaminees().contains(pExaminer)) {
            instanceLecture.addExaminer(pExaminer);
            pExaminer.addAsProfToIlv(instanceLecture);
            update(pExaminer);
        } else {
            addErrorMessage("errorUserIsStudent");
        }
        return "exams.xhtml";
    }

    /**
     * Entfernt einen Prüfer aus der ILV.
     * @param pExaminer, der Prüfer der entfernt werden soll
     * @return exams.xhtml
     */
    public String removeExaminer(final User pExaminer) {
        assertNotNull(pExaminer, "InstanceLectureEditBean: removeExaminer(User)");
        for (Exam exam : examDAO.getAllExams()) {
            if (exam.getInstanceLecture().getId().equals(instanceLecture.getId())) {
                if (exam.getExaminers().contains(pExaminer)) {
                    addErrorMessage("errorUserIsExaminerOfExam");
                    return "exams.xhtml";
                }
            }
        }
        if (instanceLecture.getExaminers().stream()
                .filter(e -> e.getRole().equals(Role.EXAMINER))
                .collect(Collectors.toList()).size() <= 1
                && pExaminer.getRole().equals(Role.EXAMINER)) {
            addErrorMessage("errorLastExaminer");
            return "exams.xhtml";
        }
        instanceLecture.removeExaminer(pExaminer);
        pExaminer.removeProfFromIlv(instanceLecture);
        update(pExaminer);
        if (pExaminer.getId().equals(getSession().getUser().getId())) {
            return "semester.xhtml";
        }
        return "exams.xhtml";
    }

    /**
     * Fügt einen Student zu ILV hinzu.
     * @param pStudent, der hinzugefügt werden soll.
     * @return exams.xhtml
     */
    public String addStudent(final User pStudent) {
        assertNotNull(pStudent, "InstanceLectureEditBean: addStudent(User)");
        if (!instanceLecture.getExaminers().contains(pStudent)) {
            instanceLecture.addExaminee(pStudent);
            pStudent.addAsStudentToIlv(instanceLecture);
            JoinExam joinExam = new JoinExam();
            joinExam.setPruefling(pStudent);
            joinExam.setKind(Anmeldeart.BYPROF);
            joinExam.setInstanceLecture(instanceLecture);
            joinExamDAO.save(joinExam);
            instanceLecture.addJoinExam(joinExam);
            instanceLectureDao.update(instanceLecture);
            update(pStudent);
        } else {
            addErrorMessage("errorUserIsExaminer");
        }
        return "exams.xhtml";
    }

    /**
     * Entfernt einen Studenten aus der ILV.
     * @param pStudent, der entfernt werden soll
     * @return exams.xhtml
     */
    public String removeStudent(final User pStudent) {
        assertNotNull(pStudent, "InstanceLectureEditBean: removeStudent(User)");
        for (Exam exam : examDAO.getAllExams()) {
            if (exam.getInstanceLecture().getId().equals(instanceLecture.getId())) {
                if (exam.getStudents().contains(pStudent)) {
                    addErrorMessage("errorUserIsExamineeOfExam");
                    return "exams.xhtml";
                }
            }
        }
        if (instanceLecture.getExaminees().contains(pStudent)) {
            instanceLecture.removeExaminee(pStudent);
            pStudent.removeStudentFromIlv(instanceLecture);
            JoinExam joinExam = joinExamDAO
                    .getAllJoinExams()
                    .stream()
                    .filter(j -> j.getInstanceLecture().getId()
                            .equals(instanceLecture.getId()))
                    .collect(Collectors.toList()).get(0);
            instanceLecture.removeJoinExam(joinExam);
            try {
                userDao.update(pStudent);
            } catch (Exception e) {
            }
            joinExamDAO.remove(joinExam);
            instanceLectureDao.update(instanceLecture);
            instanceLecture.removeExaminee(pStudent);
            pStudent.removeStudentFromIlv(instanceLecture);
            update(pStudent);
        }
        return "exams.xhtml";
    }

    /**
     * Gibt eine Liste zurück mit allen unregistrierten Studenten im System
     * @return Liste mit unregistrierten Prüflingen
     */
    public List<User> getUnregisteredStudents() {
        return userDao.getAllStudents().stream()
                .filter(s -> !instanceLecture.getExaminees().contains(s))
                .collect(Collectors.toList());
    }

    /**
     * Gibt für die Lehrveranstaltung alle ILVs zurück.
     * @param pLecture
     * @return Liste von ILVs
     */
    public List<InstanceLecture> getAllInstances(Lecture pLecture) {
        return instanceLectureDao.getInstanceLecturesForLecture(pLecture);
    }

    /**
     * Gibt die Prüfungen eines User zurück
     * @param pUser, für den gesucht wird
     * @return Liste mit Prüfungen des Benutzers
     */
    public List<Exam> getExams(User pUser) {
        List<Exam> exam = new ArrayList<Exam>();
        List<JoinExam> tmp = joinExamDAO.getNonExmptyJoinExamsForUser(pUser);
        if (tmp != null) {
            for (JoinExam l : tmp) {
                if (l.getExam() != null) {
                    exam.add(l.getExam());
                }
            }
        }
        return exam;
    }

    /**
     * Gibt eine Liste zurück, welche alle freigegebenen Prüfungen enthält für eine ILV.
     * @return Liste mit Prüfungen
     */
    public List<Exam> getReleasedExams() {
        List<Exam> tmp = examDAO.getExamsForInstanceLecture(instanceLecture);
        List<Exam> examList = new ArrayList<Exam>();
        if (!tmp.isEmpty())
            for (Exam e : tmp) {
                if (e.getReleased() == true) {
                    examList.add(e);
                }
            }
        return examList;
    }

    /**
     * Prüft, ob der gegegebene Student für die Prüfung registriert ist.
     * @return true, falls er registriert ist, sonst false.
     */
    public boolean isStudentRegisteredForExam() {
        List<User> allRegisteredUsers = new ArrayList<>();
        instanceLecture.getExams().forEach(
                e -> allRegisteredUsers.addAll(e.getStudents()));
        return allRegisteredUsers.contains(getSession().getUser());
    }

    /**
     * Gibt alle freigegebenen Prüfungen für eine ILv zurück
     * @return Liste von Prüfungen
     */
    public List<Exam> getReleasedExamsForInstanceLecture() {
        return examDAO
                .getAllExams()
                .stream()
                .filter(e -> e.getReleased()
                        && e.getInstanceLecture().getId().equals(instanceLecture.getId()))
                .collect(Collectors.toList());
    }

    /*
     * Duplicated Code aus der Document Bean, da es zu unvorhergesehenen Problemen kam.
     */

    /**
     * Gibt den Namen des Studenten zurück.
     * @param pStudent
     * @return Gibt den Namen des Studenten als String zurück
     */
    public String getNameOfStudent(User pStudent) {
        assertNotNull(pStudent);
        return userDao.getUserForUsername(pStudent.getUsername()).getGivenName();
    }

    /**
     * Gibt den Nachnamen des Studenten zurück.
     * @param pStudent
     * @return Nachname als String.
     */
    public String getSurnameOfStudent(User pStudent) {
        assertNotNull(pStudent);
        return userDao.getUserForUsername(pStudent.getUsername()).getSurname();
    }

    /**
     * Gib die MatrNr des Studenten zurück.
     * @param pStudent
     * @return MatrNr des Studenten als String
     */
    public String getMatrNrOfStudent(User pStudent) {
        assertNotNull(pStudent);
        return userDao.getUserForUsername(pStudent.getUsername()).getMatrNr();
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
     * @param pUser
     *              Der Benutzer für den das Quittung gedruckt werden soll.
     * @param pExam
     *              Die Prüfung für die das Quittung gedruckt werden soll.
     *
     * @return Der StreamedContent, welche die erzeugte PDF enthält.
     * @throws IOException, falls ein Fehler beim Speichern der Datei auftritt.
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
     * @param pUser
     *              Der Benutzer für den das Leistungsnachweis gedruckt werden soll.
     * @param pExams
     *              Die Prüfung für die das Leistungsnachweis gedruckt werden soll.
     *
     * @return Der StreamedContent, welche die erzeugte PDF enthält.
     * @throws IOException, falls ein Fehler beim Speichern der Datei auftritt.
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

    /**
     * Gibt alle verfügbaren Dokumente zurück.
     * @return Liste mit den verfügbaren Dokumenten
     */
    public List<String> getAvailableDocuments() {
        List<String> availableDocuments = new ArrayList<>();
        availableDocuments.add("Protokoll");
        availableDocuments.add("Quittung");
        availableDocuments.add("Zertifikat");
        return availableDocuments;
    }

    /**
     * Erzeugt für einen Zeitraum alle ausgewählten Dokumente
     * @param pInstanceLecture, die gesuchte ILV.
     * @param pStart, der Startzeitpunkt
     * @param pEnd, der Endzeitpunkt
     * @param pChecked, ob der Dokumententyp ausgewählt wurde.
     * @return StreamedContent mit den erzeugten Dokumenten
     * @throws IOException
     */
    public StreamedContent getDocumentsForTimeFrame(
            final InstanceLecture pInstanceLecture, Date pStart, Date pEnd,
            Map<String, Boolean> pChecked) throws IOException {
        Map<String, Boolean> checked = pChecked;
        LocalDateTime start = LocalDateTime.ofInstant(pStart.toInstant(),
                ZoneId.systemDefault());
        LocalDateTime end = LocalDateTime.ofInstant(pEnd.toInstant(),
                ZoneId.systemDefault());

        assertNotNull(pInstanceLecture);
        List<String> selectedDocuments = checked.entrySet().stream()
                .filter(Map.Entry::getValue).map(Map.Entry::getKey)
                .collect(Collectors.toList());
        ArrayList<Exam> exams = new ArrayList<Exam>();
        ArrayList<JoinExam> joinExams = new ArrayList<JoinExam>();
        List<Exam> doof = examDAO.getExamsForInstanceLecture(pInstanceLecture);

        for (Exam e : doof) {
            LocalDateTime ldtExam = e.getLocalDateTime();

            if (ldtExam.isAfter(start) && ldtExam.isBefore(end)) {
                exams.add(e); // Nur zum Debuggen
                List<JoinExam> tmp = joinExamDAO.getUsersForExam(e);
                if (tmp != null) {
                    joinExams.addAll(tmp);
                }
            }
        }
        List<StreamedContent> streamedContents = new ArrayList<StreamedContent>();
        for (JoinExam gtfo : joinExams) {
            if (gtfo.getExam() != null && gtfo.getPruefling() != null) {
                if (selectedDocuments.contains("Protokoll")) {
                    streamedContents
                            .add(getProtocol(gtfo.getPruefling(), gtfo.getExam()));
                }
                if (selectedDocuments.contains("Quittung")) {
                    streamedContents
                            .add((getReceipe(gtfo.getPruefling(), gtfo.getExam())));
                }
                if (selectedDocuments.contains("Zertifikat")) {
                    streamedContents.add(getCertificates(gtfo.getPruefling(),
                            gtfo.getExam()));
                }
            }
        }
        List<DefaultStreamedContent> resultStreamedContents = new ArrayList<DefaultStreamedContent>();
        List<InputStream> resultInputContents = new ArrayList<InputStream>();
        List<File> resultFiles = new ArrayList<File>();

        for (StreamedContent s : streamedContents) {
            resultFiles.add(streamToPDFTempFile(s.getStream()));
        }

        saveDocumentsToZip(resultFiles);
        return severalFiles;
    }

    /**
     * Erstellt aus einer Liste von Dateien einen StreamedContent-Zip-File für Primefaces.
     * 
     * @param pFile
     *            die ZipFile
     */
    public void saveDocumentsToZip(List<File> pFile) {
        ByteArrayInputStream bis = new ByteArrayInputStream(zipBytes(pFile));
        InputStream stream = bis;
        severalFiles = new DefaultStreamedContent(stream, "application/zip",
                "documents.zip", Charsets.UTF_8.name());
    }

    /**
     * Erstellt aus einer Liste aus Dateien einen byteArray.
     * 
     * @param pFile
     *            Die Liste der Files
     * @return array aus Bytes.
     */
    private byte[] zipBytes(List<File> pFile) {
        ByteArrayOutputStream tmpByteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(tmpByteArrayOutputStream);
        FileInputStream fis = null;
        try {
            for (File f : pFile) {
                fis = new FileInputStream(f);
                ZipEntry entry = new ZipEntry(f.getName());
                zos.putNextEntry(entry);

                byte[] data = new byte[4 * 1024];
                int size = 0;
                while ((size = fis.read(data)) != -1) {
                    zos.write(data, 0, size);
                }
                zos.flush();
                fis.close();
            }
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tmpByteArrayOutputStream.toByteArray();
    }

    /**
     * Erstellt aus einem InputStream eine temporäre Datei.
     * 
     * @param pInputStream
     * @return die erstellte Files
     * @throws IOException
     */
    public File streamToPDFTempFile(InputStream pInputStream) throws IOException {
        File tempFile = File.createTempFile("tmpFilePDF", ".pdf");
        OutputStream outputStream = new FileOutputStream(tempFile);
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = pInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } finally {
            pInputStream.close();
            outputStream.close();
            return tempFile;
        }
    }

    /**
     *   Gibt die Endnote für einen User für eine ILV zurück.
    *
     * @param pUser, der gesuchte User
     * @param pInstanceLecture, die gesuchte ILV
     * @return Endnote des Studenten in der ILV
     */
    public Double getSubRating(final User pUser, final InstanceLecture pInstanceLecture) {
        List<JoinExam> joinExams = assertNotNull(
                instanceLectureDao.getById(pInstanceLecture.getId())).getJoinExam()
                .stream().filter(j -> j.getPruefling().getId().equals(pUser.getId()))
                .collect(Collectors.toList());
        return joinExams.get(0).getGrade() == null ? null : joinExams.get(0).getGrade()
                .getMark();
    }

    /**
     *  Gibt die Endnote für einen User für eine ILV zurück.
     * @param pUser, der gesuchte User
     * @param pInstanceLecture, die gesuchte ILV.
     * @return Endnote des Studenten in der ILV
     */
    public Double getFinalScore(final User pUser, final InstanceLecture pInstanceLecture) {
        List<JoinExam> joinExams = assertNotNull(
                instanceLectureDao.getById(pInstanceLecture.getId())).getJoinExam()
                .stream().filter(j -> j.getPruefling().getId().equals(pUser.getId()))
                .collect(Collectors.toList());
        return joinExams.get(0).getGrade() == null
                || joinExams.get(0).getGrade().getEndMark() == null ? null : joinExams
                .get(0).getGrade().getMark();
    }

    /**
     * Gibt die Prüfungen eines Prüfers für eine ILV zurück.
     * @return Liste von Prüfungen.
     */
    public List<Exam> getExamsOfInstanceLectureForExaminer() {
        return instanceLecture.getExams().stream()
                .filter(e -> e.getExaminers().contains(getSession().getUser()))
                .collect(Collectors.toList());
    }

    /**
     * Getter für InstancesForUser-Attribut
     * @return Liste von ILVs
     */
    public List<InstanceLecture> getInstancesForUser() {
        return instancesForUser;
    }

    /**
     * Setter für InstancesForUser-Attribut
     * @param instancesForUser
     */
    public void setInstancesForUser(List<InstanceLecture> instancesForUser) {
        this.instancesForUser = instancesForUser;
    }

    /**
     * Gibt alle Lectures im System zurück.
     * @return Liste mit allen Lectures.
     */
    public List<InstanceLecture> getUniuqueIlvs() {
        List<InstanceLecture> tmp = new ArrayList<InstanceLecture>();
        List<InstanceLecture> j = getSession().getUser().getAsStudent();
        for (InstanceLecture je : j) {
            if (!tmp.contains(je.getLecture().getId())) {
                tmp.add(je);
            }
        }
        return tmp;
    }

}
