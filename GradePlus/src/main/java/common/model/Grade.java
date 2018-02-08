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

import common.util.Assertion;

import static common.util.Assertion.assertNotNull;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

/**
 * Repräsentation einer Noteneintragung für ein Fach. Zwei Noteneintragungen sind gleich,
 * wenn sie die gleiche Kennung besitzen, d. h. die Kennung identifiziert eine
 * Noteneintragung in eindeutiger Weise.
 *
 * Zwei Noteneintragungen sind äquivalent, wenn sie die gleiche ID besitzen.
 *
 * @author Dennis Schürholz, Karsten Hölscher, Sebastian Offermann
 * @version 2016-07-22
 */
@Entity
@Table(name = "grades")
public class Grade extends JPAEntity implements Serializable {

    /**
     * Die eindeutige SerialisierungsID.
     */
    private static final long serialVersionUID = -3554277193592515328L;

    /**
     * Der Benutzer, dem dieser Noteneintrag zugeordnet ist.
     */
    @ManyToOne(optional = false)
    private User user;

    /**
     * Das Schulfach dieser Noteneintragung.
     */
    @Column(nullable = false)
    private String subject;

    /**
     * Die Note dieser Noteneintragung.
     */
    @Column(nullable = false)
    private BigDecimal mark;

    /**
     * Gibt den Benutzer zurück, dem dieser Noteneintrag zugeordnet ist.
     *
     * @return Den Benutzer, dem dieser Noteneintrag zugeordnet.
     */
    public User getUser() {
        return user;
    }

    /**
     * Setzt den Benutzer, dem dieser Noteneintrag zugeordnet ist, auf den gegebenen
     * Benutzer.
     *
     * @param pUser
     *            Der neue Benutzer, dem dieser Noteneintrag zugeordnet werden soll
     * @throws IllegalArgumentException
     *             Falls der gegebene Benutzer den Wert {@code null} hat.
     */
    public void setUser(final User pUser) {
        user = Assertion.assertNotNull(pUser);
    }

    /**
     * Gibt das Fach dieses Noteneintrags zurück.
     *
     * @return Das Fach dieses Noteneintrags.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Setzt das Fach dieser Noteneintragung auf das gegebene Fach.
     *
     * @param pSubject
     *            Das neue Fach für diese Noteneintragung.
     * @throws IllegalArgumentException
     *             Falls das gegebene Fach den Wert {@code null} hat.
     */
    public void setSubject(final String pSubject) {
        subject = Assertion.assertNotNull(pSubject);
    }

    /**
     * Gibt die Note dieses Noteneintrags zurück.
     *
     * @return Die Note dieses Noteneintrags.
     */
    public BigDecimal getMark() {
        return mark;
    }

    /**
     * Setzt die Note dieser Noteneintragung auf die gegebene Note.
     *
     * @param pMark
     *            Die neue Note für diese Noteneintragung.
     * @throws IllegalArgumentException
     *             Falls die gegebene Note den Wert {@code null} hat.
     */
    public void setMark(final BigDecimal pMark) {
        mark = Assertion.assertNotNull(pMark);
    }

    @Override
    public boolean equals(final Object theObject) {
        if (!(theObject instanceof Grade)) {
            return false;
        }
        final Grade otherGrade = (Grade) theObject;
        return getId().equals(otherGrade.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return String.format("Grade {id: %d, subject: %s, mark: %s}", getId(), subject,
                mark);
    }
}
