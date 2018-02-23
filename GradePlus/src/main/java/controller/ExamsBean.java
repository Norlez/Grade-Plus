package controller;

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
 * Diese Bean ist dafür verantwortlich, die die vom Prüfer zu gebenden Prüfungen
 * anzuzeigen. Dort kann der Prüfer Termine verschieben oder Absagen.
 *
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-21
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
     * Der aktuell ausgewählte Prüfungstermin.
     */
    private Exam exam;

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
        try {
            examDao.save(exam);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorExamdataIncomplete"));
        } catch (final UnexpectedUniqueViolationException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG, getTranslation("error"));
        }
        init();
        return "lectureinstance.xhtml";
    }

    /**
     * Aktualisiert die aktuell angezeigte Prüfung in der Liste aller innerhalb der
     * Applikation bekannten Prüfungen.
     *
     * @return "lectureinstance.xhtml", um auf das Facelet der Übersicht der Prüfungen zu
     *         leiten.
     */
    public String update() {
        try {
            Exam oldExam = examDao.getById(exam.getId());
            List<JoinExam> students = oldExam.getParticipants(); // TODO Participants ist
                                                                 // eine JoinExam, darin
                                                                 // sind die Prüflinge
            examDao.update(exam);
            String message = String
                    .format("Die Daten des Prüfungstermins für %s am %s um %s Uhr, wurden angepasst.\n\nNeue Daten:\nDatum: %s\nUhrzeit: %s Uhr\nDauer: %i Minuten",
                            oldExam.getIlv().getLecture().getName(), oldExam.getDate()
                                    .toString(), oldExam.getTime().toString(), exam
                                    .getDate().toString(), exam.getTime().toString(),
                            exam.getExamLength());
            for (int i = 0; i < students.size(); i++) {
                // notify(students.get(i), message); TODO
                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            }
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorExamdataIncomplete"));
        } catch (final UnexpectedUniqueViolationException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG, getTranslation("error"));
        }
        init();
        return "lectureinstance.xhtml";
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
        List<JoinExam> students = assertNotNull(pExam).getParticipants(); // TODO
                                                                          // Participants
                                                                          // ist eine
                                                                          // JoinExam,
                                                                          // darin sind
                                                                          // die Prüflinge
        examDao.remove(pExam);
        String message = String
                .format("Der Prüfungstermin für %s am %s um %s Uhr wurde gelöscht. Bitte melden Sie sich bei Bedarf zu einem neuen Termin an.",
                        pExam.getIlv().getLecture().getName(),
                        pExam.getDate().toString(), pExam.getTime().toString());
        for (int i = 0; i < students.size(); i++) {
            // notify(students.get(i), message); TODO
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
        init();
        return null;
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
        sender.setTopic("Änderungen an einem Prüfungstermin");
        sender.setContent(assertNotNull(pMessage));
        sender.setSenderEmail("gradeplusbremen@gmail.com");
        // sender.setSenderPassword("Koschke123"); TODO
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        sender.setRecipient(pUser.getEmail());
        sender.sendMailTo();
    }

    /**
     * Erzeugt {@link Exam}-Objekte mit gegebenen Stammdaten innerhalb des angegebenen
     * Zeitraums mit entsprechenden Pausen zwischen Prüfungen.
     *
     * @param instanceLecture
     *            Die ILV, der die Prüfung angehören.
     * @param lowerBound
     *            Der frühste Zeitpunkt einer Prüfung.
     * @param upperBound
     *            Der späteste Zeitpunkt, zu der eine Prüfung noch laufen kann. Keine
     *            Prüfung darf nach diesem Zeitpunkt beendet werden.
     * @param lengthOfExams
     *            Die Länge einer Prüfung.
     * @param lengthOfBreaksBetweenExams
     *            Die Länge der Pausen zwischen Prüfungen.
     * @param location
     *            Der Ort der Prüfungen.
     * @param type
     *            Die Art der Prüfungen (z.B. Fachgespräche)
     * @param isGroupExam
     *            Gibt an, ob es sich um eine Gruppenprüfung handelt.
     * @param examRegulations
     *            Die Prüfungsordnung der Prüfung.
     *
     * @return Alle Prüfungen als Liste, die aufgrund von Terminkonflikten nicht
     *         hinzugefügt werden konnten.
     */
    public List<Exam> createExamsForTimePeriod(final InstanceLecture instanceLecture,
            final LocalDateTime lowerBound, final LocalDateTime upperBound,
            final int lengthOfExams, final int lengthOfBreaksBetweenExams,
            final String location, final String type, final boolean isGroupExam,
            final String examRegulations) {
        List<Exam> conflictingExams = new ArrayList<>();
        LocalDateTime startOfExam = lowerBound;
        LocalDateTime endOfExam = startOfExam.plusMinutes(lengthOfExams);

        while (endOfExam.compareTo(upperBound) <= 0) {

            init();

            exam.setDate(startOfExam.toLocalDate());
            // exam.addExaminer(getSession().getUser()); TODO
            // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            exam.setExamLength(lengthOfExams);
            exam.setExamRegulations(examRegulations);
            exam.setGroupExam(isGroupExam);
            exam.setIlv(instanceLecture);
            exam.setLocation(location);
            exam.setTime(startOfExam.toLocalTime());
            exam.setType(type);

            if (!isTimeSlotEmpty(startOfExam, endOfExam)) {
                conflictingExams.add(exam);
                continue;
            }

            save();
            instanceLecture.addExam(exam);
            instanceLectureDao.update(instanceLecture);

            startOfExam = startOfExam.plusMinutes(lengthOfExams
                    + lengthOfBreaksBetweenExams);
            endOfExam = startOfExam.plusMinutes(lengthOfExams);
        }

        init();
        return conflictingExams;
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
