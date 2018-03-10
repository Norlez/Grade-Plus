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

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
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

    private final ExamDAO examDAO;

    /**
     * Die aktuell zu bearbeitende ILV.
     */
    private InstanceLecture instanceLecture;

    /**
     * Prüft, ob die Attribute in dieser Klasse verändert werden dürfen.
     */
    private boolean editChecker = false;

    private JoinExamDAO joinExamDAO;




    /**
     * Diese Map wird benötigt, um festzustellen, welche Dokumente gedruckt werden sollen.
     */
    private Map<String, Boolean> checked = new HashMap<>();

    private Date startOfTimeFrame;
    private Date endOfTimeFrame;



    public Map<String, Boolean> getChecked() {
        return checked;
    }

    public void setChecked(final Map<String, Boolean> pChecked) {
        checked = assertNotNull(pChecked);
    }

    public Date getStartOfTimeFrame() {
        return startOfTimeFrame;
    }

    public void setStartOfTimeFrame(final Date startOfTimeFrame) {
        this.startOfTimeFrame = startOfTimeFrame;
    }

    public Date getEndOfTimeFrame() {
        return endOfTimeFrame;
    }

    public void setEndOfTimeFrame(final Date endOfTimeFrame) {
        this.endOfTimeFrame = endOfTimeFrame;
    }


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
    public InstanceLectureEditBean(final Session pSession, final ExamDAO pExamDao,
            final InstanceLectureDAO pInstanceLectureDao, final UserDAO pUserDao,
            final JoinExamDAO pJoinExamDAO) {
        super(pSession);
        instanceLectureDao = assertNotNull(pInstanceLectureDao);
        userDao = assertNotNull(pUserDao);
        joinExamDAO = assertNotNull(pJoinExamDAO);
        examDAO = assertNotNull(pExamDao);
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
        return "examregister.xhtml";
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
     * 
     * @param pUser
     */
    public void update(User pUser) {
        try {
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
        assertNotNull(pExaminer, "InstanceLectureEditBean: addExaminer(User)");
        if (!instanceLecture.getExaminees().contains(pExaminer)) {
            instanceLecture.addExaminer(pExaminer);
            pExaminer.addAsProfToIlv(instanceLecture);
            update(pExaminer);
        } else {
            addErrorMessage("errorUserIsStudent");
        }
        return "exams.xhtml";
    }

    public String removeExaminer(final User pExaminer) {
        assertNotNull(pExaminer, "InstanceLectureEditBean: removeExaminer(User)");
        for (Exam exam : examDAO.getAllExams()) {
            if (exam.getInstanceLecture().getId().equals(instanceLecture.getId())) {
                if (exam.getExaminers().contains(pExaminer)) {
                    addErrorMessage("errorUserIsExaminerOfExam");
                    return "exams.xhtml";
                }
            }
        }
        if (instanceLecture.getExaminers().size() <= 1) {
            addErrorMessage("errorLastExaminer");
            return "exams.xhtml";
        }
        instanceLecture.removeExaminer(pExaminer);
        pExaminer.removeProfFromIlv(instanceLecture);
        update(pExaminer);
        return "exams.xhtml";
    }

    public String addStudent(final User pStudent) {
        assertNotNull(pStudent, "InstanceLectureEditBean: addStudent(User)");
        if (!instanceLecture.getExaminers().contains(pStudent)) {
            instanceLecture.addExaminee(pStudent);
            pStudent.addAsStudentToIlv(instanceLecture);
            JoinExam joinExam = new JoinExam();
            joinExam.setPruefling(pStudent);
            joinExam.setKind(Anmeldeart.BYPROF);
            joinExam.setInstanceLecture(instanceLecture);
            joinExamDAO.save(joinExam);
            instanceLecture.addJoinExam(joinExam);
            instanceLectureDao.update(instanceLecture);
            update(pStudent);
        } else {
            addErrorMessage("errorUserIsExaminer");
        }
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

    public List<InstanceLecture> getAllInstances(Lecture pLecture) {
        return instanceLectureDao.getInstanceLecturesForLecture(pLecture);
    }

    public List<Exam> getExams(User pUser) {
        List<Exam> exam = new ArrayList<Exam>();
        List<JoinExam> tmp = joinExamDAO.getNonExmptyJoinExamsForUser(pUser);
        if (tmp != null) {
            for (JoinExam l : tmp) {
                if (l.getExam() != null) {
                    exam.add(l.getExam());
                }
            }
        }
        return exam;
    }

    public List<Exam> getReleasedExams() {
        List<Exam> tmp = examDAO.getExamsForInstanceLecture(instanceLecture);
        List<Exam> examList = new ArrayList<Exam>();
        if (!tmp.isEmpty())
            for (Exam e : tmp) {
                if (e.getReleased() == true) {
                    examList.add(e);
                }
            }
        return examList;
    }

    public boolean isStudentRegisteredForExam() {
        List<User> allRegisteredUsers = new ArrayList<>();
        instanceLecture.getExams().forEach(
                e -> allRegisteredUsers.addAll(e.getStudents()));
        return allRegisteredUsers.contains(getSession().getUser());
    }

    public List<Exam> getReleasedExamsForInstanceLecture() {
        return examDAO
                .getAllExams()
                .stream()
                .filter(e -> e.getReleased()
                        && e.getInstanceLecture().getId().equals(instanceLecture.getId()))
                .collect(Collectors.toList());
    }


    public List<String> getAvailableDocuments() {
        List<String> availableDocuments = new ArrayList<>();
        availableDocuments.add("Protokoll");
        availableDocuments.add("Quittung");
        availableDocuments.add("Zertifikat");
        return availableDocuments;
    }


    public String getDocumentsForTimeFrame(final InstanceLecture pInstanceLecture, Date pStart, Date pEnd,  Map<String, Boolean> pChecked) {

        //Date start = pStart;
        //Date end = pEnd;
        Map<String, Boolean> checked = pChecked;
        LocalDateTime start = LocalDateTime.ofInstant(pStart.toInstant(), ZoneId.systemDefault());
        LocalDateTime end = LocalDateTime.ofInstant(pEnd.toInstant(), ZoneId.systemDefault());

        assertNotNull(pInstanceLecture);
        List<String> selectedDocuments = checked.entrySet().stream()
                .filter(Map.Entry::getValue).map(Map.Entry::getKey)
                .collect(Collectors.toList());
        ArrayList<Exam> exams = new ArrayList<Exam>();

        List<Exam> pisse = examDAO.getExamsForInstanceLecture(pInstanceLecture);

        for(Exam e: pisse) {
            LocalDateTime ldtExam = e.getLocalDateTime();

            
            if (ldtExam.isAfter(start) && ldtExam.isBefore(end)) {
                exams.add(e);
            }
        }

        if (selectedDocuments.contains("Protokoll")) {
            // MACH SACHEN
        }
        if (selectedDocuments.contains("Quittung")) {
            // WAS MACHEN
        }
        if (selectedDocuments.contains("Zertifikat")) {
            // WAS MACHEN SACHEN
        }

        return "exams.xhtml";
    }

}
