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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import businesslogic.Math;
import org.junit.Test;

/**
 * Hinweis: Die Tests prüfen die Durchschnittsberechnung immer für 8 Nachkommastellen und
 * {@link RoundingMode#FLOOR}. Das sollte im Allgemeinen ausreichen.
 *
 * Übung: Tests für die median-Methode anlegen (analog zu den Tests der average-Methode)
 *
 * @author Marcel Steinbeck, Karsten Hölscher
 * @version 2017-07-03
 */
public class MathTest {

    @Test
    public void testAverageNullList() {
        assertThatIllegalArgumentException().isThrownBy(
                () -> Math.average(null, 0, RoundingMode.FLOOR));

    }

    @Test
    public void testAverageListWithNullElement() {
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(BigDecimal.ONE);
        decimals.add(null);
        decimals.add(BigDecimal.ZERO);

        assertThatIllegalArgumentException().isThrownBy(
                () -> Math.average(decimals, 0, RoundingMode.FLOOR));
    }

    @Test
    public void testAverageEmptyList() {
        final List<BigDecimal> decimals = new ArrayList<>();

        assertThatExceptionOfType(ArithmeticException.class).isThrownBy(
                () -> Math.average(decimals, 8, RoundingMode.FLOOR));
    }

    @Test
    public void testAverageSingleDecimalOne() {
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(BigDecimal.ONE);

        final BigDecimal result = Math.average(decimals, 8, RoundingMode.FLOOR);
        final BigDecimal expected = new BigDecimal("1.00000000");
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testAverageSingleDecimalPi() {
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(new BigDecimal("3.1415"));

        final BigDecimal result = Math.average(decimals, 8, RoundingMode.FLOOR);
        final BigDecimal expected = new BigDecimal("3.14150000");
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testAverageMultiDecimalInteger() {
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(new BigDecimal(3));
        decimals.add(new BigDecimal(17));
        decimals.add(new BigDecimal(20));
        decimals.add(new BigDecimal(8));

        final BigDecimal result = Math.average(decimals, 8, RoundingMode.FLOOR);
        final BigDecimal expected = new BigDecimal("12.00000000");
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testAverageMultiDecimalRationalNumber() {
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(new BigDecimal(1));
        decimals.add(new BigDecimal(0.5));
        decimals.add(new BigDecimal(1.5));
        decimals.add(new BigDecimal(2));

        final BigDecimal result = Math.average(decimals, 8, RoundingMode.FLOOR);
        final BigDecimal expected = new BigDecimal("1.25000000");
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void testAverageMultiDecimalPositivesAndNegatives() {
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(new BigDecimal("-13.53"));
        decimals.add(new BigDecimal("-2.91"));
        decimals.add(new BigDecimal("25.7"));
        decimals.add(new BigDecimal("-9.26"));

        final BigDecimal result = Math.average(decimals, 8, RoundingMode.FLOOR);
        final BigDecimal expected = new BigDecimal("0.00000000");
        assertThat(result).isEqualTo(expected);
    }

    /**
     * Testet, ob die übergebene Liste nicht verändert wird.
     */
    @Test
    public void testAverageListUnchanged() {
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(new BigDecimal("-13.53"));
        decimals.add(new BigDecimal("-2.91"));
        decimals.add(new BigDecimal("25.7"));
        decimals.add(new BigDecimal("-9.26"));

        final List<BigDecimal> result = new ArrayList<>(decimals);
        Math.average(decimals, 8, RoundingMode.FLOOR);
        assertThat(result).isEqualTo(decimals);
    }

    /**
     * Testet die erwartete Exception falls der Parameterwert {@code null} ist.
     */
    @Test
    public void testMedianNullList() {
        assertThatIllegalArgumentException().isThrownBy(() -> Math.median(null));
    }

    /**
     * Testet die erwartete Exception falls in der Liste irgendwo {@code null} enthalten
     * ist.
     */
    @Test
    public void testMedianListWithNullElement() {
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(BigDecimal.ONE);
        decimals.add(null);
        decimals.add(BigDecimal.ZERO);
        assertThatIllegalArgumentException().isThrownBy(() -> Math.median(decimals));
    }

    /**
     * Testet die erwartete Exception falls in der Liste vorne {@code null} enthalten ist.
     */
    @Test
    public void testMedianListWithNullElementFront() {
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(null);
        decimals.add(BigDecimal.ONE);
        decimals.add(BigDecimal.ZERO);
        assertThatIllegalArgumentException().isThrownBy(() -> Math.median(decimals));
    }

    /**
     * Testet die erwartete Exception falls in der Liste hinten {@code null} enthalten
     * ist.
     */
    @Test
    public void testMedianListWithNullElementBack() {
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(BigDecimal.ONE);
        decimals.add(BigDecimal.ZERO);
        decimals.add(null);
        assertThatIllegalArgumentException().isThrownBy(() -> Math.median(decimals));
    }

    /**
     * Testet die erwartete Exception falls die Liste leer ist.
     */
    @Test
    public void testMedianEmptyList() {
        final List<BigDecimal> decimals = new ArrayList<>();
        assertThatExceptionOfType(ArithmeticException.class).isThrownBy(
                () -> Math.median(decimals));
    }

    /**
     * Testet den erwarteten Wert für eine einelementige Liste.
     */
    @Test
    public void testMedianOneElement() {
        BigDecimal expected = BigDecimal.ONE;
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(BigDecimal.ONE);
        BigDecimal result = Math.median(decimals);
        assertThat(result).isEqualTo(expected);
    }

    /**
     * Testet den erwarteten Wert für eine zweielementige Liste (Obermedian).
     */
    @Test
    public void testMedianTwoElements() {
        BigDecimal expected = new BigDecimal("-2.5");
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(new BigDecimal("-13.258"));
        decimals.add(new BigDecimal("-2.5"));
        BigDecimal result = Math.median(decimals);
        assertThat(result).isEqualTo(expected);
    }

    /**
     * Testet den Normalfall für eine Menge mit ungerade vielen Elementen.
     */
    @Test
    public void testMedian() {
        BigDecimal expected = new BigDecimal(".5");
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(new BigDecimal("19000"));
        decimals.add(new BigDecimal("-2.5"));
        decimals.add(new BigDecimal(".5"));
        decimals.add(new BigDecimal("-13.258"));
        decimals.add(new BigDecimal("-1.5"));
        decimals.add(new BigDecimal("21675788"));
        decimals.add(new BigDecimal("2.5675"));
        BigDecimal result = Math.median(decimals);
        assertThat(result).isEqualTo(expected);
    }

    /**
     * Testet die Berechnung des Obermedians bei gerader Anzahl von Elementen.
     */
    @Test
    public void testMedianEvenNumber() {
        BigDecimal expected = new BigDecimal("7");
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(new BigDecimal("5"));
        decimals.add(new BigDecimal("2"));
        decimals.add(new BigDecimal("7"));
        decimals.add(new BigDecimal("9"));
        BigDecimal result = Math.median(decimals);
        assertThat(result).isEqualTo(expected);
    }

    /**
     * Testet, ob der korrekte Median gefunden wird, wenn er ganz vorne in der Liste
     * steht.
     */
    @Test
    public void testMedianFront() {
        BigDecimal expected = new BigDecimal("7");
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(new BigDecimal("7"));
        decimals.add(new BigDecimal("400"));
        decimals.add(new BigDecimal("-15.2"));
        decimals.add(new BigDecimal("19.25"));
        decimals.add(new BigDecimal("-6"));
        decimals.add(new BigDecimal("212.33"));
        decimals.add(new BigDecimal("2"));
        BigDecimal result = Math.median(decimals);
        assertThat(result).isEqualTo(expected);
    }

    /**
     * Testet, ob der korrekte Median gefunden wird, wenn er ganz hinten in der Liste
     * steht.
     */
    @Test
    public void testMedianBack() {
        BigDecimal expected = new BigDecimal("15.2");
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(new BigDecimal("19.45"));
        decimals.add(new BigDecimal("-7"));
        decimals.add(new BigDecimal("211.33"));
        decimals.add(new BigDecimal("2.1"));
        decimals.add(new BigDecimal("15.23"));
        decimals.add(new BigDecimal("-4.3"));
        decimals.add(new BigDecimal("12.45"));
        decimals.add(new BigDecimal("20009087"));
        decimals.add(new BigDecimal("15.2"));
        BigDecimal result = Math.median(decimals);
        assertThat(result).isEqualTo(expected);
    }

    /**
     * Testet, ob der korrekte Median (Obermedian) gefunden wird, wenn er ganz vorne in
     * einer Liste mit einer geraden Anzahl von Elementen steht.
     */
    @Test
    public void testMedianEvenNumberFront() {
        BigDecimal expected = new BigDecimal("14.7");
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(new BigDecimal("14.7"));
        decimals.add(new BigDecimal("-211.33"));
        decimals.add(new BigDecimal("900"));
        decimals.add(new BigDecimal("-356"));
        decimals.add(new BigDecimal("15.23"));
        decimals.add(new BigDecimal("-1000000919.45"));
        BigDecimal result = Math.median(decimals);
        assertThat(result).isEqualTo(expected);
    }

    /**
     * Testet, ob der korrekte Median (Obermedian) gefunden wird, wenn er ganz hinten in
     * einer Liste mit einer geraden Anzahl von Elementen steht.
     */
    @Test
    public void testMedianEvenNumberBack() {
        BigDecimal expected = new BigDecimal("19");
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(new BigDecimal("-2"));
        decimals.add(new BigDecimal("1000000919.45"));
        decimals.add(new BigDecimal("1"));
        decimals.add(new BigDecimal("23"));
        decimals.add(new BigDecimal("19.45"));
        decimals.add(new BigDecimal("0"));
        decimals.add(new BigDecimal("2"));
        decimals.add(new BigDecimal("19"));
        BigDecimal result = Math.median(decimals);
        assertThat(result).isEqualTo(expected);
    }

    /**
     * Testet, ob der korrekte Median gefunden wird, wenn alle Listenelemente gleich sind.
     */
    @Test
    public void testMedianAllEqual() {
        BigDecimal expected = new BigDecimal("22");
        final List<BigDecimal> decimals = new ArrayList<>();
        for (int i = 0; i < 235; i++) {
            decimals.add(new BigDecimal("22"));
        }
        BigDecimal result = Math.median(decimals);
        assertThat(result).isEqualTo(expected);
    }

    /**
     * Testet, ob die übergebene Liste nicht verändert wird.
     */
    @Test
    public void testMedianParameterUnchanged() {
        final List<BigDecimal> decimals = new ArrayList<>();
        decimals.add(new BigDecimal("-2"));
        decimals.add(new BigDecimal("1000000919.45"));
        decimals.add(new BigDecimal("1"));
        decimals.add(new BigDecimal("23"));
        decimals.add(new BigDecimal("19.45"));
        decimals.add(new BigDecimal("0"));
        decimals.add(new BigDecimal("2"));
        decimals.add(new BigDecimal("19"));
        final List<BigDecimal> result = new ArrayList<>(decimals);
        Math.median(decimals);
        assertThat(result).isEqualTo(decimals);
    }

}
