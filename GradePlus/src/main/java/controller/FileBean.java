package controller;

import com.opencsv.CSVWriter;
import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.model.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
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
 * @author Torben Groß
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
     * Erzeugt eine neue FileBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden FileBean.
     * @param pUserDao
     *            Die UserDAO der zu erzeugenden FileBean.
     */
    @Inject
    public FileBean(final Session pSession, final UserDAO pUserDao,
            final InstanceLectureDAO pInstanceLectureDAO, final JoinExamDAO pJoinExamDAO) {
        super(pSession);
        userDao = assertNotNull(pUserDao);
        instanceLectureDAO = assertNotNull(pInstanceLectureDAO);
        joinExamDAO = assertNotNull(pJoinExamDAO);
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
     * @return
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

            File file = new File(System.getProperty("user.home") + "/Exam-Dates-"
                    + generateRandomString() + ".ics");
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
                        + System.getProperty("user.home"));
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * Generiert einen zufälligen String, damit eine neue ICS erstellt wird und nicht
     * immer die alte überschrieben wird.
     * 
     * @return
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
     * Schreibt die Noten für die gegebene Liste von JoinExams in eine CSV.
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
        } catch (java.io.IOException e) {
            System.out.print("Ein Fehler beim Schreiben der Datei.");
        }
    }

    /**
     * Importiert eine Notenliste und vergibt die Endnoten an die Prüflinge für diese ILV.
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
            String[] data = theLine.split(";");
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
                joinExamDAO.update(joinExam);
            }

        }
        return null;
    }
}
