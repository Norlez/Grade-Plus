package controller;

import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.InstanceLecture;
import common.model.Lecture;
import common.model.Session;
import common.model.User;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import persistence.InstanceLectureDAO;
import persistence.UserDAO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static common.util.Assertion.assertNotNull;

/**
 * Diese Bean ist für das Bearbeiten bestimmter {@link InstanceLecture}-Objekte
 * verantwortlich.
 *
 * @author Torben Groß
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
     * Die aktuell zu bearbeitende ILV.
     */
    private InstanceLecture instanceLecture;

    /**
     * Prüft, ob die Attribute in dieser Klasse verändert werden dürfen.
     */
    private boolean editChecker = false;

    /**
     * Erzeugt eine neue InstanceLectureEditBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden InstanceLectureEditBean.
     * @param pInstanceLectureDao
     *            Die InstanceLectureDAO der zu erzeugenden InstanceLectureEditBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} oder {@code pInstanceLectureDao} {@code null}
     *             sind.
     */
    @Inject
    public InstanceLectureEditBean(final Session pSession,
            final InstanceLectureDAO pInstanceLectureDao, final UserDAO pUserDao) {
        super(pSession);
        instanceLectureDao = assertNotNull(pInstanceLectureDao);
        userDao = assertNotNull(pUserDao);
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
        return "semesterdetails.xhtml";
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
     * @param pUser
     */
    public void update(User pUser)
    {
        try{
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

    public String addExaminer(final User pExaminer) {
        instanceLecture.addExaminer(assertNotNull(pExaminer,
                "InstanceLectureEditBean: addExaminer(User)"));
        pExaminer.addAsProfToIlv(instanceLecture);
        update(pExaminer);
        return "exams.xhtml";
    }

    public String removeExaminer(final User pExaminer) {
        instanceLecture.removeExaminer(assertNotNull(pExaminer,
                "InstanceLectureEditBean: removeExaminer(User)"));
        pExaminer.removeProfFromIlv(instanceLecture);
        update(pExaminer);
        return "exams.xhtml";
    }

    public String addStudent(final User pStudent) {
        instanceLecture.addExaminee(assertNotNull(pStudent,
                "InstanceLectureEditBean: addStudent(User)"));
        pStudent.addAsStudentToIlv(instanceLecture);
        update(pStudent);
        return "exams.xhtml";
    }

    public String removeStudent(final User pStudent) {
        instanceLecture.removeExaminee(assertNotNull(pStudent,
                "InstanceLectureEditBean: removeStudent(User)"));
        pStudent.removeStudentFromIlv(instanceLecture);
        update(pStudent);
        return "exams.xhtml";
    }

    public List<User> getUnregisteredStudents() {
        return userDao.getAllStudents().stream()
                .filter(s -> !instanceLecture.getExaminees().contains(s))
                .collect(Collectors.toList());
    }

    public List<InstanceLecture> getAllInstances(Lecture pLecture)
    {
        return instanceLectureDao.getInstanceLecturesForLecture(pLecture);
    }
}
