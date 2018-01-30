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

import static businesslogic.Math.average;
import static businesslogic.Math.median;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import common.model.Configuration;
import common.model.Grade;
import common.model.Session;
import persistence.GradeDAO;
import persistence.UserDAO;
import common.util.Assertion;

/**
 * Komponententest für die Klasse GradeBean. Abhängigkeiten werden gemockt.
 *
 * Hier werden aktuell lediglich die Methoden zur Berechnung des Notendurchschnitts und
 * des Notenmedians getestet, da alle anderen Methoden keinerlei Funktion zusätzlich zu
 * verwendeten Funktionen enthalten, also einfach Werte weiterreichen bzw. zurückgeben.
 * Auch die beiden obigen Tests sind aktuell im Wesentlichen lediglich Tests einer Methode
 * aus der Standardbibliothek - insofern fragwürdig.
 * 
 * Die Klasse muss mit dem {@link PowerMockRunner} ausgeführt werden, da sie statische
 * Methoden mockt.
 *
 * @author Karsten Hölscher
 * @version 2017-07-03
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Configuration.class, businesslogic.Math.class, Assertion.class })
public class GradesBeanTest {

    @Mock
    private Configuration configuration;

    @Mock
    private Session session;

    @Mock
    private GradeDAO gradeDAO;

    @Mock
    private UserDAO userDAO;

    private GradesBean gradesBean;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(Configuration.class);
        when(Configuration.getDefault()).thenReturn(configuration);
        PowerMockito.mockStatic(Assertion.class);
        when(Assertion.assertNotNull(session)).thenReturn(session);

        PowerMockito.mockStatic(Math.class);

        when(configuration.getRoundingMode()).thenReturn(RoundingMode.FLOOR);
        when(configuration.getScale()).thenReturn(2);

        gradesBean = new GradesBean(session, gradeDAO, userDAO);

        /*
         * Da die zu testende Klasse eine Liste von Noten als Attribut enthält, wird eine
         * leere Liste dort eingesetzt (um eine NPE zu verhindern). Die tatsächlichen
         * Berechnungen werden ohnehin gemockt, so dass der Inhalt der Liste keine Rolle
         * spielt.
         */
        final List<Grade> allGrades = new ArrayList<>();
        Whitebox.setInternalState(gradesBean, allGrades);
    }

    /**
     * Testet, ob die Berechnung der Durchschnittsnote eine Fehlermeldung zurückgibt, wenn
     * keine Noten eingetragen sind.
     */
    @Test
    public void testGetGradeAverageNoGrades() {
        when(average(anyList(), anyInt(), any(RoundingMode.class))).thenThrow(
                ArithmeticException.class);

        final String expected = Whitebox.getInternalState(GradesBean.class,
                "NO_GRADES_PRESENT");

        assertThat(gradesBean.getGradeAverage()).isEqualTo(expected);
    }

    /**
     * Test, der sicherstellt, dass trailing zeros direkt nach dem Komma im Ergebnis des
     * Durchschnitts entfernt werden.
     */
    @Test
    public void testGetGradeAverageStripTwoTrailingZeros() {
        when(average(anyList(), anyInt(), any(RoundingMode.class))).thenReturn(
                new BigDecimal("2.00"));

        final String expected = "2";
        assertThat(gradesBean.getGradeAverage()).isEqualTo(expected);
    }

    /**
     * Testet, ob eine 0 vor dem Komma nicht versehentlich entfernt wird.
     */
    @Test
    public void testGetGradeAverageIntegerEndingWithZero() {
        when(average(anyList(), anyInt(), any(RoundingMode.class))).thenReturn(
                new BigDecimal("10"));

        final String expected = "10";
        assertThat(gradesBean.getGradeAverage()).isEqualTo(expected);
    }

    /**
     * Testet, ob eine trailing zero korrekt entfernt wird.
     */
    @Test
    public void testGetGradeAverageOneTrailingZero() {
        when(average(anyList(), anyInt(), any(RoundingMode.class))).thenReturn(
                new BigDecimal("1.20"));

        final String expected = "1.2";
        assertThat(gradesBean.getGradeAverage()).isEqualTo(expected);
    }

    /**
     * Testet, ob nicht versehentlich reguläre Nachkommastellen abgeschnitten werden.
     */
    @Test
    public void testGetGradeAverageFloatNoTrailingZeros() {
        when(average(anyList(), anyInt(), any(RoundingMode.class))).thenReturn(
                new BigDecimal("7.33"));

        final String expected = "7.33";
        assertThat(gradesBean.getGradeAverage()).isEqualTo(expected);
    }

    /**
     * Testet, ob die Berechnung der Mediannote eine Fehlermeldung zurückgibt, wenn keine
     * Noten eingetragen sind.
     */
    @Test
    public void testGetGradeMedianNoGrades() {
        when(median(anyList())).thenThrow(ArithmeticException.class);

        final String expected = Whitebox.getInternalState(GradesBean.class,
                "NO_GRADES_PRESENT");
        assertThat(gradesBean.getGradeMedian()).isEqualTo(expected);
    }

    /**
     * Test, der sicherstellt, dass trailing zeros direkt nach dem Komma im Ergebnis des
     * Durchschnitts entfernt werden.
     */
    @Test
    public void testGetGradeMedianStripTwoTrailingZeros() {
        when(median(anyList())).thenReturn(new BigDecimal("2.00"));

        final String expected = "2";
        assertThat(gradesBean.getGradeMedian()).isEqualTo(expected);
    }

    /**
     * Testet, ob eine 0 vor dem Komma nicht versehentlich entfernt wird.
     */
    @Test
    public void testGetGradeMedianIntegerEndingWithZero() {
        when(median(anyList())).thenReturn(new BigDecimal("10"));

        final String expected = "10";
        assertThat(gradesBean.getGradeMedian()).isEqualTo(expected);
    }

    /**
     * Testet, ob eine trailing zero korrekt entfernt wird.
     */
    @Test
    public void testGetGradeMedianOneTrailingZero() {
        when(median(anyList())).thenReturn(new BigDecimal("1.20"));

        final String expected = "1.2";
        assertThat(gradesBean.getGradeMedian()).isEqualTo(expected);
    }

    /**
     * Testet, ob nicht versehentlich reguläre Nachkommastellen abgeschnitten werden.
     */
    @Test
    public void testGetGradeMedianFloatNoTrailingZeros() {
        when(median(anyList())).thenReturn(new BigDecimal("4.27"));

        final String expected = "4.27";
        assertThat(gradesBean.getGradeMedian()).isEqualTo(expected);
    }

}
