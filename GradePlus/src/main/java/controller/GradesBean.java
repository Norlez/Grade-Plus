/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 AG Softwaretechnik, University of Bremen:
 * Karsten Hölscher, Sebastian Offermann, Dennis Schürholz, Marcel Steinbeck
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package controller;

import static common.util.Assertion.assertNotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import businesslogic.Math;
import common.exception.DuplicateEmailException;
import common.exception.DuplicateUniqueFieldException;
import common.exception.DuplicateUsernameException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.*;
import org.apache.log4j.Level;
import persistence.ExamDAO;
import persistence.JoinExamDAO;
import persistence.UserDAO;
import common.util.Assertion;
import org.apache.log4j.Logger;

import persistence.GradeDAO;

/**
 * Dieses Bean ist für die Notenverwaltung zuständig. Backing Bean (und damit Controller
 * im MVC-Muster) für das Facelet {@code grades.xhtml}.
 *
 * Da die Webseite dynamisch ist in dem Sinne, dass während ihrer Anzeige mehrere Requests
 * möglich sind (Löschen einer Note aus der angezeigten Tabelle) bekommt sie
 * {@code ViewScoped}.
 *
 * @author Dennis Schürholz, Karsten Hölscher, Sebastian Offermann, Marcel Steinbeck, Marvin Kampen, Torben Groß, Tugce Karakus
 * @version 2018-03-11
 */
@Named
@SessionScoped
public class GradesBean extends AbstractBean implements Serializable {

    /**
     * Die eindeutige SerialisierungsID.
     */
    private static final long serialVersionUID = 320401008216711886L;

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(GradesBean.class);

    /**
     * Fehlermeldung für den Fall, dass der Durchschnitt der Noten angefordert wird,
     * obwohl keine Noten vorhanden sind.
     */
    private static final String NO_GRADES_PRESENT = "keine Noten eingetragen";

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Noten-Objekte
     * übernimmt.
     */
    private transient final GradeDAO gradeDAO;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Benutzer-Objekte
     * übernimmt.
     */
    private final UserDAO userDAO;

    /**
     * Speichert eine Referenz auf eine Exam.
     */
    private Exam exam;

    /**
     * Die Joinexam, die Konfiguriert werden soll.
     */
    private JoinExam toConfigure;

    /**
     * Die aktuell angezeigte Note.
     */
    private Grade grade;

    /**
     * Eine Liste mit allen aktuell angezeigten Noten.
     */
    private List<Grade> allGrades;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Join-Exams-Objekte
     * übernimmt.
     */
    private JoinExamDAO joinExamDAO;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Exams-Objekte
     * übernimmt.
     */
    private ExamDAO examDAO;

    /**
     * Gibt alle möglichen Noten zurück
     * @return Liste mit allen möglichen Noten
     */
    public List<Double> getPossibleGrades() {
        return possibleGrades;
    }

    /**
     *
     * @param possibleGrades
     */
    public void setPossibleGrades(List<Double> possibleGrades) {
        this.possibleGrades = possibleGrades;
    }

    /**
     * Die Liste aller innherhalb der Applikation möglichen Notenvergaben.
     */
    private List<Double> possibleGrades;

    public Double getSelectedGrade() {
        List<JoinExam> daGrades = joinExamDAO
                .getAllJoinExams()
                .stream()
                .filter(j -> j.getExam().getId().equals(exam.getId())
                        && j.getPruefling().getId().equals(user.getId())
                        && j.getGrade() != null).collect(Collectors.toList());
        return daGrades.isEmpty() ? 1.0 : daGrades.get(0).getGrade().getMark();
    }

    public void setSelectedGrade(Double selectedGrade) {
        this.selectedGrade = selectedGrade;
    }

    /**
     * Die ausgewählte Note
     */
    private Double selectedGrade;

    /**
     * Erzeugt eine neue GradesBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden GradesBean.
     * @param pGradeDAO
     *            Die GradeDAO der zu erzeugenden GradesBean.
     * @param pUserDAO
     *            Die UserDAO der zu erzeugenden GradesBean.
     * @param pExamDAO
     *              Die ExamDAO der zu erzeugenden GradesBean.
     * @param pJoinExamDAO
     *              Die ExamDAO der zu erzeugenden GradesBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession}, {@code pGradeDAO} oder {@code pUserDAO}
     *             {@code null} ist.
     */
    @Inject
    public GradesBean(final Session pSession, final GradeDAO pGradeDAO,
            final UserDAO pUserDAO, final JoinExamDAO pJoinExamDAO, final ExamDAO pExamDAO) {
        super(pSession);
        gradeDAO = Assertion.assertNotNull(pGradeDAO);
        userDAO = Assertion.assertNotNull(pUserDAO);
        joinExamDAO = Assertion.assertNotNull(pJoinExamDAO);
        examDAO = Assertion.assertNotNull(pExamDAO);
    }

    /**
     * Initialisiert die Attribute {@link #grade} und {@link #allGrades}, sodass
     * {@link #grade} einen leeren Noteneintrag repräsentiert und {@link #allGrades} alle
     * Noten des aktuell eingeloggten Benutzers enthält.
     */
    @PostConstruct
    public void init() {
        if (!isLoggedIn()) {
            return;
        }
        grade = new Grade();
        allGrades = getSession().getUser().getGrades();
        possibleGrades = calculatePossibleGradesList();
        toConfigure = new JoinExam();
    }

    /**
     * Liefert eine einfache Map mit den verfügbaren Jahren im System zurück. Diese werden
     * als Auswahl im Drop-Down Menu bei der erstellung einer ILV verfügbar sein.
     *
     * @return Eine einfache Map mit verfügbaren Rollen.
     */
    private List<Double> calculatePossibleGradesList() {
        final List<Double> tmp = new ArrayList<Double>();
        tmp.add(1.0);
        tmp.add(1.3);
        tmp.add(1.7);
        tmp.add(2.0);
        tmp.add(2.3);
        tmp.add(2.7);
        tmp.add(3.0);
        tmp.add(3.3);
        tmp.add(3.7);
        tmp.add(4.0);
        tmp.add(5.0);
        return Collections.unmodifiableList(tmp);
    }

    /**
     * Gibt die anzuzeigende Note zurück.
     *
     * @return Die anzuzeigende Note.
     */
    public Grade getGrade() {
        return grade;
    }

    /**
     * Getter für toConfigure-Attribut
     * @return toConfigure
     */
    public JoinExam getToConfigure() {
        return toConfigure;
    }

    /**
     * Setter für toConfigure-Attribut
     * @param toConfigure
     */
    public void setToConfigure(JoinExam toConfigure) {
        this.toConfigure = toConfigure;
    }

    /**
     * Gibt die Liste aller anzuzeigenden Noten zurück. Diese Liste ist keine Kopie,
     * sondern Teil des internen Zustands der Bean (siehe README.md, Abschnitt 'Beans und
     * Ressourcenallokation').
     *
     * @return Die Liste aller anzuzeigenden Noten.
     */
    public List<Grade> getAllGrades() {
        return allGrades;
    }

    /**
     * Berechnet den Durchschnittswert aller Noten des aktuell in der zugehörigen Session
     * eingeloggten Benutzers und gibt diesen Wert als Zeichenkettenrepräsentation zurück.
     * Wenn der Durchschnitt nicht berechnet werden kann (weil es keine Noten gibt), wird
     * eine Fehlermeldung zurückgegeben.
     *
     * @return Den Durchschnittswert aller Noten des aktuell in der zugehörigen Session
     *         eingeloggten Benutzers als Zeichenkette.
     */
    // public String getGradeAverage() {
    // final List<BigDecimal> theDecimals = new ArrayList<>(allGrades.size());
    // for (final Grade g : allGrades) {
    // theDecimals.add(g.getMark());
    // }
    // final Configuration config = Configuration.getDefault();
    // final int scale = config.getScale();
    // final RoundingMode roundingMode = config.getRoundingMode();
    // try {
    // final BigDecimal average = Math.average(theDecimals, scale, roundingMode);
    // return average.stripTrailingZeros().toPlainString();
    // } catch (final ArithmeticException e) {
    // logger.debug(
    // "Calculation of grade average has been called without any grades.", e);
    // return NO_GRADES_PRESENT;
    // }
    // }

    /**
     * Berechnet den Median aller Noten des aktuell in der zugehörigen Session
     * eingeloggten Benutzers und gibt diesen Wert zurück.
     *
     * @return Den Median aller Noten des aktuell in der zugehörigen Session eingeloggten
     *         Benutzers.
     */
    // public String getGradeMedian() {
    // final List<Double> theDecimals = new ArrayList<>(allGrades.size());
    // for (final Grade g : allGrades) {
    // theDecimals.add(g.getMark());
    // }
    // try {
    // final Double median = Math.median(theDecimals);
    // return median.stripTrailingZeros().toPlainString();
    // } catch (final ArithmeticException e) {
    // logger.debug(
    // "Calculation of grades median has been called without any grades.", e);
    // return NO_GRADES_PRESENT;
    // }
    // }

    /**
     * Fügt die angezeigte Note der Liste aller Noten des aktuell in der zugehörigen
     * Session eingeloggten Benutzers hinzu (unter Verwendung des Data-Access-Objektes)
     * und setzt die angezeigte Note auf eine neue Note mit leeren Attributwerten. Die
     * Liste der angezeigten Noten wird aktualisiert. Wenn niemand eingeloggt ist, führt
     * dies zu einem entsprechenden Hinweis und die Seite wird neu geladen.
     *
     * @return {@code null}, damit nicht zu einem anderen Facelet navigiert wird
     * @throws UnexpectedUniqueViolationException
     *             Falls beim Aktualisieren des {@link User}-Objektes eine
     *             {@link DuplicateUniqueFieldException} ausgelöst wurde.
     */
    public String save(Exam pExam, User pStudent) throws DuplicateEmailException,
            DuplicateUsernameException {
        if (!isLoggedIn()) {
            return null;
        }
        JoinExam joinExam = null; //Geht durch alle JoinExams
        List<JoinExam> joinExams = joinExamDAO.getJoinExamsForUser(user);
        for (JoinExam j : joinExams) {
            if (j.getExam() != null && j.getExam().getId() == pExam.getId()) {
                joinExam = j;
                break;
            }
        }
        if (joinExam.getGrade() != null) {
            update(pExam, user);
            return "grades.xhtml";
        }
        grade.setMark(selectedGrade);
        grade.setJoinExam(joinExam);
        grade.setSubject(pExam.getInstanceLecture().getLecture().getName());
        grade.setUser(joinExam.getPruefling());
        joinExam.getPruefling().addGrade(grade);
        gradeDAO.save(grade);
        joinExamDAO.update(joinExam);
        joinExam.setGrade(grade);
        joinExamDAO.update(joinExam);
        userDAO.update(joinExam.getPruefling());
        examDAO.update(pExam);
        exam = examDAO.getById(pExam.getId());
        logger.info("hats geklappt ?");

        init();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        pStudent.setLoggingString(date.toString() + ": Für Ihre Prüfung in "
                + pExam.getInstanceLecture().getLecture().getName() + " vom "
                + pExam.getLocalDateTime().toString()
                + " hat Ihr Prüfer Ihnen eine Note zugeteilt.\n");
        try {
            userDAO.update(pStudent);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorUserdataIncomplete"));
        } catch (final DuplicateUsernameException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorUsernameAlreadyInUse", pStudent.getUsername());
        } catch (final DuplicateEmailException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorEmailAlreadyInUse", pStudent.getEmail());
        }
        setEditChecker(user);
        return "grades.xhtml";
    }

    /**
     * Updatet die Note mit der gegebenen Teilnote.
     *
     * @param pExam in der der Student teilgenommen hat
     * @param  pStudent der gesuchte User
     *
     *  @throws IllegalArgumentException, falsche Argumente
     *
     *  @throws DuplicateUsernameException, Username existiert bereits
     *
     *  @throws DuplicateEmailException, Email existiert bereits.
     *
     * @return null, um auf der Seite zu bleiben
     */
    public String update(Exam pExam, User pStudent) {
        if (!isLoggedIn()) {
            return null;
        }
        JoinExam joinExam = null;
        List<JoinExam> joinExams = joinExamDAO.getJoinExamsForUser(pStudent);
        for (JoinExam j : joinExams) {
            if (j.getExam() != null && j.getExam().getId() == pExam.getId()) {
                joinExam = j;
                break;
            }
        }
        joinExam.getGrade().setMark(selectedGrade);
        gradeDAO.update(joinExam.getGrade());
        logger.info("hats geklappt ?");

        init();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        java.util.Date date = new java.util.Date();
        pStudent.setLoggingString(date.toString() + ": Für Ihre Prüfung in "
                + pExam.getInstanceLecture().getLecture().getName() + " vom "
                + pExam.getLocalDateTime().toString()
                + " hat Ihr Prüfer ihre Note geändert.\n");
        try {
            userDAO.update(pStudent);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorUserdataIncomplete"));
        } catch (final DuplicateUsernameException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorUsernameAlreadyInUse", pStudent.getUsername());
        } catch (final DuplicateEmailException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorEmailAlreadyInUse", pStudent.getEmail());
        }
        setEditChecker(user);
        return null;
    }

    /**
     * Entfernt die übergebene Note aus der Liste aller Noten des aktuell in der
     * zugehörigen Session eingeloggten Benutzers unter Verwendung des entsprechenden
     * Data-Access-Objekts. Die Liste der angezeigten Noten wird aktualisiert. Sollte die
     * zu entfernende Note nicht in der Liste der Noten vorhanden sein, passiert nichts.
     * Wenn niemand eingeloggt ist, wird die Seite neu geladen und führt zu einem
     * entsprechenden Hinweis.
     *
     * @param pGrade
     *            Die zu entfernende Note.
     * @return {code null} damit nicht zu einem anderen Facelet navigiert wird.
     * @throws IllegalArgumentException
     *             falls die zu entfernende Note den Wert {@code null} hat.
     * @throws UnexpectedUniqueViolationException
     *             Falls beim Aktualisieren des {@link User}-Objektes eine
     *             {@link DuplicateUniqueFieldException} ausgelöst wurde.
     */
    public String remove(final Grade pGrade) {
        if (!isLoggedIn()) {
            return null;
        }
        Assertion.assertNotNull(pGrade);
        final User user = getSession().getUser();
        user.removeGrade(pGrade);
        gradeDAO.remove(pGrade);
        try {
            userDAO.update(user);
        } catch (final DuplicateUniqueFieldException e) {
            throw new UnexpectedUniqueViolationException(e);
        }
        init();
        return null;
    }

    /**
     * Exportiert Liste mit Noten.
     */
    public void exportGrades() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den Dateipfad.
     *
     * @param pDirPath
     *            Der angegebene Dateipfad.
     */
    public void setDirPath(String pDirPath) {
        throw new UnsupportedOperationException();
    }

    /**
     * Importiert Liste mit Noten.
     */
    public void importGrades() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die finale Note nach externer Berechnung;
     */
    public void setFinalGrade() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle Exams für einen User zurück. TODO: Ungetestet
     *
     * @param pUser
     * @return Liste von Exams.
     */
    public List<Exam> getAllExamsOfUser(final User pUser) {
        assertNotNull(pUser);
        List<Exam> e = new ArrayList<Exam>();
        List<JoinExam> joinExams = joinExamDAO.getJoinExamsForUser(pUser);
        if (joinExams == null) {
            return null;
        }
        for (JoinExam j : joinExams) {
            if (j.getExam() != null) {
                e.add(j.getExam());
            }
        }
        return e;
    }

    /**
     * Getter für Exam-Attribut
     * @return exam
     */
    public Exam getExam() {
        return exam;
    }

    /**
     * Setter für Exam-Attribut
     * @param pExam
     * @return grades.xhtml als Weiterleitung
     */
    public String setExam(final Exam pExam) {
        exam = assertNotNull(pExam);
        editChecker = false;
        user = null;
        return "grades.xhtml";
    }

    /**
     * Prüft, ob der User eine Note hat.
     * @param pStudent
     * @return grades.xhtml und setcommentaty.xhtml als Weiterleitung
     */
    public boolean hasGrade(final User pStudent) {
        return exam
                .getParticipants()
                .stream()
                .filter(j -> j
                        .getPruefling()
                        .getId()
                        .equals(assertNotNull(pStudent, "GradesBean: hasGrade(User)")
                                .getId())).collect(Collectors.toList()).get(0).getGrade() != null;
    }

    /**
     * Speichert den Kommentar ab.
     * @param pJoinExam
     * @return grades.xhtml
     */
    public String enterComment(final JoinExam pJoinExam) {
        joinExamDAO.update(pJoinExam);
        return "grades.xhtml";
    }

    /**
     * Leitet auf die setCommentary Seite weiter, dabei wird toConfigure mit einem Wert belegt.
     * @param pJoinExam
     * @return setCommentary.xhtml
     */
    public String setComment(final JoinExam pJoinExam) {
        assertNotNull(pJoinExam);
        toConfigure = pJoinExam;
        return "setcommentary.xhtml";
    }

    /**
     * Gibt alle JoinExams für einen User zurück.
     * @param e, die Exam an die der User teilnimmt.
     * @param u, der gesuchte User
     * @return Die JoinExam für den User.
     */
    public JoinExam getJoinExamForUser(Exam e, User u) {
        JoinExam tmp = null;
        List<JoinExam> j = joinExamDAO.getJoinExamsForUser(u);
        for (JoinExam join : j) {
            if (join.getExam() != null && join.getExam().getId() == exam.getId()) {
                tmp = join;
                break;
            }
        }
        return tmp;
    }

    /**
     * Gibt an, ob die Grade bearbeitet wurde. Wird mit false initialisiert.
     */
    private boolean editChecker = false;

    /**
     * Speichert einen User temporär.
     */
    private User user;

    /**
     * Gibt den Editchecker für den gegebenen User zurück
     * @param pUser, von dem wir den EditCheck Status haben wollen
     * @return Den EditChecker
     */
    public boolean getEditChecker(final User pUser) {
        return editChecker && user.getId().equals(assertNotNull(pUser).getId());
    }

    /**
     * Setzt den EditChecker auf den gegebenen User.
     * @param pUser
     */
    public void setEditChecker(final User pUser) {
        if (editChecker) {
            if (pUser.getId().equals(user.getId())) {
                user = null;
                editChecker = !editChecker;
            } else {
                user = pUser;
            }
        } else {
            user = assertNotNull(pUser);
            editChecker = !editChecker;
        }

    }

}
