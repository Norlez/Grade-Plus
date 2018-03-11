package controller;

import common.model.*;
import common.util.Assertion;

import java.io.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.opencsv.*;
import persistence.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;

import static common.util.Assertion.assertNotNull;

/**
 * Mittels BackupBean werden sämtliche Backup aktionen bewerkstelligt Dafür kann der
 * Administrator Backups mittels import Einspielen, mittels Export abspeichern, mittels
 * delete entfernen und mittels create ein neues Backup des derzeitigen Systems erstellen.
 *
 * @author Andreas Estenfelder, Torben Groß, Arbnor Miftari, Anil Olgun
 * @version 2018-03-11
 */
@Named
@RequestScoped
public class BackupBean extends AbstractBean implements Serializable {

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Benutzer-Objekte
     * übernimmt.
     */
    private final UserDAO userDAO;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Lecture-Objekte
     * übernimmt.
     */
    private final LectureDAO lectureDAO;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für ILV-Objekte
     * übernimmt.
     */
    private final InstanceLectureDAO instanceLectureDAO;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Exams-Objekte
     * übernimmt.
     */
    private final ExamDAO examDAO;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Grade-Objekte
     * übernimmt.
     */
    private final GradeDAO gradeDAO;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für JoinExam-Objekte
     * übernimmt.
     */
    private final JoinExamDAO joinExamDAO;

    /**
     * Enthält alle Nutzer des Systems.
     */
    private List<User> allUsers;

    /**
     * Enthält alle Lectures des Systems
     */
    private List<Lecture> allLectures;

    /**
     * Enthält alle ILVs des Systems
     */
    private List<InstanceLecture> allInstanceLectures;

    /**
     * Enthält alle Exmas des Systems
     */
    private List<Exam> allExams;

    /**
     * Enthält alle ILVs des Systems
     */
    private List<JoinExam> allJoinExams;

    /**
     * Enthält alle Grades des Systems
     */
    private List<Grade> allGrades;

    /**
     * Der  Schreiber, der für das Schreiben von Dateien genutzt wird.
     */
    private Writer out;

    /**
     * Ist für das Schreiben der CSV Dateien zuständig.
     */
    private CSVWriter csvWriter;

    /**
     * Erzeugt eine neue BackupBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden BackupBean.
     *            @param pUserDao
     *            Die UserDao der zu erzeugenden BackupBean.
     *            @param pExamDao
     *            Die ExamDao der zu erzeugenden BackupBean.
     *            @param pGradeDao
     *            Die GradeDao der zu erzeugenden BackupBean.
     *            @param pInstanceLectureDao
     *            Die InstanceLectureDao der zu erzeugenden BackupBean.
     *            @param pJoinExamDao
     *            Die JoinExamDao der zu erzeugenden BackupBean.
     *            @param pLectureDao
     *            Die LectureDao der zu erzeugenden BackupBean.
     *
     * @throws IllegalArgumentException
     *             Falls {@code pSession} oder {@code pBackupDAO} {@code null} ist.
     *             Gilt auch für die anderen Parameter
     */
    @Inject
    public BackupBean(Session pSession, UserDAO pUserDao, LectureDAO pLectureDao,
            InstanceLectureDAO pInstanceLectureDao, ExamDAO pExamDao,
            JoinExamDAO pJoinExamDao, GradeDAO pGradeDao) {
        super(pSession);
        userDAO = pUserDao;
        lectureDAO = pLectureDao;
        instanceLectureDAO = pInstanceLectureDao;
        examDAO = pExamDao;
        joinExamDAO = pJoinExamDao;
        gradeDAO = pGradeDao;
    }

    /**
     * Initialisiert die Attribute {@link #} und {@link #allUsers}, sodass {@link #} einen
     * neu anzulegenden {@link User} repräsentiert und {@link #allUsers} alle bekannten
     * Benutzer der Applikation enthält.
     */
    @PostConstruct
    public void init() {
        allUsers = userDAO.getAllUsers();
        allLectures = lectureDAO.getAllLectures();
        allInstanceLectures = instanceLectureDAO.getAllInstanceLectures();
        allExams = examDAO.getAllExams();
        allJoinExams = joinExamDAO.getAllJoinExams();
        allGrades = gradeDAO.getAllGrades();
    }

    /**
     * Stellt eine Verbindung zu der Datenbank her
     * @return Connection, die Verbindung zur Datenbank
     * @throws SQLException, falls etwas fehlschlägt.
     */
    public Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        //Speziell für die Glassfish Derby Datenbank
        connectionProps.put("user", "APP");
        connectionProps.put("password", "APP");
        conn = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/sun-appserv-samples", connectionProps);
        return conn;
    }

    /**
     * Erstellt für den aktuellen Stand der Datenbank ein Backup im Downloadverzeichnis.
     *
     * @throws SQLException, falls etwas fehlschlägt mit der Verbindung
     * @throws IOException, falls beim Schreiben der Datei ein Fehler auftritt
     */
    public void createBackup() throws SQLException, IOException {
        File file = new File(System.getProperty("user.home") + "/Downloads/");
        file.createNewFile();
        Connection conn = getConnection();
        //Der Name der Backup-Datei
        java.text.SimpleDateFormat todaysDate = new java.text.SimpleDateFormat(
                "yyyy-MM-dd");
        String backupdirectory = file.getPath() + '\\' + "DatabaseBackup-"
                + todaysDate.format((java.util.Calendar.getInstance()).getTime());
        //Aufbau der Verbindung
        CallableStatement cs = conn
                .prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)");
        cs.setString(1, backupdirectory);
        cs.execute();
        cs.close();
        System.out.println("backed up database to " + backupdirectory);
    }

    /**
     * Sollte eine CSV schreiben.
     * @deprecated Die Methode ist veraltet nicht nutzen!
     *
     */
    public void createUserCSV() {
        try {
            out = new BufferedWriter(new FileWriter("CSV/User.csv"));
            csvWriter = new CSVWriter(out, ';', CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END); // LIne-End-Problem
            List<User> u = allUsers;
            String[] s = new String[1];
            for (int i = 0; i < u.size(); i++) {
                User tmp = u.get(i);
                s[0] = tmp.toCSV();
                csvWriter.writeNext(s);
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (java.io.IOException e) {
            System.out.print("Ein Fehler beim Schreiben der Datei.");
        }
    }

    /**
     * Sollte eine CsvDatei für die Lectures erstellen
     * @deprecated Die Methode ist veraltet nicht nutzen!
     */
    public void createLectureCSV() {
        try {
            out = new BufferedWriter(new FileWriter("CSV/Lecture.csv"));
            csvWriter = new CSVWriter(out, ';', CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END); // LIne-End-Problem
            List<Lecture> l = allLectures;
            String[] s = new String[1];
            for (int i = 0; i < l.size(); i++) {
                Lecture tmp = l.get(i);
                s[0] = tmp.toCSV();
                csvWriter.writeNext(s);
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (java.io.IOException e) {
            System.out.print("Ein Fehler beim Schreiben der Datei.");
        }
    }

    /**
     * Sollte eine CsvDatei für die ILVs erstellen
     * @deprecated Die Methode ist veraltet nicht nutzen!
     */
    public void createInstanceLectureCSV() {
        try {
            out = new BufferedWriter(new FileWriter("CSV/InstanceLecture.csv"));
            csvWriter = new CSVWriter(out, ';', CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END); // LIne-End-Problem
            List<InstanceLecture> ilv = allInstanceLectures;
            String[] s = new String[1];
            for (int i = 0; i < ilv.size(); i++) {
                InstanceLecture tmp = ilv.get(i);
                s[0] = tmp.toCSV();
                csvWriter.writeNext(s);
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (java.io.IOException e) {
            System.out.print("Ein Fehler beim Schreiben der Datei.");
        }
    }

    /**
     * Sollte eine CsvDatei für die Grades erstellen
     * @deprecated Die Methode ist veraltet nicht nutzen!
     */
    public void createGradeCSV() {
        try {
            out = new BufferedWriter(new FileWriter("CSV/Grade.csv"));
            csvWriter = new CSVWriter(out, ';', CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END); // LIne-End-Problem
            List<Grade> g = allGrades;
            String[] s = new String[1];
            for (int i = 0; i < g.size(); i++) {
                Grade tmp = g.get(i);
                s[0] = tmp.toCSV();
                csvWriter.writeNext(s);
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (java.io.IOException e) {
            System.out.print("Ein Fehler beim Schreiben der Datei.");
        }
    }

    /**
     * Sollte eine CsvDatei für die JoinExams erstellen
     * @deprecated Die Methode ist veraltet nicht nutzen!
     */
    public void createJoinExamCSV() {
        try {
            out = new BufferedWriter(new FileWriter("CSV/JoinExam.csv"));
            csvWriter = new CSVWriter(out, ';', CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END); // LIne-End-Problem
            List<JoinExam> j = allJoinExams;
            String[] s = new String[1];
            for (int i = 0; i < j.size(); i++) {
                JoinExam tmp = j.get(i);
                s[0] = tmp.toCSV();
                csvWriter.writeNext(s);
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (java.io.IOException e) {
            System.out.print("Ein Fehler beim Schreiben der Datei.");
        }
    }

    /**
     * Sollte eine CsvDatei für die Exams erstellen
     * @deprecated Die Methode ist veraltet nicht nutzen!
     */
    public void createExamCSV() {
        try {
            out = new BufferedWriter(new FileWriter("CSV/Exam.csv"));
            csvWriter = new CSVWriter(out, ';', CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END); // LIne-End-Problem
            List<Exam> e = allExams;
            String[] s = new String[1];
            for (int i = 0; i < e.size(); i++) {
                Exam tmp = e.get(i);
                s[0] = tmp.toCSV();
                csvWriter.writeNext(s);
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (java.io.IOException e) {
            System.out.print("Ein Fehler beim Schreiben der Datei.");
        }
    }

    /**
     * Wurde nicht implementiert.
     *
     * Stellt ein aktuell ausgewähltes Backup wiederher.
     *
     *
     * @return {@code true}, falls das Backup wiederhergestellt wurde, sonst {@code false}
     *         .
     */
    public boolean restore() {
        throw new UnsupportedOperationException();
    }
}
