package controller;

import common.model.*;
import common.util.Assertion;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;
import com.opencsv.*;
import persistence.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

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

    private final JoinExamDAO joinExamDAO;

    private List<User> allUsers;

    private List<Lecture> allLectures;

    private List<InstanceLecture> allInstanceLectures;

    private List<Exam> allExams;

    private List<JoinExam> allJoinExams;

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
            JoinExamDAO pJoinExamDao) {
        super(pSession);
        userDAO = pUserDao;
        lectureDAO = pLectureDao;
        instanceLectureDAO = pInstanceLectureDao;
        examDAO = pExamDao;
        joinExamDAO = pJoinExamDao;
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
    }

    /**
     * Erstellt für den aktuellen Stand der Datenbank ein Backup.
     *
     * @return {@code true}, falls das Backup erstellt wurde, sonst {@code false}.
     */
    public void create() {

    }

    /**
     * Setzt mal JavaDoc hier, alla
     */
    public void createBackup() {
        try {
            Writer out = new BufferedWriter(new FileWriter("User.csv"));
            CSVWriter csvWriter = new CSVWriter(out, ';', CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END); // LIne-End
            // könnte
            // wehtun
            List<User> u = allUsers;
            String[] s = new String[1];
            for (int i = 0; i < u.size(); i++) {
                User tmp = u.get(i);
                s[0] = tmp.toString();
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
