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
package businesslogic;

import common.model.Role;
import common.model.SemesterTime;

import static common.util.Assertion.assertNotNegative;
import static common.util.Assertion.assertWithoutNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

/**
 * Diese Klasse stellt einige grundlegende mathematische Funktionen bereit.
 *
 * @author Marcel Steinbeck, Karsten Hölscher
 * @version 2016-05-27
 */
public final class Math {

    /**
     * Privater Konstruktor, der verhindert, dass eine Instanz dieser Utility-Klasse
     * erzeugt werden kann.
     */
    private Math() {
    }

    /**
     * Berechnet den Durchschnitt der übergebenen Liste und gibt das Ergebnis mit der
     * gegebenen Anzahl an Nachkommastellen und dem gegebenen Rundungsverfahren zurück.
     *
     * @param theDecimals
     *            Die Liste der Dezimalzahlen.
     * @param scale
     *            Die Anzahl der Nachkommastellen für das Ergebnis.
     * @param roundingMode
     *            Die Rundungsart für das Ergebnis.
     * @return Der Durchschnitt der übergebenen Liste.
     * @throws IllegalArgumentException
     *             Falls {@code theDecimals == null} oder ein Element e in
     *             {@code theDecimals} mit {@code e == null} existiert oder {@code scale}
     *             negativ ist oder {@code roundingMode == null}.
     * @throws ArithmeticException
     *             Falls die gegebene Liste leer ist.
     */
    public static BigDecimal average(final List<BigDecimal> theDecimals, final int scale,
            final RoundingMode roundingMode) {
        assertWithoutNull(theDecimals);
        assertNotNegative(scale);
        assertWithoutNull(roundingMode);

        if (theDecimals.isEmpty()) {
            throw new ArithmeticException(
                    "List of values is empty, thus an average value cannot be calculated.");
        }

        final BigDecimal size = BigDecimal.valueOf(theDecimals.size());
        BigDecimal avg = new BigDecimal("0", MathContext.UNLIMITED);
        for (final BigDecimal decimal : theDecimals) {
            avg = avg.add(decimal);
        }
        return avg.divide(size, scale, roundingMode);
    }

    /**
     * Berechnet den Median der übergebenen Liste. Ist die Anzahl der Werte gerade, so
     * wird stets der Obermedian bestimmt. Als Beispiel: Der Median von [5, 2, 9, 7] ist
     * 7.
     *
     * @param theDecimals
     *            Die Liste der Dezimalzahlen.
     * @return Der Median der übergebenen Liste.
     * @throws IllegalArgumentException
     *             Falls {@code theDecimals == null} oder ein Element e in
     *             {@code theDecimals} mit {@code e == null} existiert.
     * @throws ArithmeticException
     *             Falls die gegebene Liste leer ist.
     */
    public static BigDecimal median(final List<BigDecimal> theDecimals) {
        assertWithoutNull(theDecimals);

        if (theDecimals.isEmpty()) {
            throw new ArithmeticException(
                    "List of values is empty, thus the median cannot be determined.");
        }

        final List<BigDecimal> sorted = new ArrayList<>(theDecimals);
        Collections.sort(sorted);
        return sorted.get(sorted.size() / 2);
    }

    /**
     * Berechnet die Standartabweichung der übergebenen Liste.
     *
     * @param pDecimals
     *
     * @throws IllegalArgumentException
     *             Falls {@code theDecimals == null} oder ein Element e in
     *             {@code theDecimals} mit {@code e == null} existiert.
     *
     * @throws ArithmeticException
     *             Falls die gegebene Liste leer ist.
     */
    public static BigDecimal standartDeviation(final List<BigDecimal> pDecimals) {
        throw new UnsupportedOperationException();
    }



    /**
     * Liefert eine einfache Map mit den verfügbaren Jahren im System zurück. Diese werden
     * als Auswahl im Drop-Down Menu bei der erstellung einer ILV verfügbar sein.
     *
     * @return Eine einfache Map mit verfügbaren Rollen.
     */
    private List<String> calculateYearList() {
        final List<String> tmp = new ArrayList<String>();
        for (int i = 2018; i < 2080; i++) {
            tmp.add(Integer.toString(i));
        }
        return Collections.unmodifiableList(tmp);
    }




    /**
     * Liefert eine einfache Map mit den verfügbaren Semestern im System zurück.
     * @return Eine einfache Map mit verfügbaren Semestern.
     */
    private Map<String, SemesterTime> calculateSemesterMap() {
        final Map<String, SemesterTime> tmp = new LinkedHashMap<>();
        tmp.put("WiSe", SemesterTime.WINTER);
        tmp.put("SoSe", SemesterTime.SOMMER);
        return Collections.unmodifiableMap(tmp);
    }

    /**
     * Liefert eine einfache Map mit allen bekannten Rollen innerhalb der Applikation zurück.
     */
    public static Map<String, Role> calculateRoleMap() {
        final Map<String, Role> tmp = new LinkedHashMap<>();
        tmp.put("ADMIN", Role.ADMIN);
        tmp.put("EXAMINER", Role.EXAMINER);
        tmp.put("STUDENT", Role.STUDENT);
        return Collections.unmodifiableMap(tmp);
    }
}
