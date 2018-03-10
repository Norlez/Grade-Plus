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
import businesslogic.Crypt;
import org.junit.Test;

/**
 * Testet Methoden in der Klasse {@link businesslogic.Crypt} als Komponententests.
 *
 * @author Karsten Hölscher
 * @version 2017-07-03
 */
public class CryptTest {
    /**
     * Testet die erwartete Exception falls der Parameterwert {@code null} ist.
     */

    @Test
    public void testHashNull() {
        assertThatIllegalArgumentException().isThrownBy(() -> Crypt.hash(null));
    }

    /**
     * Testet den erwarteten MD5-Hash für den leeren String.
     */
    /*
     * @Test public void testHashEmpty() { final String expected =
     * "D41D8CD98F00B204E9800998ECF8427E"; final String result = Crypt.hash("");
     * assertThat(result).isEqualTo(expected); }
     * 
     * /** Testet den erwarteten MD5-Hash für den Buchstaben a.
     */
    /*
     * @Test public void testHashCharacter() { final String expected =
     * "0CC175B9C0F1B6A831C399E269772661"; final String result = Crypt.hash("a");
     * assertThat(result).isEqualTo(expected); }
     * 
     * /** Testet den erwarteten MD5-Hash für eine längere Zeichenkette.
     */
    /*
     * @Test public void testHashArbitratyString() { final String expected =
     * "ED076287532E86365E841E92BFC50D8C"; final String result =
     * Crypt.hash("Hello World!"); assertThat(result).isEqualTo(expected); }
     */
}