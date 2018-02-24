package controller;

import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import persistence.ExamDAO;
import persistence.InstanceLectureDAO;
import persistence.UserDAO;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@ViewScoped
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
     * Eine Liste mit allen bekannten Prüfungsterminen.
     */
    private List<Exam> allExams;

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
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für
     * {@link InstanceLecture}-Objekte übernimmt.
     */
    private final InstanceLectureDAO instanceLectureDao;

    /**
     * Erzeugt eine neue ExamsBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden ExamsBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    @Inject
    public ExamsBean(final Session pSession, final ExamDAO pExamDao,
            final UserDAO pUserDao, final InstanceLectureDAO pInstanceLectureDao) {
        super(pSession);
        examDao = assertNotNull(pExamDao);
        userDao = assertNotNull(pUserDao);
        instanceLectureDao = assertNotNull(pInstanceLectureDao);

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
        allExams = assertNotNull(examDao.getAllExams());
        examsOfStudent = assertNotNull(examDao
                .getExamsForExaminee(getSession().getUser()));
        examsOfExaminer = assertNotNull(examDao.getExamsForExaminee(getSession()
                .getUser()));
        startOfTimeSlot = null;
        endOfTimeSlot = null;
        lengthOfBreaks = null;
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
        exam = assertNotNull(pExam);
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
     * Fügt die aktuell angezeigte Prüfung der Liste aller innerhalb der Applikation
     * bekannten Prüfungen hinzu.
     *
     * @return "lectureinstance.xhtml", um auf das Facelet der Übersicht der Prüfungen zu
     *         leiten.
     */
    public String save() {
        User user = getSession().getUser();
        try {
            exam.addExaminer(user);
            examDao.save(exam);
            exam.getIlv().addExam(exam);
            instanceLectureDao.update(exam.getIlv());
            user.addExamAsProf(exam);
            userDao.update(user);
            // TODO Examiner als JoinExam hinzufügen?
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // User und Exam haben JoinExam
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorInputdataIncomplete"));
        } catch (final UnexpectedUniqueViolationException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG, getTranslation("error"));
        } catch (final DuplicateUsernameException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorUsernameAlreadyInUse", user.getUsername());
        } catch (final DuplicateEmailException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorEmailAlreadyInUse", user.getEmail());
        }
        init();
        return "lectureinstance.xhtml";
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
            List<JoinExam> joinExams = oldExam.getParticipants();
            List<User> students = new ArrayList<>();
            for (JoinExam joinExam : joinExams) {
                // students.addAll(joinExam.getStudents()); TODO getStudents() gibts noch
                // nicht!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            }

            examDao.update(exam);
            String message = String
                    .format("Die Daten des Prüfungstermins für %s am %s um %s Uhr, wurden angepasst.\n\nNeue Daten:\nDatum: %s\nUhrzeit: %s Uhr\nDauer: %i Minuten",
                            oldExam.getIlv().getLecture().getName(), oldExam.getDate()
                                    .toString(), oldExam.getTime().toString(), exam
                                    .getDate().toString(), exam.getTime().toString(),
                            exam.getExamLength());
            for (int i = 0; i < students.size(); i++) {
                notify(students.get(i), message);
            }
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorInputdataIncomplete"));
        } catch (final UnexpectedUniqueViolationException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG, getTranslation("error"));
        }
        init();
        return "exams.xhtml";
    }

    /**
     * Entfernt die übergebene Prüfung aus der Liste aller bekannten Prüfungen unter
     * Verwendung des entsprechenden Data-Access-Objekts. Sollte die zu entfernende
     * Prüfung nicht in der Liste der Prüfungen vorhanden sein, passiert nichts.
     *
     * @param pExam
     *            Die zu entfernende Prüfung.
     * @return {code null} damit nicht zu einem anderen Facelet navigiert wird.
     */
    public String remove(final Exam pExam) {
        List<JoinExam> joinExams = assertNotNull(pExam).getParticipants();
        List<User> students = new ArrayList<>();
        for (JoinExam joinExam : joinExams) {
            // students.addAll(joinExam.getStudents()); TODO getStudents() gibts noch
            // nicht!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }

        examDao.remove(pExam);
        String message = String
                .format("Der Prüfungstermin für %s am %s um %s Uhr wurde gelöscht. Bitte melden Sie sich bei Bedarf zu einem neuen Termin an.",
                        pExam.getIlv().getLecture().getName(),
                        pExam.getDate().toString(), pExam.getTime().toString());
        for (int i = 0; i < students.size(); i++) {
            notify(students.get(i), message);
        }
        init();
        return null;
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
        exam.setDate(startOfTimeSlot.toLocalDate());
        exam.setTime(startOfTimeSlot.toLocalTime());
        exam.addExaminer(getSession().getUser());
        while (exam.getLocalDateTime().plusMinutes(exam.getExamLength())
                .compareTo(endOfTimeSlot) <= 0) {
            if (!isTimeSlotEmpty(exam.getLocalDateTime(), exam.getLocalDateTime()
                    .plusMinutes(exam.getExamLength()))) {
                conflictingExams.add(exam);
                continue;
            }
            save();
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
        sender.getMail().setContent(assertNotNull(pMessage));
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

            LocalDateTime currLowerBound = LocalDateTime.of(currExam.getDate(),
                    currExam.getTime());
            LocalDateTime currUpperBound = currLowerBound.plusMinutes(currExam
                    .getExamLength());

            if ((lowerBound.isBefore(currLowerBound) && upperBound
                    .isAfter(currLowerBound))
                    || (currLowerBound.isBefore(lowerBound) && currUpperBound
                            .isAfter(lowerBound)) || lowerBound.equals(currLowerBound)) {
                return true;
            }
        }
        return false;
    }

}
