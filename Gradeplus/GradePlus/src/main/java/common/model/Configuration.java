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
package common.model;

import businesslogic.Math;
import common.util.Assertion;

import static common.util.Assertion.assertNotNegative;
import static common.util.Assertion.assertNotNull;

import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Diese Klasse repräsentiert eine Konfiguration und stellt verschiedene
 * Konfigurationswerte bereit. Diese Werte können beispielsweise für
 * {@link Math#average(java.util.List, int, RoundingMode)} verwendet werden.
 *
 * Zwei Konfigurationen sind äquivalent, wenn sie die gleiche ID besitzen.
 *
 * @author Marcel Steinbeck, Sebastian Offermann
 * @version 2016-07-22
 */
@Entity
public class Configuration extends JPAEntity {

    /**
     * Der Standardwert für die verwendete Anzahl an Nachkommastellen.
     *
     * @see #scale
     */
    private static final int DEFAULT_SCALE = 2;

    /**
     * Der Standardwert für das verwendete Rundungsverfahren.
     *
     * @see #roundingMode
     */
    private static final RoundingMode DEFAULT_ROUND_MODE = RoundingMode.FLOOR;

    /**
     * Die Anzahl an Nachkommastellen, die bei Berechnungen mit
     * {@link java.math.BigDecimal} verwendet werden soll.
     */
    @Column(nullable = false)
    private Integer scale;

    /**
     * Das Rundungsverfahren, welches bei Berechnungen mit {@link java.math.BigDecimal}
     * verwendet werden soll.
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoundingMode roundingMode;

    /**
     * Gibt eine Default-Konfiguration zurück.
     *
     * @return die Default-Konfiguration.
     */
    public static Configuration getDefault() {
        final Configuration configuration = new Configuration();
        configuration.setScale(DEFAULT_SCALE);
        configuration.setRoundingMode(DEFAULT_ROUND_MODE);
        return configuration;
    }

    /**
     * Gibt die Anzahl an Nachkommastellen zurück, die bei Berechnungen mit
     * {@link java.math.BigDecimal} verwendet werden soll.
     *
     * @return Die Anzahl an Nachkommastellen, die bei Berechnungen mit
     *         {@link java.math.BigDecimal} verwendet werden soll.
     */
    public int getScale() {
        return scale;
    }

    /**
     * Setzt die Anzahl an Nachkommastellen, die bei Berechnungen mit
     * {@link java.math.BigDecimal} verwendet werden soll.
     *
     * @param pScale
     *            Die Anzahl an Nachkommastellen, die bei Berechnungen mit
     *            {@link java.math.BigDecimal} verwendet werden soll.
     * @throws IllegalArgumentException
     *             Falls {@code theScale < 0}.
     */
    public void setScale(final int pScale) {
        scale = Assertion.assertNotNegative(pScale);
    }

    /**
     * Gibt das Rundungsverfahren zurück, das bei Berechnungen mit
     * {@link java.math.BigDecimal} verwendet werden soll.
     *
     * @return Das Rundungsverfahren, das bei Berechnungen mit
     *         {@link java.math.BigDecimal} verwendet werden soll.
     */
    public RoundingMode getRoundingMode() {
        return roundingMode;
    }

    /**
     * Setzt das Rundungsverfahren, das bei Berechnungen mit {@link java.math.BigDecimal}
     * verwendet werden soll.
     *
     * @param pRoundingMode
     *            Das Rundungsverfahren, das bei Berechnungen mit
     *            {@link java.math.BigDecimal} verwendet werden soll.
     * @throws IllegalArgumentException
     *             Falls {@code theRoundingMode == null}.
     */
    public void setRoundingMode(final RoundingMode pRoundingMode) {
        roundingMode = Assertion.assertNotNull(pRoundingMode);
    }

    @Override
    public boolean equals(final Object theObject) {
        if (!(theObject instanceof Configuration)) {
            return false;
        }
        final Configuration otherConfiguration = (Configuration) theObject;
        return getId().equals(otherConfiguration.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return String.format("Configuration {id: %d, scale: %d, roundingMode: %s}",
                getId(), scale, roundingMode);
    }
}
