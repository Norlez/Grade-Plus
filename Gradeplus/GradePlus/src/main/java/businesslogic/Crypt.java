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

import static common.util.Assertion.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Diese Klasse stellt Kryptofunktionalität bereit.
 *
 * @author Dennis Schürholz, Karsten Hölscher, Sebastian Offermann
 * @version 2016-07-22
 */
public class Crypt {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(Crypt.class);

    /**
     * Privater Konstruktor, der verhindert, dass eine Instanz dieser Utility-Klasse
     * erzeugt werden kann.
     */
    private Crypt() {
    }

    /**
     * Erzeugt einen MD5-Hash für die gegebene Zeichenkette und gibt diesen zurück. TODo:
     * Code bereinigen und Javadoc anpassen.
     * 
     * @param input
     *            Die Zeichenkette, für die der MD5-Hash erstellt werden soll.
     * @return MD5-Hash für die gegebene Zeichenkette.
     * @throws IllegalArgumentException
     *             Falls das gegebene Passwort den Wert {@code null} hat.
     * @throws UnsupportedOperationException
     *             falls das System nicht über die Voraussetzungen verfügt, das Hashen
     *             durchzuführen. Die {@code UnsupportedOperationException} kapselt eine
     *             Exception, die den Grund näher beschreibt: eine
     *             {@code NoSuchAlgorithmException}, falls es keinen Security Provider für
     *             den Algorithmus {@code MD5} im System gibt; eine
     *             {@code UnsupportedEncodingException}, falls der Zeichensatz
     *             {@code UTF-8} nicht unterstützt wird.
     */
    public static String hash(final String input) {
        return BCrypt.hashpw(assertNotNull(input), BCrypt.gensalt());
    }

    /**
     *
     * Verschlüsselt den gegebenen Input mit einer noch nicht vorgesehenen
     * Verschlüsselungsmethode.
     *
     *
     * @param input
     *            Die Zeichenkette, für die die Verschlüsselung erstellt werden soll.
     * @return Verschlüsselte Zeichenkette für die gegebene Zeichenkette
     * @throws IllegalArgumentException
     *             Falls das gegebene Passwort den Wert {@code null} hat.
     */
    public static String encryption(final String input) {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * Entschlüsselt den gegebenen Input mit einer noch nicht vorgesehenen
     * Entschlüsselungsmethode.
     *
     *
     * @param input
     *            Die Zeichenkette, für die die Entschlüsselung erstellt werden soll.
     * @return Entschlüsselte Zeichenkette für die gegebene Zeichenkette
     * @throws IllegalArgumentException
     *             Falls das gegebene Passwort den Wert {@code null} hat.
     *
     */
    public static String decryption(final String input) {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * codiert den gegebenen Input mit einem noch nicht vorgesehenen Codierungsverfahren.
     *
     * @param input
     *            Die Zeichenkette, für die Codierung erstellt werden soll.
     * @return codierte Zeichenkette für die gegebene Zeichenkette
     * @throws IllegalArgumentException
     *             Falls das gegebene Passwort den Wert {@code null} hat.
     */
    public static String encode(final String input) {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * codiert den gegebenen Input mit einem noch nicht vorgesehenen Codierungsverfahren.
     *
     * @param input
     *            Die Zeichenkette, für die Decodierung erstellt werden soll.
     * @return codierte Zeichenkette für die gegebene Zeichenkette
     * @throws IllegalArgumentException
     *             Falls das gegebene Passwort den Wert {@code null} hat.
     *
     *
     */
    public static String decode(final String input) {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * Berechnet den ASCII - Wert einer übergebenen Zeichenkette.
     *
     * @param input
     *            Die Zeichenkette, die umgewandelt werden soll.
     * @return Integer, die dem Zeichen der ASCII Tabelle entspricht.
     * @throws IllegalArgumentException
     *             Falls der gegebene Input den Wert {@code null} hat.
     *
     *
     */
    public static Integer stringAsNumber(final String input) {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * Berechnet den Integer aus der ASCII Tabelle, die der Zeichenkette entspricht.
     * 
     * @param input
     *            Der Integer, der umgewandelt werden soll.
     * @return Zeichenkette, die der Zahl der ASCII Tabelle entsprich.t
     * @throws IllegalArgumentException
     *             Falls der gegebene Input den Wert {@code null} hat.
     *
     *
     */
    public static String numberAsString(final Integer input) {
        throw new UnsupportedOperationException();
    }
}
