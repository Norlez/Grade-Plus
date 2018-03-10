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
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
     * Speichert die ausgewählte Prüfung temporär.
     */
    private Exam selectedExam;

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
     * Diese Map wird benötigt, um Prüfer oder Prüflinge mittels Checkbox auswählen zu
     * können.
     */
    private Map<Long, Boolean> checked;

    /**
     * Falls eine Prüfung im gewählten Zeitraum bereits existiert, wird dieser Wert auf
     * {@code true} gesetzt, und somit kann die Prüfung bei Bedarf dennoch gespeichert
     * werden.
     */
    private boolean alreadyExists;

    /**
     * Die Liste aller innherhalb der Applikation bekannten Prüfungstypen.
     */
    private List<String> examTypes;

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
        examTypes = calculateExamTypeList();
    }

    /**
     * Initialisiert die Attribute der ExamsBean.
     */
    @PostConstruct
    public void init() {
        exam = exam == null || exam.getInstanceLecture() == null ? new Exam() : new Exam(
                exam.getInstanceLecture());
        startOfTimeSlot = null;
        endOfTimeSlot = null;
        lengthOfBreaks = null;
        checked = new HashMap<>();
        alreadyExists = false;
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
     * Setzt die aktuelle Prüfung auf den gegebenen Wert.
     *
     * @param pExam
     *            Die aktuelle Prüfung.
     * @return "examedit.xhtml", um auf das Facelet der Bearbeitung einer Prüfung
     *         weiterzuleiten.
     */
    public String setExam(final Exam pExam) {
        init();
        exam = assertNotNull(pExam);
        exam.getInstanceLecture().getExaminers()
                .forEach(u -> checked.put(u.getId(), exam.getExaminers().contains(u)));
        return "examedit.xhtml";
    }

    /**
     * Gibt die Liste aller innerhalb des Systems bekannten Prüfungstermine zurück.
     *
     * @return Die Liste aller innerhalb des Systems bekannten Prüfungstermine.
     */
    public List<Exam> getAllExams() {
        return assertNotNull(examDao.getAllExams(),
                "ExamsBean: getAllExamt() -> examDao.getAllExams()");
    }

    /**
     * Gibt die Liste aller Prüfungen zurück, in denen der angemeldete Benutzer Prüfling
     * ist.
     *
     * @return Alle Prüfungen, in denen der angemeldete Benutzer Prüfling ist.
     */
    public List<Exam> getExamsForStudent() {
        return assertNotNull(examDao.getExamsForStudent(getSession().getUser()),
                "ExamsBean: getAllExams() -> examDao.getExamsForStudent(getSession().getUser())");
    }

    /**
     * Gibt die Liste aller Prüfungen zurück, in denen der angemeldete Benutzer Prüfer
     * ist.
     *
     * @return Alle Prüfungen, in denen der angemeldete Benutzer Prüfer ist.
     */
    public List<Exam> getExamsForExaminer() {
        return examDao.getAllExams().stream()
                .filter(e -> e.getExaminers().contains(getSession().getUser()))
                .collect(Collectors.toList());
        // return assertNotNull(examDao.getExamsForExaminer(getSession().getUser()),
        // "ExamsBean: getAllExams() -> examDao.getExamsForExaminer(getSession().getUser())");
    }

    /**
     * Gibt die temporär gespeicherte Exam zurück
     * 
     * @return
     */
    public Exam getSelectedExam() {
        return selectedExam;
    }

    /**
     * Setzt die aktuell gewählte ILV auf den gegebenen Wert.
     *
     * @param pInstanceLecture
     *            Die neue aktuell geählte ILV.
     */
    public String setInstanceLectureForExam(final InstanceLecture pInstanceLecture) {
        init();
        exam.setInstanceLecture(assertNotNull(pInstanceLecture,
                "ExamsBean: setInstanceLectureFotExam(InstanceLecture)"));
        return "examcreate.xhtml";
    }

    /**
     * Setzt die aktuell gewählte ILV auf den gegebenen Wert.
     *
     * @param pInstanceLecture
     *            Die neue aktuell geählte ILV.
     */
    public String setInstanceLectureForExams(final InstanceLecture pInstanceLecture) {
        init();
        exam.setInstanceLecture(assertNotNull(pInstanceLecture,
                "ExamsBean: setInstanceLectureForExams(InstanceLecture)"));
        return "examscreate.xhtml";
    }

    /**
     * Fügt die aktuell angezeigte Prüfung der Liste aller innerhalb der Applikation
     * bekannten Prüfungen hinzu.
     *
     * @return "lectureinstance.xhtml", um auf das Facelet der Übersicht der Prüfungen zu
     *         leiten.
     */
    public String save() {
        if (startOfTimeSlot == null
                && endOfTimeSlot == null
                && exam.getLocalDateTime().compareTo(
                        LocalDateTime.ofInstant(exam.getInstanceLecture()
                                .getTermOfApplication().toInstant(),
                                ZoneId.systemDefault())) < 0) {
            addErrorMessage("errorStartOfExamBeforeTermOfApplication");
            return null;
        }
        if (!isTimeSlotEmpty(exam.getLocalDateTime(), exam.getLocalDateTime()
                .plusMinutes(exam.getExamLength()))) {
            alreadyExists = !alreadyExists;
            if (alreadyExists) {
                return null;
            }
        }
        List<User> examiners = checked.entrySet().stream().filter(Map.Entry::getValue)
                .map(Map.Entry::getKey).map(userDao::getById)
                .collect(Collectors.toList());
        examiners.add(getSession().getUser());
        exam.setExaminers(examiners);
        exam.getInstanceLecture().addExam(exam);
        // examiners.forEach(e -> e.addAsProfToIlv(exam.getInstanceLecture()));
        // addAsProfToIlv pls
        examiners.forEach(exam.getInstanceLecture()::addExaminer);
        try {
            examDao.save(exam);
            instanceLectureDao.update(exam.getInstanceLecture());
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorInputdataIncomplete"));
        } catch (final UnexpectedUniqueViolationException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("someError"));
        }
        for (User examiner : examiners) {
            try {
                examiner.addExamAsProf(exam);
                userDao.update(examiner);
            } catch (final DuplicateUsernameException e) {
                addErrorMessageWithLogging("registerUserForm:username", e, logger,
                        Level.DEBUG, "errorUsernameAlreadyInUse", examiner.getUsername());
            } catch (final DuplicateEmailException e) {
                addErrorMessageWithLogging("registerUserForm:email", e, logger,
                        Level.DEBUG, "errorEmailAlreadyInUse", examiner.getEmail());
            }
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
        if (!checked.isEmpty()
                && checked.entrySet().stream().filter(Map.Entry::getValue)
                        .collect(Collectors.toList()).isEmpty()) {
            addErrorMessage("errorNoExaminersInExam");
            return null;
        }
        if (exam.getLocalDateTime().compareTo(
                LocalDateTime.ofInstant(exam.getInstanceLecture().getTermOfApplication()
                        .toInstant(), ZoneId.systemDefault())) < 0) {
            addErrorMessage("errorStartOfExamBeforeTermOfApplication");
            return null;
        }
        if (exam.getParticipants().size() > exam.getGroupSize()) {
            addErrorMessage("errorGroupSizeSmallerThanExaminees");
            return null;
        }
        if (!isTimeSlotEmpty(exam.getLocalDateTime(), exam.getLocalDateTime()
                .plusMinutes(exam.getExamLength()))) {
            alreadyExists = !alreadyExists;
            if (alreadyExists) {
                return null;
            }
        }
        try {
            Exam oldExam = examDao.getById(exam.getId());
            List<User> students = oldExam.getStudents();
            if (!checked.isEmpty()) {
                for (User theExaminer : oldExam.getExaminers()) {
                    removeExaminer(theExaminer);
                }
                for (User theExaminer : checked.entrySet().stream()
                        .filter(Map.Entry::getValue).map(Map.Entry::getKey)
                        .map(userDao::getById).collect(Collectors.toList())) {
                    addExaminer(theExaminer);
                }
            }
            examDao.update(exam);
            String message = String
                    .format("Die Daten des Prüfungstermins für %s am %s um %s Uhr, wurden angepasst.\n\nNeue Daten:\nDatum: %s\nUhrzeit: %s Uhr\nDauer: %d Minuten",
                            oldExam.getInstanceLecture().getLecture().getName(),
                            oldExam.dateToString(), oldExam.timeToString(),
                            exam.dateToString(), exam.timeToString(),
                            exam.getExamLength());
            if (!oldExam.getLocalDateTime().equals(exam.getLocalDateTime())
                    || !oldExam.getExamLength().equals(exam.getExamLength())
                    || !oldExam.getLocation().equals(exam.getLocation())) {
                students.forEach(s -> notify(s, message));
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
        assertNotNull(pExam, "ExamsBean: remove(Exam)");
        for (User theStudent : pExam.getStudents()) {
            SystemMailBean.reportExamCancel(theStudent, pExam);
            deregisterAsStudent(theStudent, pExam);
        }
        examDao.remove(pExam);
        List<User> updatedUsers = new ArrayList<>();
        String message = String
                .format("Der Prüfungstermin für %s am %s um %s Uhr wurde gelöscht. Bitte melden Sie sich bei Bedarf zu einem neuen Termin an.",
                        pExam.getInstanceLecture().getLecture().getName(), pExam
                                .getLocalDateTime().toLocalDate().toString(), pExam
                                .getLocalDateTime().toLocalTime().toString());
        for (User examiner : pExam.getExaminers()) {
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
        pExam.getInstanceLecture().removeExam(pExam);
        instanceLectureDao.update(pExam.getInstanceLecture());
        init();
        return "exams.xhtml";
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
        // if (!user.getAsStudent().contains(exam.getInstanceLecture())) {
        // return "examregister.xhtml";
        // }
        JoinExam joinExam = null;
        List<JoinExam> joinExamList = joinExamDao.getJoinExamsForUser(user);
        for (JoinExam j : joinExamList) {
            if (j.getInstanceLecture().getId() == pExam.getInstanceLecture().getId()
                    && j.getExam() == null) {
                joinExam = j;
            }
        }
        // JoinExam joinExam = new JoinExam();
        joinExam.setExam(pExam);
        // joinExam.setPruefling(user);
        // joinExam.setKind(Anmeldeart.BYPROF);
        joinExamDao.update(joinExam);
        pExam.addParticipant(joinExam);
        examDao.update(pExam);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        User registeredUser = getSession().getUser();
        registeredUser.setLoggingString(date.toString() + ": Anmeldung für "
                + pExam.getInstanceLecture().getLecture().getName() + " Prüfung am "
                + pExam.dateToString() + " um " + pExam.timeToString() + "\n");
        try {
            userDao.update(registeredUser);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorUserdataIncomplete"));
        } catch (final DuplicateUsernameException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorUsernameAlreadyInUse",
                    registeredUser.getUsername());
        } catch (final DuplicateEmailException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorEmailAlreadyInUse", registeredUser.getEmail());
        }

        return "dashboard.xhtml";
    }

    /**
     * Meldet den eingeloggten Benutzer von der gegebenen Prüfung ab.
     *
     * @param pUser
     *            Der zu entfernende Prüfling.
     *
     * @param pExam
     *            Die Prüfung.
     *
     * @return "exams.xhtml", um auf das Facelet der Übersicht über die Prüfungen
     *         umzuleiten.
     */
    public String deregisterAsStudent(final User pUser, final Exam pExam) {
        assertNotNull(pUser, "ExamsBean: deregisterAsStudent(User, _)");
        assertNotNull(pExam, "ExamsBean: deregisterAsStudent(_, Exam)");
        List<JoinExam> joinExams = pExam.getParticipants();
        JoinExam joinExam = null;
        for (JoinExam theJoinExam : joinExams) {
            if (theJoinExam.getExam() != null && theJoinExam.getExam().equals(pExam)) {
                joinExam = theJoinExam;
                pExam.removeParticipant(joinExam);
                break;
            }
        }
        if (joinExam == null) {
            addErrorMessageWithLogging(new IllegalArgumentException(
                    "An error has occurred."), logger, Level.DEBUG,
                    getTranslation("someError"));
            return null;
        }

        joinExam.setExam(null);
        joinExamDao.update(joinExam);
        examDao.update(pExam);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        User registeredUser = getSession().getUser();
        registeredUser.setLoggingString(date.toString() + ": Abmeldung für "
                + pExam.getInstanceLecture().getLecture().getName() + " Prüfung am "
                + pExam.dateToString() + " um " + pExam.timeToString() + "\n");
        try {
            userDao.update(registeredUser);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorUserdataIncomplete"));
        } catch (final DuplicateUsernameException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorUsernameAlreadyInUse",
                    registeredUser.getUsername());
        } catch (final DuplicateEmailException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorEmailAlreadyInUse", registeredUser.getEmail());
        }

        return "dashboard.xhtml";
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

    public String deregisterAsExaminer(final Exam pExam) {
        Exam theExam = exam;
        exam = assertNotNull(pExam);
        removeExaminer(getSession().getUser());
        exam = theExam;
        return "dashboard.xhtml";
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
        if (startOfTimeSlot.compareTo(LocalDateTime.ofInstant(exam.getInstanceLecture()
                .getTermOfApplication().toInstant(), ZoneId.systemDefault())) < 0) {
            addErrorMessage("errorStartOfTimeFrameBeforeTermOfApplication");
            return null;
        }
        if (startOfTimeSlot.compareTo(endOfTimeSlot) >= 0) {
            addErrorMessage("errorEndOfTimeFrameNotAfterStartOfTimeFrame");
            return null;
        }
        if (startOfTimeSlot == null || endOfTimeSlot == null || lengthOfBreaks == null) {
            addErrorMessageWithLogging(new IllegalArgumentException(
                    "startOfTimeSlot, endOfTimeSlot or lengthOfBreaks is NULL."), logger,
                    Level.DEBUG, getTranslation("errorInputdataIncomplete"));
        }
        conflictingExams = new ArrayList<>();
        exam.setLocalDateTime(startOfTimeSlot);
        LocalDateTime theEndOfTimeSlot = endOfTimeSlot;
        Integer theLengthOfBreaks = lengthOfBreaks;
        while (exam.getLocalDateTime().plusMinutes(exam.getExamLength())
                .compareTo(theEndOfTimeSlot) <= 0) {
            Exam theExam = exam;
            Map<Long, Boolean> theChecked = checked;
            if (!isTimeSlotEmpty(exam.getLocalDateTime(), exam.getLocalDateTime()
                    .plusMinutes(exam.getExamLength()))) {
                conflictingExams.add(exam);
            }
            startOfTimeSlot = exam.getLocalDateTime();
            boolean localAlreadyExists = alreadyExists;
            save();
            if (localAlreadyExists) {
                alreadyExists = true;
            } else if (alreadyExists) {
                return null;
            }
            checked = theChecked;
            exam = new Exam(theExam);
            exam.setLocalDateTime(exam.getLocalDateTime().plusMinutes(
                    exam.getExamLength() + theLengthOfBreaks));
        }
        init();
        return "exams.xhtml";
    }

    /**
     *
     * Gibt die Liste der Prüfungsarten zurück.
     * 
     * @return die Liste von Prüfungsarten.
     */
    public List<String> getExamTypes() {
        return examTypes;
    }

    /**
     * Setzt die typen von möglichen Prüfungen im System.
     *
     */
    public void setExamTypes(List<String> examTypes) {
        this.examTypes = assertNotNull(examTypes);
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
        startOfTimeSlot = LocalDateTime
                .ofInstant(pStartOfTimeSlot.toInstant(), ZoneId.systemDefault())
                .withSecond(0).withNano(0);
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
        endOfTimeSlot = LocalDateTime
                .ofInstant(pEndOfTimeSlot.toInstant(), ZoneId.systemDefault())
                .withSecond(0).withNano(0);
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
     * Gibt die Map der Checkbox zurück.
     *
     * @return Die Map der Checkbox.
     */
    public Map<Long, Boolean> getChecked() {
        return checked;
    }

    /**
     * Setzt die Map der Checkbox auf den gegebenen Wert.
     *
     * @param pChecked
     *            Die zu setzende Map der Checkbox.
     */
    public void setChecked(final Map<Long, Boolean> pChecked) {
        checked = assertNotNull(pChecked);
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
        MailBean sender = new MailBean(getSession(), userDao);
        /*
         * sender.getMail().setTopic("Änderungen an einem Prüfungstermin");
         * sender.getMail().setContent( assertNotNull(pMessage,
         * "ExamsBean: notify(_, String)")); sender.getMail().setRecipient(
         * assertNotNull(pUser, "ExamsBean: notify(User, _)").getEmail());
         * sender.sendSystemMail();
         */
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

        for (Exam currExam : getExamsForExaminer()) {

            LocalDateTime currLowerBound = currExam.getLocalDateTime();
            LocalDateTime currUpperBound = currLowerBound.plusMinutes(currExam
                    .getExamLength());

            if (!currExam.getId().equals(exam.getId())
                    && ((lowerBound.isBefore(currLowerBound) && upperBound
                            .isAfter(currLowerBound))
                            || (currLowerBound.isBefore(lowerBound) && currUpperBound
                                    .isAfter(lowerBound)) || lowerBound
                                .equals(currLowerBound))) {
                return false;
            }
        }
        return true;
    }

    public String releaseExam(final Exam pExam) {
        assertNotNull(pExam).setReleased(true);
        Exam theExam = exam;
        exam = pExam;
        update();
        exam = theExam;
        return "exams.xhtml";
    }

    /**
     * Setzt eine oder mehrere Exams auf den Status freigegeben für die Prüflinge.
     *
     * @param pExams
     *            mit allen zu ändernden Exams.
     */
    public void releaseExams(List<Exam> pExams) {
        if (getSession().getUser().getRole() == Role.EXAMINER) {
            assertNotNull(pExams);
            for (Exam e : pExams) {
                e.setReleased(true);
                examDao.update(e);
            }
        }
    }

    public String closeExam(final Exam pExam) {
        assertNotNull(pExam).setReleased(false);
        for (User u : pExam.getStudents()) {
            deregisterAsStudent(u, pExam);
            SystemMailBean.reportExamCancel(u, pExam);
        }
        Exam theExam = exam;
        exam = pExam;
        update();
        exam = theExam;
        return "exams.xhtml";
    }

    /**
     * Setzt eine oder mehrere Exams auf den Status geschlossen für die Prüflinge.
     *
     * @param pExams
     *            mit allen zu ändernden Exams.
     */
    public void closeExams(List<Exam> pExams) {
        if (getSession().getUser().getRole() == Role.EXAMINER) {
            assertNotNull(pExams);
            for (Exam e : pExams) {
                e.setReleased(false);
                examDao.update(e);
            }
        }
    }

    /**
     * Gibt alle Prüfer der gegebenen Prüfung formatiert als String zurück.
     *
     * @param pExam
     *            Die gewählte Prüfung.
     * @return Alle Prüfer der gegebenen Prüfung formatiert als String.
     */
    public List<String> examinersToStringList(final Exam pExam) {
        return assertNotNull(pExam).getExaminers().stream()
                .map(e -> e.getGivenName() + " " + e.getSurname())
                .collect(Collectors.toList());
    }

    /**
     * Gibt alle Prüfer der ILV der gewählten Prüfung zurück ohne den angemeldeten
     * Benutzer, da dieser immer automatisch Prüfer wird.
     *
     * @return Alle Prüfer der ILV der gewählten Prüfung zurück ohne den angemeldeten
     *         Benutzer.
     */
    public List<User> getPossibleExaminersWithoutUser() {
        return exam.getInstanceLecture().getExaminers().stream()
                .filter(u -> !u.equals(getSession().getUser()))
                .collect(Collectors.toList());
    }

    public boolean getAlreadyExists() {
        return alreadyExists;
    }

    public void setAlreadyExists() {
        alreadyExists = !alreadyExists;
    }

    /**
     * Liefert eine einfache Map mit den verfügbaren Exam Typen im System zurück. Diese
     * werden als Auswahl im Drop-Down Menu bei der erstellung einer Prüfung verfügbar
     * sein.
     *
     * @return Eine einfache Map mit verfügbaren Rollen.
     */
    private List<String> calculateExamTypeList() {
        final List<String> tmp = new ArrayList<String>();
        tmp.add("Mündliche Prüfung");
        tmp.add("Fachgespräch");
        return Collections.unmodifiableList(tmp);
    }

    /**
     * Gibt alle Prüfungen eines Studenten zurück, in denen er Prüfer ist.
     *
     * @return Alle Prüfungen eines Studenten, in denen er Prüfer ist.
     */
    public List<Exam> getExamsAsStudentExaminer() {
        return getAllExams()
                .stream()
                .filter(e -> e.getExaminers().stream().map(User::getId)
                        .collect(Collectors.toList())
                        .contains(getSession().getUser().getId()))
                .collect(Collectors.toList());
    }

    /**
     * Speichert den Wert der Exam temporär.
     * 
     * @param pExam
     *            , wird zwischengespeichert
     * @return Die Notenverwaltung
     */
    public String setSelectedExam(final Exam pExam) {
        assertNotNull(pExam);
        selectedExam = pExam;
        return "grades.xhtml";
    }

    /**
     * Gibt die Anmeldeart des Studenten zurück.
     * 
     * @param pUser
     *            , für den die Anmledeart ausgegeben werden soll für diese ILV
     * @return Gibt die Anmeldeart zurück
     */
    public JoinExam getWayOfRegister(final User pUser,
            final InstanceLecture pInstanceLecture) {
        JoinExam joinE = null;
        List<JoinExam> joinExam = joinExamDao.getJoinExamsForUser(pUser);
        if (joinExam == null) {
            throw new IllegalArgumentException(
                    "Ein Student wurde fehlerhaft hinzugefügt.");
        }
        for (JoinExam j : joinExam) {
            if (j.getInstanceLecture().getId() == pInstanceLecture.getId()) {
                joinE = j;
            }
        }
        return joinE;
    }

    public JoinExam getJoinExamForUserOfExam(final Exam pExam) {
        return assertNotNull(pExam)
                .getParticipants()
                .stream()
                .filter(j -> j.getPruefling().getId()
                        .equals(getSession().getUser().getId()))
                .collect(Collectors.toList()).get(0);
    }

    public String setExamGroupSize(final int pGroupSize) {
        if (pGroupSize < 1) {
            return null;
        } else {
            selectedExam.setGroupSize(pGroupSize);
            examDao.update(selectedExam);
        }
        return null;
    }

}
