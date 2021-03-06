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
 * @author Dennis Schürholz, Karsten Hölscher, Sebastian Offermann, Marvin Kampen
 * @version 2018-03-11
 */
@Entity
@Table(name = "grades")
@NamedQueries({
        @NamedQuery(name = "grades.getAll", query = "SELECT g FROM Grade g"),
        @NamedQuery(name = "grades.forUser", query = "SELECT g FROM Grade g WHERE g.user = :user") })
public class Grade extends JPAEntity implements Serializable {

    /**
     * Die eindeutige SerialisierungsID.
     */
    private static final long serialVersionUID = -3554277193592515328L;

    /**
     * Der Benutzer, dem dieser Noteneintrag zugeordnet ist.
     */
    @ManyToOne(optional = true)
    private User user;

    /**
     * Das Schulfach dieser Noteneintragung.
     */
    @Column(nullable = true)
    private String subject;

    /**
     * Die Note dieser Noteneintragung.
     */
    @Column(nullable = false)
    private Double mark;

    /**
     * Die Endnote für das Fach
     */
    @Column(nullable = true)
    private Double endMark;

    /**
     * Erhält eine Referenz auf die JoinExam, der die Note zugeordnet werden soll.
     */
    @OneToOne
    private JoinExam joinExam;

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
    public Double getMark() {
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
    public void setMark(final Double pMark) {
        mark = Assertion.assertNotNull(pMark);
    }

    /**
     *  Setter für das JoinExam-Attribut
     * @param joinExam
     */
    public void setJoinExam(JoinExam joinExam) {
        this.joinExam = joinExam;
    }

    /**
     *  Getter für das JoinExam-Attribut
     * @return joinExam
     */
    public JoinExam getJoinExam() {
        return joinExam;
    }

    /**
     * Getter für das EndMark-Attribut
     * @return endMark
     */
    public Double getEndMark() {
        return endMark;
    }

    /**
     * Setter für das EndMark-Attribut
     * @param endMark
     */
    public void setEndMark(Double endMark) {
        this.endMark = endMark;
    }

    /**
     * Vergleicht die Objekte der Klasse Grade auf Gleichheit.
     * @param theObject
     * @return true, falls die Objekte gleich sind, sonst false
     */
    @Override
    public boolean equals(final Object theObject) {
        if (!(theObject instanceof Grade)) {
            return false;
        }
        final Grade otherGrade = (Grade) theObject;
        return getId().equals(otherGrade.getId());
    }

    /**
     * Gibt den HashCode für das Grade-Objekt zurück.
     * @return Hashcode als int
     */
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Gibt die String Repräsentation des Grade-Objekts zurück.
     * @return Stringrepräsentation
     */
    @Override
    public String toString() {
        return String.format("Grade {id: %d, subject: %s, mark: %s}", getId(), subject,
                mark);
    }

    /**
     * Gibt eine String Repräsentation zurück, die für CSVs geeignet ist.
     * @return String im Format: Attribut; Attribut;
     */
    public String toCSV() {
        return String.format("%d; %s; %s; %f", getId(), user, subject, mark);
    }
}
