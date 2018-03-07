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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import businesslogic.Math;
import common.exception.DuplicateUniqueFieldException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.Session;
import persistence.UserDAO;
import common.util.Assertion;
import org.apache.log4j.Logger;

import common.model.Configuration;
import common.model.Grade;
import common.model.User;
import persistence.GradeDAO;

/**
 * Dieses Bean ist für die Notenverwaltung zuständig. Backing Bean (und damit Controller
 * im MVC-Muster) für das Facelet {@code grades.xhtml}.
 *
 * Da die Webseite dynamisch ist in dem Sinne, dass während ihrer Anzeige mehrere Requests
 * möglich sind (Löschen einer Note aus der angezeigten Tabelle) bekommt sie
 * {@code ViewScoped}.
 *
 * @author Dennis Schürholz, Karsten Hölscher, Sebastian Offermann, Marcel Steinbeck
 * @version 2017-06-28
 */
@Named
@ViewScoped
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
     * Die aktuell angezeigte Note.
     */
    private Grade grade;

    /**
     * Eine Liste mit allen aktuell angezeigten Noten.
     */
    private List<Grade> allGrades;


    public List<Double> getPossibleGrades() {
        return possibleGrades;
    }

    public void setPossibleGrades(List<Double> possibleGrades) {
        this.possibleGrades = possibleGrades;
    }

    /**
     * Die Liste aller innherhalb der Applikation möglichen Notenvergaben.
     */
    private List<Double> possibleGrades;


    public Double getSelectedGrade() {
        return selectedGrade;
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
     * @throws IllegalArgumentException
     *             Falls {@code pSession}, {@code pGradeDAO} oder {@code pUserDAO}
     *             {@code null} ist.
     */
    @Inject
    public GradesBean(final Session pSession, final GradeDAO pGradeDAO,
                      final UserDAO pUserDAO) {
        super(pSession);
        gradeDAO = Assertion.assertNotNull(pGradeDAO);
        userDAO = Assertion.assertNotNull(pUserDAO);
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
    public String getGradeAverage() {
        final List<BigDecimal> theDecimals = new ArrayList<>(allGrades.size());
        for (final Grade g : allGrades) {
            theDecimals.add(g.getMark());
        }
        final Configuration config = Configuration.getDefault();
        final int scale = config.getScale();
        final RoundingMode roundingMode = config.getRoundingMode();
        try {
            final BigDecimal average = Math.average(theDecimals, scale, roundingMode);
            return average.stripTrailingZeros().toPlainString();
        } catch (final ArithmeticException e) {
            logger.debug(
                    "Calculation of grade average has been called without any grades.", e);
            return NO_GRADES_PRESENT;
        }
    }

    /**
     * Berechnet den Median aller Noten des aktuell in der zugehörigen Session
     * eingeloggten Benutzers und gibt diesen Wert zurück.
     *
     * @return Den Median aller Noten des aktuell in der zugehörigen Session eingeloggten
     *         Benutzers.
     */
    public String getGradeMedian() {
        final List<BigDecimal> theDecimals = new ArrayList<>(allGrades.size());
        for (final Grade g : allGrades) {
            theDecimals.add(g.getMark());
        }
        try {
            final BigDecimal median = Math.median(theDecimals);
            return median.stripTrailingZeros().toPlainString();
        } catch (final ArithmeticException e) {
            logger.debug(
                    "Calculation of grades median has been called without any grades.", e);
            return NO_GRADES_PRESENT;
        }
    }

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
    public String save() {
        /*
         * Übung: Noten können aktuell beliebige Zahlen sein - ändert das und fangt
         * unsinnige Werte ab
         */
        if (!isLoggedIn()) {
            return null;
        }
        final User user = getSession().getUser();
        grade.setUser(user);

        BigDecimal theMark = grade.getMark();
        BigDecimal lowestMark = BigDecimal.valueOf(1);
        BigDecimal highestMark = BigDecimal.valueOf(4);

        if (lowestMark.compareTo(theMark) > 0 || highestMark.compareTo(theMark) < 0) {
            return null;
        } else {
            BigDecimal tmp = theMark.remainder(new BigDecimal(1));
            BigDecimal n3 = BigDecimal.valueOf(.3);
            BigDecimal n7 = BigDecimal.valueOf(.7);
            BigDecimal n0 = BigDecimal.valueOf(0);
            if ((tmp.compareTo(n3) == 0) || (tmp.compareTo(n7) == 0)
                    || (tmp.compareTo(n0) == 0)) {
                user.addGrade(grade);
                gradeDAO.save(grade);
                logger.info("hats geklappt ?");

            } else {
                return null;
            }
            try {
                userDAO.update(user);
            } catch (final DuplicateUniqueFieldException e) {
                throw new UnexpectedUniqueViolationException(e);
            }
            init();
            return null;
        }
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
}

