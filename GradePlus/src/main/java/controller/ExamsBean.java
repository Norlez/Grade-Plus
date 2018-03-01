package controller;

import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import persistence.ExamDAO;
import persistence.InstanceLectureDAO;
import persistence.JoinExamDAO;
import persistence.UserDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static common.util.Assertion.assertNotNull;

/**
 * Diese Bean ist für die Anzeige der {@link Exam}-Objekte für sowohl Prüfer, Prüfling als
 * auch Admin verantwortlich. Außerdem können Prüfungen erstellt, verändert oder gelöscht
 * werden. Mittels {@link #createExamsForTimeFrame()} ist es möglich, Prüfungen innerhalb
 * eines gegebenen Zeitrahmens mit vorbestimmter Pausenlänge zwischen Prüfungen zu
 * erstellen.
 *
 * @author Andreas Estenfelder, Torben Groß
 * @version 2018-02-24
 */
@Named
@SessionScoped
public class ExamsBean extends AbstractBean implements Serializable {

    /**
     * Die eindeutige SerialisierungsID.
     */
    private static final long serialVersionUID = -1789862409211220952L;

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(UsersBean.class);

    /**
     * Die gewählte ILV.
     */
    private InstanceLecture instanceLecture;

    /**
     * Der aktuell ausgewählte Prüfungstermin. Wird verwendet, um Informationen
     * ausgewählter Prüfungen anzuzeigen, eine neue Prüfungs zu erstellen oder mittels
     * {@link #createExamsForTimeFrame()} mehrere Prüfungen auf einmal zu erstellen.
     */
    private Exam exam;

    /**
     * Wird von {@link #createExamsForTimeFrame()} verwendet, um Prüfungen zu speichern,
     * die aufgrund von Zeitkonflikten nicht gespeichert werden konnten.
     */
    private List<Exam> conflictingExams;

    /**
     * Wird von {@link #createExamsForTimeFrame()} verwendet, um den Startpunkt der
     * Prüfungen festzulegen.
     */
    private LocalDateTime startOfTimeSlot;

    /**
     * Wird von {@link #createExamsForTimeFrame()} verwendet, um den Endpunkt der
     * Prüfungen festzulegen.
     */
    private LocalDateTime endOfTimeSlot;

    /**
     * Wird von {@link #createExamsForTimeFrame()} verwendet, um die Länge der Pausen
     * zwischen Prüfungsterminen zu bestimmen.
     */
    private Integer lengthOfBreaks;

    /**
     * Eine Liste mit allen bekannten Prüfungsterminen.
     */
    private List<Exam> allExams;

    /**
     * Eine Liste mit allen Prüfungen der gewählten ILV, falls diese nicht {@code null}
     * ist.
     */
    private List<Exam> examsOfInstanceLecture;

    /**
     * Eine Liste mit allen Prüfungsterminen eines Prüflings.
     */
    private List<Exam> examsOfStudent;

    /**
     * Eine Liste mit allen Prüfungsterminen eines Prüfers.
     */
    private List<Exam> examsOfExaminer;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für {@link Exam}
     * -Objekte übernimmt.
     */
    private final ExamDAO examDao;

    /**
     * Wird benötigt, um in {@link #notify(User, String)} ein {@link MailBean}- Objekt
     * erzeugen zu können.
     */
    private final UserDAO userDao;

    /**
     * Wird benötigt, um beim Löschen einer Prüfung die zugehörigen {@link JoinExam}
     * -Objekte zu löschen.
     */
    private final JoinExamDAO joinExamDao;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für
     * {@link InstanceLecture}-Objekte übernimmt.
     */
    private final InstanceLectureDAO instanceLectureDao;

    /**
     * Erzeugt eine neue ExamsBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden ExamsBean.
     *
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    @Inject
    public ExamsBean(final Session pSession, final ExamDAO pExamDao,
            final UserDAO pUserDao, final InstanceLectureDAO pInstanceLectureDao,
            final JoinExamDAO pJoinExamDao) {
        super(pSession);
        examDao = assertNotNull(pExamDao, "ExamsBean: ExamsBean(_, ExamDAO, _, _, _)");
        userDao = assertNotNull(pUserDao, "ExamsBean: ExamsBean(_, _, UserDAO, _, _)");
        joinExamDao = assertNotNull(pJoinExamDao,
                "ExamsBean: ExamsBean(_, _, _, JoinExamDAO, _)");
        instanceLectureDao = assertNotNull(pInstanceLectureDao,
                "ExamsBean: ExamsBean(_, _, _, _, InstanceLectureDAO)");
    }

    /**
     * Initialisiert die Attribute {@link #exam}, {@link #allExams},
     * {@link #examsOfStudent} und {@link #examsOfExaminer}, sodass {@link #exam} eine zu
     * erstellende Prüfung repräsentiert, {@link #allExams} alle innerhalb des Systems
     * bekannten Prüfungen, {@link #examsOfStudent} alle Prüfungen eines Studenten und
     * {@link #examsOfExaminer} alle Prüfungen eines Prüfers enthält.
     */
    @PostConstruct
    public void init() {
        exam = new Exam();
        startOfTimeSlot = null;
        endOfTimeSlot = null;
        lengthOfBreaks = null;
        allExams = assertNotNull(examDao.getAllExams(),
                "ExamsBean: init() -> examDao.getAllExams()");
        examsOfStudent = assertNotNull(
                examDao.getExamsForStudent(getSession().getUser()),
                "ExamsBean: init() -> examDao.getExamsForStudent(getSession().getUser())");
        examsOfExaminer = assertNotNull(
                examDao.getExamsForExaminer(getSession().getUser()),
                "ExamsBean: init() -> examDao.getExamsForExaminer(getSession().getUser())");
    }

    /**
     * Gibt die aktuell gewählte ILV zurück oder {@code null}, falls keine ILV gewählt
     * ist.
     *
     * @return Die aktuell gewählte ILV oder {@code null}, falls keine ILV gewählt ist
     */
    public InstanceLecture getInstanceLecture() {
        return instanceLecture;
    }

    /**
     * Setzt die aktuell gewählte ILV auf den gegebenen Wert.
     *
     * @param pInstanceLecture
     *            Die neue aktuell geählte ILV.
     */
    public String setInstanceLectureForExam(final InstanceLecture pInstanceLecture) {
        instanceLecture = assertNotNull(pInstanceLecture,
                "ExamsBean: setInstanceLectureFotExam(InstanceLecture)");
        return "examcreate.xhtml";
    }

    /**
     * Setzt die aktuell gewählte ILV auf den gegebenen Wert.
     *
     * @param pInstanceLecture
     *            Die neue aktuell geählte ILV.
     */
    public String setInstanceLectureForExams(final InstanceLecture pInstanceLecture) {
        instanceLecture = assertNotNull(pInstanceLecture,
                "ExamsBean: setInstanceLectureForExams(InstanceLecture)");
        return "examscreate.xhtml";
    }

    /**
     * Gibt die zu erstellende Prüfung zurück.
     *
     * @return Die zu erstellende Prüfung.
     */
    public Exam getExam() {
        return exam;
    }

    /**
     * Setzt den ausgewählten Termin in der Bean.
     *
     * @param pExam
     *            Der vom Püfer ausgewählte Termin.
     */
    public void setExam(final Exam pExam) {
        exam = assertNotNull(pExam, "ExamsBean: setExam(Exam)");
    }

    /**
     * Gibt die Liste aller innerhalb des Systems bekannten Prüfungstermine zurück.
     *
     * @return Die Liste aller innerhalb des Systems bekannten Prüfungstermine.
     */
    public List<Exam> getAllExams() {
        return allExams;
    }

    /**
     * Gibt die Liste aller Prüfungen der gewählten ILV zurück oder {@code null}, falls
     * {@link #instanceLecture} {@code null} ist.
     *
     * @return Die Liste aller Prüfungen der gewählten ILV oder {@code null}, falls
     *         {@link #instanceLecture} {@code null} ist.
     */
    public List<Exam> getExamsOfInstanceLecture() {
        return instanceLecture == null ? null : examDao
                .getExamsForInstanceLecture(instanceLecture);
    }

    /**
     * Gibt alle Prüflinge der Prüfung als Liste zurück.
     *
     * @return Alle Prüflinge der Prüfung als Liste.
     */
    public List<User> getStudents(final Exam pExam) {
        assertNotNull(pExam, "ExamsBean: getStudent(Exam)");
        List<User> students = new ArrayList<>();
        List<JoinExam> joinExams = pExam.getParticipants();
        for (JoinExam joinExam : joinExams) {
            students.add(joinExam.getPruefling());
        }
        return students;
    }

    /**
     * Fügt die aktuell angezeigte Prüfung der Liste aller innerhalb der Applikation
     * bekannten Prüfungen hinzu.
     *
     * @return "lectureinstance.xhtml", um auf das Facelet der Übersicht der Prüfungen zu
     *         leiten.
     */
    public String save() {
        if (!isTimeSlotEmpty(exam.getLocalDateTime(), exam.getLocalDateTime()
                .plusMinutes(exam.getExamLength()))) {
            return null;
        }
        User user = getSession().getUser();
        exam.setInstanceLecture(instanceLecture);
        exam.addExaminer(user);
        exam.getInstanceLecture().addExam(exam);
        user.addExamAsProf(exam);
        try {
            examDao.save(exam);
            instanceLectureDao.update(exam.getInstanceLecture());
            userDao.update(user);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorInputdataIncomplete"));
        } catch (final UnexpectedUniqueViolationException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("someError"));
        } catch (final DuplicateUsernameException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorUsernameAlreadyInUse", user.getUsername());
        } catch (final DuplicateEmailException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorEmailAlreadyInUse", user.getEmail());
        }
        init();
        return "exams.xhtml";
    }

    /**
     * Aktualisiert die aktuell angezeigte Prüfung in der Liste aller innerhalb der
     * Applikation bekannten Prüfungen.
     *
     * @return "exams.xhtml", um auf das Facelet der Übersicht der Prüfungen zu leiten.
     */
    public String update() {
        try {
            Exam oldExam = examDao.getById(exam.getId());
            List<User> students = getStudents(oldExam);
            examDao.update(exam);
            String message = String
                    .format("Die Daten des Prüfungstermins für %s am %s um %s Uhr, wurden angepasst.\n\nNeue Daten:\nDatum: %s\nUhrzeit: %s Uhr\nDauer: %i Minuten",
                            oldExam.getInstanceLecture().getLecture().getName(), oldExam
                                    .getLocalDateTime().toLocalDate().toString(), oldExam
                                    .getLocalDateTime().toLocalTime().toString(), exam
                                    .getLocalDateTime().toLocalDate().toString(), exam
                                    .getLocalDateTime().toLocalTime().toString(),
                            exam.getExamLength());
            for (User student : students) {
                notify(student, message);
            }
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorInputdataIncomplete"));
        } catch (final UnexpectedUniqueViolationException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("someError"));
        }
        init();
        return "exams.xhtml";
    }

    /**
     * Entfernt die übergebene Prüfung aus der Liste aller bekannten Prüfungen unter
     * Verwendung des entsprechenden Data-Access-Objekts. Sollte die zu entfernende
     * Prüfung nicht in der Liste der Prüfungen vorhanden sein, passiert nichts.
     *
     * Ablauf: {@link Exam}-Objekt wird gelöscht. Die entsprechenden {@link JoinExam}
     * -Objekte der Prüflinge werden daraufhin gelöscht. Bei den Prüfern wird nun jeweils
     * das entsprechende {@link Exam}-Objekt aus der Prüferliste entfernt, woraufhin
     * Prüflinge und Prüfer geupdated werden. Zum Schluss wird die Prüfung aus der
     * entsprechenden {@link InstanceLecture} gelöscht und diese geupdated.
     *
     * @param pExam
     *            Die zu entfernende Prüfung.
     *
     * @return {code null} damit nicht zu einem anderen Facelet navigiert wird.
     */
    public String remove(final Exam pExam) {
        List<User> examiners = exam.getExaminers();
        List<User> students = getStudents(pExam);
        List<User> updatedUsers = new ArrayList<>();
        examDao.remove(pExam);
        String message = String
                .format("Der Prüfungstermin für %s am %s um %s Uhr wurde gelöscht. Bitte melden Sie sich bei Bedarf zu einem neuen Termin an.",
                        pExam.getInstanceLecture().getLecture().getName(), pExam
                                .getLocalDateTime().toLocalDate().toString(), pExam
                                .getLocalDateTime().toLocalTime().toString());
        for (User student : students) {
            List<JoinExam> joinExams = student.getParticipation();
            for (JoinExam joinExam : joinExams) {
                if (joinExam.getExam() == pExam) {
                    joinExams.remove(joinExam);
                    joinExamDao.remove(joinExam);
                    break;
                }
            }
            updatedUsers.add(student);
            notify(student, message);
        }
        for (User examiner : examiners) {
            examiner.removeExamAsProf(pExam);
            updatedUsers.add(examiner);
        }
        for (User updatedUser : updatedUsers) {
            try {
                userDao.update(updatedUser);
            } catch (final DuplicateUsernameException e) {
                addErrorMessageWithLogging("registerUserForm:username", e, logger,
                        Level.DEBUG, "errorUsernameAlreadyInUse",
                        updatedUser.getUsername());
            } catch (final DuplicateEmailException e) {
                addErrorMessageWithLogging("registerUserForm:email", e, logger,
                        Level.DEBUG, "errorEmailAlreadyInUse", updatedUser.getEmail());
            }
        }
        instanceLecture.removeExam(pExam);
        instanceLectureDao.update(instanceLecture);
        init();
        return null;
    }

    /**
     * Meldet den eingeloggten Benutzer an der gegebenen Prüfung an.
     *
     * @param pExam
     *            Die Prüfung.
     *
     * @return "exams.xhtml", um auf das Facelet der Übersicht über die Prüfungen
     *         umzuleiten.
     */
    public String registerAsStudent(final Exam pExam) {
        assertNotNull(pExam, "ExamsBean: registerAsStudent(Exam)");
        User user = getSession().getUser();
        if (!user.getAsStudent().contains(exam.getInstanceLecture())) {
            return "exams.xhtml";
        }
        JoinExam joinExam = new JoinExam();
        joinExam.setExam(pExam);
        joinExam.setPruefling(user);
        joinExam.setKind(Anmeldeart.SELBER);
        pExam.addParticipant(joinExam);
        try {
            userDao.update(user);
            joinExamDao.save(joinExam);
            examDao.update(pExam);
        } catch (final DuplicateUsernameException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorUsernameAlreadyInUse", user.getUsername());
        } catch (final DuplicateEmailException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorEmailAlreadyInUse", user.getEmail());
        }
        return "exams.xhtml";
    }

    /**
     * Meldet den eingeloggten Benutzer von der gegebenen Prüfung ab.
     *
     * @param pExam
     *            Die Prüfung.
     *
     * @return "exams.xhtml", um auf das Facelet der Übersicht über die Prüfungen
     *         umzuleiten.
     */
    public String deregisterAsStudent(final Exam pExam) {
        assertNotNull(pExam, "ExamsBean: deregisterAsStudent(Exam)");
        User user = getSession().getUser();
        List<JoinExam> joinExams = user.getParticipation();
        JoinExam joinExam = null;
        for (JoinExam theJoinExam : joinExams) {
            if (theJoinExam.getExam() == pExam) {
                joinExam = theJoinExam;
                user.removeParticipation(joinExam);
                pExam.removeParticipant(joinExam);
                break;
            }
        }
        if (joinExam == null) {
            addErrorMessageWithLogging(new IllegalArgumentException(
                    "An error has occurred."), logger, Level.DEBUG,
                    getTranslation("errorUserdataIncomplete"));
            return null;
        }
        try {
            userDao.update(user);
            joinExamDao.remove(joinExam);
            examDao.update(pExam);
        } catch (final DuplicateUsernameException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorUsernameAlreadyInUse", user.getUsername());
        } catch (final DuplicateEmailException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorEmailAlreadyInUse", user.getEmail());
        }
        return "exams.xhtml";
    }

    /**
     * Fügt den gegebenen Benutzer der aktuell ausgewählten Prüfung hinzu. Vor dem
     * Hinzufügen des Prüfers muss {@link #exam} auf eine bereits in der Datenbank
     * existierende Prüfung gesetzt sein.
     *
     * @param pUser
     *            Der der Prüfung als Prüfer hinzuzufügende Benutzer.
     *
     * @return "exams.xhtml", um auf das Facelet der Übersicht über die Prüfungen
     *         umzuleiten.
     *
     * @throws IllegalArgumentException
     *             Falls {@link #exam} nicht in der Datenbank existiert.
     */
    public String addExaminer(final User pUser) {
        assertNotNull(pUser, "ExamsBean: addExaminer(User)");
        pUser.addExamAsProf(exam);
        exam.addExaminer(pUser);
        try {
            userDao.update(pUser);
            examDao.update(exam);
        } catch (final DuplicateUsernameException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorUsernameAlreadyInUse", pUser.getUsername());
        } catch (final DuplicateEmailException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorEmailAlreadyInUse", pUser.getEmail());
        }
        return "exams.xhtml";
    }

    /**
     * Entfernt den gegebenen Benutzer der aktuell ausgewählten Prüfung. Vor dem Entfernen
     * des Prüfers muss {@link #exam} auf eine bereits in der Datenbank existierende
     * Prüfung gesetzt sein.
     *
     * @param pUser
     *            Der der Prüfung als Prüfer zu entfernende Benutzer.
     *
     * @return "exams.xhtml", um auf das Facelet der Übersicht über die Prüfungen
     *         umzuleiten.
     *
     * @throws IllegalArgumentException
     *             Falls {@link #exam} nicht in der Datenbank existiert.
     */
    public String removeExaminer(final User pUser) {
        assertNotNull(pUser, "ExamsBean: removeExaminer(User)");
        pUser.removeExamAsProf(exam);
        exam.removeExaminer(pUser);
        try {
            userDao.update(pUser);
            examDao.update(exam);
        } catch (final DuplicateUsernameException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorUsernameAlreadyInUse", pUser.getUsername());
        } catch (final DuplicateEmailException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorEmailAlreadyInUse", pUser.getEmail());
        }
        return "exams.xhtml";
    }

    /**
     * Erzeugt {@link Exam}-Objekte mit gegebenen Stammdaten innerhalb des angegebenen
     * Zeitraums mit entsprechenden Pausen zwischen Prüfungen.
     *
     * Diese Methode verwendet die Angaben innerhalb von {@link #exam}, um die Stammdaten
     * aller zu erstellenden Prüfungen zu bestimmen. Der Start- und Endpunkt der Prüfungen
     * sowie die Länge der Pausen zwischen den Prüfungen wird aus {@link #startOfTimeSlot}
     * , {@link #endOfTimeSlot} und {@link #lengthOfBreaks} entnommen. Entsprechend müssen
     * alle Attribute über das Facelet bei Aufruf dieser Methode bereits gesetzt sein.
     *
     * Können Prüfungen aufgrund von Zeitkonflikten nicht gespeichert werden, werden diese
     * in {@link #conflictingExams} gespeichert und können dort ausgelesen werden, bis
     * {@link #createExamsForTimeFrame()} erneut aufgerufen wird.
     *
     * @return "exams.xhtml", um auf das Facelet der Übersicht der Prüfungen zu leiten.
     */
    public String createExamsForTimeFrame() {
        if (startOfTimeSlot == null || endOfTimeSlot == null || lengthOfBreaks == null) {
            addErrorMessageWithLogging(new IllegalArgumentException(
                    "startOfTimeSlot, endOfTimeSlot or lengthOfBreaks is NULL."), logger,
                    Level.DEBUG, getTranslation("errorInputdataIncomplete"));
            return "exams.xhtml";
        }
        conflictingExams = new ArrayList<>();
        exam.setLocalDateTime(startOfTimeSlot);
        LocalDateTime theEndOfTimeSlot = endOfTimeSlot;
        Integer theLengthOfBreaks = lengthOfBreaks;
        while (exam.getLocalDateTime().plusMinutes(exam.getExamLength())
                .compareTo(theEndOfTimeSlot) <= 0) {
            Exam theExam = exam;
            if (!isTimeSlotEmpty(exam.getLocalDateTime(), exam.getLocalDateTime()
                    .plusMinutes(exam.getExamLength()))) {
                conflictingExams.add(exam);
            } else {
                save();
            }
            exam = new Exam(theExam);
            exam.setLocalDateTime(exam.getLocalDateTime().plusMinutes(
                    exam.getExamLength() + lengthOfBreaks));
        }
        init();
        return "exams.xhtml";
    }

    /**
     * Gibt alle Prüfungen als Liste zurück, die beim Aufruf von
     * {@link #createExamsForTimeFrame()} aufgrund von Zeitkonflikten nicht gespeichert
     * werden konnten.
     *
     * @return Alle Prüfungen als Liste, die nicht gespeichert wurden.
     */
    public List<Exam> getConflictingExams() {
        return conflictingExams;
    }

    /**
     * Gibt den Startpunkt der zu erstellenden Prüfung zurück.
     *
     * @return Den Startpunkt der zu erstellenden Prüfung.
     */
    public java.util.Date getLocalDateTime() {
        if (exam.getLocalDateTime() == null) {
            return null;
        }
        return Date.from(exam.getLocalDateTime().atZone(ZoneId.systemDefault())
                .toInstant());
    }

    /**
     * Setzt den Startpunkt der Prüfung auf den gegebenen Wert.
     *
     * @param pDate
     *            Der neue Startpunkt der Prüfung.
     */
    public void setLocalDateTime(final java.util.Date pDate) {
        if (pDate != null) {
            exam.setLocalDateTime(LocalDateTime.ofInstant(pDate.toInstant(),
                    ZoneId.systemDefault()));
        }
    }

    /**
     * Gibt den Startpunkt der zu erstellenden Prüfungen zurück.
     *
     * @return Den Startpunkt der zu erstellenden Prüfungen.
     */
    public java.util.Date getStartOfTimeSlot() {
        return startOfTimeSlot == null ? null : Date.from(startOfTimeSlot.atZone(
                ZoneId.systemDefault()).toInstant());
    }

    /**
     * Setzt den Startpunkt der Prüfungen auf den gegebenen Wert.
     *
     * @param pStartOfTimeSlot
     *            Der neue Startpunkt der Prüfungen.
     */
    public void setStartOfTimeSlot(final java.util.Date pStartOfTimeSlot) {
        assertNotNull(pStartOfTimeSlot, "ExamsBean: setStartOfTimeSlot(Date)");
        startOfTimeSlot = LocalDateTime.ofInstant(pStartOfTimeSlot.toInstant(),
                ZoneId.systemDefault());
    }

    /**
     * Gibt den Endpunkt der Prüfungen zurück.
     *
     * @return Den Endpunkt der Prüfungen.
     */
    public java.util.Date getEndOfTimeSlot() {
        return endOfTimeSlot == null ? null : Date.from(endOfTimeSlot.atZone(
                ZoneId.systemDefault()).toInstant());
    }

    /**
     * Setzt den Endpunkt der Prüfungen auf den gegebenen Wert.
     *
     * @param pEndOfTimeSlot
     *            Der neue Endpunkt der Prüfungen.
     */
    public void setEndOfTimeSlot(final java.util.Date pEndOfTimeSlot) {
        assertNotNull(pEndOfTimeSlot, "ExamsBean: setEndOfTimeSlot(Date)");
        endOfTimeSlot = LocalDateTime.ofInstant(pEndOfTimeSlot.toInstant(),
                ZoneId.systemDefault());
    }

    /**
     * Gibt die Länge der Pausen zwischen den Prüfungen zurück.
     *
     * @return Die Länge der Pausen zwischen den Prüfungen.
     */
    public Integer getLengthOfBreaks() {
        return lengthOfBreaks;
    }

    /**
     * Setzt die Länge der Pausen zwischen den Prüfungen auf den gegebenen Wert.
     *
     * @param pLengthOfBreaks
     *            Die neue Länge der Pausen zwischen den Prüfungen.
     */
    public void setLengthOfBreaks(final Integer pLengthOfBreaks) {
        lengthOfBreaks = assertNotNull(pLengthOfBreaks,
                "ExansBean: setLengthOfBreaks(Integer)");
    }

    /**
     * Sendet eine Nachricht an den Prüfling, um ihm über Änderungen eines Prüfungstermins
     * zu informieren.
     *
     * @param pUser
     *            Der {@link User}, der über Änderungen einer Prüfung benachrichtigt
     *            werden soll.
     * @param pMessage
     *            Die zu sendende Nachricht.
     */
    private void notify(final User pUser, final String pMessage) {
        MailBean sender = new MailBean(getSession());
        sender.getMail().setTopic("Änderungen an einem Prüfungstermin");
        sender.getMail().setContent(
                assertNotNull(pMessage, "ExamsBean: notify(_, String)"));
        sender.getMail().setRecipient(pUser.getEmail());
        sender.sendSystemMail();
    }

    /**
     * Prüft, ob bereits eine Prüfung am angegebenen Zeitslot stattfindet.
     *
     * @param lowerBound
     *            Die untere Grenze des zu prüfenden Zeitslots.
     * @param upperBound
     *            Die obere Grenze des zu Prüfenden Zeitslots.
     *
     * @return {@code true} Falls im gegebenen Zeitraum bereits eine Prüfung stattfindet,
     *         sonst {@code false}.
     */
    private boolean isTimeSlotEmpty(final LocalDateTime lowerBound,
            final LocalDateTime upperBound) {

        for (Exam currExam : examsOfExaminer) {

            LocalDateTime currLowerBound = currExam.getLocalDateTime();
            LocalDateTime currUpperBound = currLowerBound.plusMinutes(currExam
                    .getExamLength());

            if ((lowerBound.isBefore(currLowerBound) && upperBound
                    .isAfter(currLowerBound))
                    || (currLowerBound.isBefore(lowerBound) && currUpperBound
                            .isAfter(lowerBound)) || lowerBound.equals(currLowerBound)) {
                return false;
            }
        }
        return true;
    }

}
