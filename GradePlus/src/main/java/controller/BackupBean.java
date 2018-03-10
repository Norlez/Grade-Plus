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
 * @version 2017-12-21
 */
@Named
@RequestScoped
public class BackupBean extends AbstractBean implements Serializable {

    private final UserDAO userDAO;

    private final LectureDAO lectureDAO;

    private final InstanceLectureDAO instanceLectureDAO;

    private final ExamDAO examDAO;

    private final GradeDAO gradeDAO;

    private final JoinExamDAO joinExamDAO;

    private List<User> allUsers;

    private List<Lecture> allLectures;

    private List<InstanceLecture> allInstanceLectures;

    private List<Exam> allExams;

    private List<JoinExam> allJoinExams;

    private List<Grade> allGrades;

    private Writer out;

    private CSVWriter csvWriter;

    /**
     * Erzeugt eine neue BackupBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden BackupBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} oder {@code pBackupDAO} {@code null} ist.
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

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "APP");
        connectionProps.put("password", "APP");
        conn = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/sun-appserv-samples", connectionProps);
        return conn;
    }

    /**
     * Erstellt für den aktuellen Stand der Datenbank ein Backup.
     *
     */
    public void createBackup() throws SQLException, IOException {
        File file = new File(System.getProperty("user.home")+"/Downloads/");
        file.createNewFile();
        Connection conn = getConnection();
        java.text.SimpleDateFormat todaysDate = new java.text.SimpleDateFormat(
                "yyyy-MM-dd");
        String backupdirectory = file.getPath() + '\\'+ "DatabaseBackup-"
                + todaysDate.format((java.util.Calendar.getInstance()).getTime());
        CallableStatement cs = conn
                .prepareCall("CALL SYSCS_UTIL.SYSCS_BACKUP_DATABASE(?)");
        cs.setString(1, backupdirectory);
        cs.execute();
        cs.close();
        System.out.println("backed up database to " + backupdirectory);
    }

    /**
     * Setzt mal JavaDoc hier, alla
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
     * Stellt ein aktuell ausgewähltes Backup wiederher.
     * <p>
     * <p>
     * Das aktuell ausgewählte Backup.
     *
     * @return {@code true}, falls das Backup wiederhergestellt wurde, sonst {@code false}
     *         .
     */
    public boolean restore() {
        throw new UnsupportedOperationException();
    }
}
