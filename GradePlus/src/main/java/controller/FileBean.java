package controller;

import com.opencsv.CSVWriter;
import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.model.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import persistence.GradeDAO;
import persistence.InstanceLectureDAO;
import persistence.JoinExamDAO;
import persistence.UserDAO;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.Join;
import javax.servlet.http.Part;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static common.util.Assertion.assertNotNull;

/**
 * Diese Bean ist für das Importieren und Exportieren verschiedener Dateien wie
 * Pabo-Listen zuständig.
 *
 * @author Torben Groß, Marvin Kampen, Tugce Karakus
 * @version 2018-02-27
 */
@Named
@RequestScoped
public class FileBean extends AbstractBean implements Serializable {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(UsersBean.class);

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Benutzer-Objekte
     * übernimmt.
     */
    private UserDAO userDao;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Noten-Objekte
     * übernimmt.
     */
    private final GradeDAO gradeDAO;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für
     * InstanceLecture-Objekte übernimmt.
     */
    private InstanceLectureDAO instanceLectureDAO;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für JoinExam-Objekte
     * übernimmt.
     */
    private JoinExamDAO joinExamDAO;

    /**
     * Die zu verarbeitende Datei.
     */
    private Part file;

    /**
     * File, das für den jeweiligen Prüfer für eine Prüfung und dem dazugehörigen
     * Studenten im System hochgeladen wird.
     */
    private UploadedFile uploadFile;

    /**
     * Setzt den übergebenen String für das jeweilige Attribut in der JoinExam.
     * 
     * @param pByte
     *            zu setzende String.
     * @param pJoinExam
     *            Die JoinExam, dessen String attribut durch pString gesetzt werden soll.
     */
    public String setJoinExamUploadedFile(final byte[] pByte, JoinExam pJoinExam) {
        if (file == null) {
            addErrorMessage("errorFileEmpty");
            return null;
        }
        assertNotNull(pByte);
        pJoinExam.setSavedDocument(pByte);
        joinExamDAO.update(pJoinExam);
        return null;
    }

    /**
     * Wandelt den InputStream eines UploadedFile-Objektes in einen String um.
     * 
     * @param pFile
     *            die File die hochgeladen werden soll.
     * @return der aus dem InputStream erzeugte String
     * @throws IOException
     */
    public byte[] saveDocumentForUserInDatabaseGaaahdDayum(final UploadedFile pFile)
            throws IOException {
        InputStream inputStream = pFile.getInputstream();
        return IOUtils.toByteArray(inputStream);
    }

    /**
     * Wandelt einen gegebenen String in einen InputSteam um, der wiederrum in ein
     * StreamedContent objekt gewandelt wird, damit er vom User runtergeladen werden kann.
     * 
     * @param pJoinExam
     * @return Streamedcontent mit dem neuen Protokoll
     */
    public StreamedContent downloadUploadedFile(final JoinExam pJoinExam) {
        assertNotNull(pJoinExam.getSavedDocument());
        byte[] tmp = pJoinExam.getSavedDocument();
        java.io.InputStream inputStream = new ByteArrayInputStream(tmp);
        return new DefaultStreamedContent(inputStream, "application/pdf", "Protocol"
                + pJoinExam.getPruefling().getMatrNr() + ".pdf");
    }

    /**
     * Erzeugt eine neue FileBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden FileBean.
     * @param pUserDao
     *            Die UserDAO der zu erzeugenden FileBean.
     * @param pUserDao
     *            Die UserDao der zu erzeugenden FileBean
     * @param pGradeDao
     *            Die GradeDao der zu erzeugenden FileBean.
     * @param pInstanceLectureDAO
     *            Die InstanceLectureDao der zu erzeugenden FileBean.
     *
     */
    @Inject
    public FileBean(final Session pSession, final UserDAO pUserDao,
            final GradeDAO pGradeDao, final InstanceLectureDAO pInstanceLectureDAO,
            final JoinExamDAO pJoinExamDAO) {
        super(pSession);
        userDao = assertNotNull(pUserDao);
        instanceLectureDAO = assertNotNull(pInstanceLectureDAO);
        joinExamDAO = assertNotNull(pJoinExamDAO);
        gradeDAO = pGradeDao;
    }

    /**
     * Gibt den hochgeladenen UplaodedFile zurück.
     * 
     * @return
     */
    public UploadedFile getUploadFile() {
        return uploadFile;
    }

    /**
     * Setzt den hochgeladenen File durch den übergebenen Parameter
     * 
     * @Param die UploadedFile
     */
    public void setUploadFile(UploadedFile uploadFile) {
        this.uploadFile = assertNotNull(uploadFile);
    }

    /**
     * Gibt die aktuell ausgewählte Datei zurück.
     *
     * @return Die aktuell ausgewählte Datei.
     */
    public Part getFile() {
        return file;
    }

    /**
     * Setzt die aktuell ausgewählte Datei auf den gegebenen Wert.
     *
     * @param pFile
     *            Die neue aktuell ausgewählte Datei.
     */
    public void setFile(final Part pFile) {
        file = assertNotNull(pFile);
    }

    /**
     * Speichert eine Menge von Usern aus einer CSV -Datei in die Applikation
     *
     * @return users.xhtml als weiterführende Seite
     * @throws IOException
     */
    public String saveFromCSV() throws IOException {
        InputStream is = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        for (String theLine : lines) {
            String[] data = theLine.split(",");
            User theUser = new User();
            theUser.setEmail(data[0].trim());
            theUser.setUsernameForUserMail();
            theUser.setSurname(data[1].trim());
            theUser.setGivenName(data[2].trim());
            theUser.setMatrNr(data[3].trim());
            theUser.setPassword(data[4].trim());
            theUser.setRole(Role.fromString(data[5]));
            try {
                userDao.save(theUser);
                SystemMailBean.registerMail(theUser);
            } catch (final IllegalArgumentException e) {
                addErrorMessageWithLogging(e, logger, Level.DEBUG,
                        getTranslation("errorUserdataIncomplete"));
            } catch (final DuplicateUsernameException e) {
                addErrorMessageWithLogging("registerUserForm:username", e, logger,
                        Level.DEBUG, "errorUsernameAlreadyInUse", theUser.getUsername());
            } catch (final DuplicateEmailException e) {
                addErrorMessageWithLogging("registerUserForm:email", e, logger,
                        Level.DEBUG, "errorEmailAlreadyInUse", theUser.getEmail());
            }
        }
        return "users.xhtml";
    }

    /**
     * Trägt die Pabo-Liste in der ILV ein.
     *
     *
     * @return exams.xhtml als Weiterleitung
     * @throws IOException
     *             , falls die einzulesende Datei fehlerhaft ist.
     */
    public String saveFromCSVFromPabo(InstanceLecture pInstanceLecture)
            throws IOException, DuplicateEmailException, DuplicateUsernameException {
        assertNotNull(pInstanceLecture);
        InputStream is = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        for (String theLine : lines) {
            String[] data = theLine.split(";");
            if (userDao.getUserForMatrNr(data[0].trim()) != null) {
                User u = userDao.getUserForMatrNr(data[0].trim());

                if (!pInstanceLecture.getExaminers().contains(u)) {
                    JoinExam j = new JoinExam();
                    j.setKind(Anmeldeart.BYPABO);
                    j.setPruefling(u);
                    j.setInstanceLecture(pInstanceLecture);
                    joinExamDAO.save(j);
                    u.addAsStudentToIlv(pInstanceLecture);
                    pInstanceLecture.addExaminee(u);
                    pInstanceLecture.addJoinExam(j);
                    instanceLectureDAO.update(pInstanceLecture);
                    userDao.update(u);
                }
            }
        }
        return "importstudentlist.xhtml";
    }

    /**
     * Druck die Termine der übergebenen Liste von Exams im ICS Format.
     *
     * @param examsOfStudent
     *            Ist die Liste der Exams eines Studenten.
     */
    public void printDateAsICS(List<Exam> examsOfStudent) {
        StringBuilder date = new StringBuilder();
        date.append("BEGIN:VCALENDAR");
        date.append("\n");
        date.append("VERSION:2.0");
        date.append("\n");
        date.append("PRODID: EXAMDATEN DER STUDENTEN");
        date.append("\n");
        date.append("METHOD:PUBLISH");
        date.append("\n");
        date.append("CALSCALE:GREGORIAN");
        date.append("\n");
        date.append("BEGIN:VTIMEZONE");
        date.append("\n");
        date.append("TZID:Europe/Berlin");
        date.append("\n");
        date.append("BEGIN:DAYLIGHT");
        date.append("\n");
        date.append("TZOFFSETFROM:+0100");
        date.append("\n");
        date.append("TZOFFSETTO:+0200");
        date.append("\n");
        date.append("TZNAME:CEST");
        date.append("\n");
        date.append("DTSTART:19700329T020000");
        date.append("\n");
        date.append("RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=3");
        date.append("\n");
        date.append("END:DAYLIGHT");
        date.append("\n");
        date.append("BEGIN:STANDARD");
        date.append("\n");
        date.append("TZOFFSETFROM:+0200");
        date.append("\n");
        date.append("TZOFFSETTO:+0100");
        date.append("\n");
        date.append("TZNAME:CET");
        date.append("\n");
        date.append("DTSTART:19701025T030000");
        date.append("\n");
        date.append("RRULE:FREQ=YEARLY;BYDAY=-1SU;BYMONTH=10");
        date.append("\n");
        date.append("END:STANDARD");
        date.append("\n");
        date.append("END:VTIMEZONE");
        date.append("\n");

        for (Exam e : examsOfStudent) {
            date.append("BEGIN:VEVENT");
            date.append("\n");
            date.append("DTSTART;TZID=" + e.dateTimeToIcsFormat());
            date.append("\n");
            date.append("DTEND;TZID=" + e.dateTimeToIcsPlusExamLengthFormat());
            date.append("\n");
            date.append("SUMMARY: Exam-Date for "
                    + e.getInstanceLecture().getLecture().getName());
            date.append("\n");
            date.append("UID:" + "Exam" + e.getId());
            date.append("\n");
            date.append("CLASS:PRIVATE");
            date.append("\n");
            date.append("CATEGORIES:Exam");
            date.append("\n");
            date.append("PRIORITY:0");
            date.append("\n");
            date.append("LOCATION:" + e.getLocation());
            date.append("\n");
            date.append("RRULE:INTERVAL=1;BYDAY=MO;FREQ=WEEKLY");
            date.append("\n");
            date.append("END:VEVENT");
            date.append("\n");
        }
        date.append("END:VCALENDAR");
        String name = new String("Exam-Dates");
        BufferedWriter bw = null;
        try {

            File file = new File(System.getProperty("user.home")
                    + "/Downloads/Exam-Dates-" + generateRandomString() + ".ics");
            file.createNewFile();

            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            bw.write("" + date);
            System.out.println("New File added");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (Exception ex) {
                System.out.println("Exceptiooooon: ICS EXPORT");
            }
        }
        FacesMessage message = new FacesMessage(
                "Ihre ICS wurde auf folgendes Verzeichnis gespeichert:"
                        + System.getProperty("user.home") + "/Downloads");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * Generiert einen zufälligen String, damit eine neue ICS erstellt wird und nicht
     * immer die alte überschrieben wird.
     * 
     * @return zufällig generierter String
     */
    protected String generateRandomString() {
        String pool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!&§$#";
        StringBuilder tmp = new StringBuilder();
        Random rnd = new Random();
        while (tmp.length() < 10) {
            int index = (int) (rnd.nextFloat() * pool.length());
            tmp.append(pool.charAt(index));
        }
        return tmp.toString();
    }

    /**
     * Schreibt die Noten für die gegebene ILV in eine CSV.
     * 
     * @param pInstanceLecture
     *            für die die Noten exportiert werden sollen
     *
     */
    public void exportGradesFromInstanceLecture(final InstanceLecture pInstanceLecture) {
        assertNotNull(pInstanceLecture);
        try {
            File file = new File(System.getProperty("user.home") + "/Downloads/Noten.csv");
            file.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(file.getPath()));
            CSVWriter csvWriter = new CSVWriter(out, ';', CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END); // LIne-End-Problem
            List<JoinExam> j = assertNotNull(joinExamDAO.getAllJoinExams());
            List<JoinExam> joinExamList = new ArrayList<JoinExam>();
            for (JoinExam joinExam : j) {
                if (joinExam.getExam() != null
                        && joinExam.getExam().getInstanceLecture().getId() == pInstanceLecture
                                .getId()) {
                    joinExamList.add(joinExam);
                }
            }
            String[] s = new String[1];
            for (int i = 0; i < joinExamList.size(); i++) {
                JoinExam tmp = joinExamList.get(i);
                s[0] = tmp.getPruefling().getMatrNr() + ";" + tmp.getGrade().getMark()
                        + ";" + tmp.getGrade().getJoinExam().getExam().getType();
                csvWriter.writeNext(s);
            }
            csvWriter.flush();
            csvWriter.close();
            addErrorMessage("outputSaveLocation",
                    new String[] { System.getProperty("user.home") + "\\Downloads" });
        } catch (java.io.IOException e) {
            System.out.print("Ein Fehler beim Schreiben der Datei.");
        }
    }

    /**
     * Importiert eine Notenliste und vergibt die Endnoten an die Prüflinge für diese ILV.
     *
     * @param pInstanceLecture
     *            für die Noten vergeben werden sollen
     *
     * @return null, da keine neue Seite geladen wird
     */
    public String importGradesFromInstanceLecture(InstanceLecture pInstanceLecture)
            throws IOException {
        assertNotNull(pInstanceLecture);
        InputStream is = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> lines = new ArrayList<>();
        String line;
        JoinExam joinExam = null;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        for (String theLine : lines) {
            String[] data = theLine.split(";"); // Schaut nur nach der MatrNr
            if (userDao.getUserForMatrNr(data[0].trim()) != null) {
                User u = userDao.getUserForMatrNr(data[0].trim());
                for (JoinExam j : joinExamDAO.getJoinExamsForUser(u)) {
                    if (j.getExam() != null
                            && j.getExam().getInstanceLecture().getId() == pInstanceLecture
                                    .getId()) {
                        joinExam = j;
                        break;
                    }
                }
                Double grade = Double.parseDouble(data[1].trim());
                joinExam.getGrade().setEndMark(grade);
                gradeDAO.update(joinExam.getGrade());
            }

        }
        return null;
    }
}
