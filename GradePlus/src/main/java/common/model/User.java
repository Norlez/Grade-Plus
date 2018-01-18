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

import static common.util.Assertion.assertNotNull;
import static java.util.Collections.unmodifiableList;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import businesslogic.Crypt;
import common.util.Assertion;

/**
 * Repräsentation eines Benutzers. Ein Benutzer verfügt über einen Namen, ein Passwort,
 * eine Email-Adresse sowie eine Liste von Noten.
 * 
 * Zwei Benutzer sind äquivalent, wenn sie die gleiche ID besitzen.
 *
 * @author Karsten Hölscher, Marcel Steinbeck
 * @version 2017-12-23
 */
@Entity
@Table(name = "Users")
@NamedQueries({
        // Die Umbenennung der Tabelle 'User' in 'Users' hat
        // keine Auswirkung auf den verwendeten Namen im Query.
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email") })
public class User extends JPAEntity implements Serializable {

    /**
     * Standard-Sprache für Benutzer.
     */
    private static final Locale DEFAULT_LANGUAGE = Locale.GERMAN;

    /**
     * Die eindeutige SerialisierungsID.
     */
    private static final long serialVersionUID = -2841896419854631425L;

    /**
     * Der Benutzername.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Das verschlüsselte Passwort dieses Benutzers.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Die Email-Adresse dieses Benutzers.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Die Sprache dieses Benutzers.
     */
    @Column(nullable = false)
    private Locale language = DEFAULT_LANGUAGE;

    /**
     * Die Noteneinträge dieses Benutzers.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Grade> grades;

    /**
     * Die Matrikelnummer eines Studenten.
     */
    private int matrNr;

    /**
     * Liste mit allen Rollen des Benutzers.
     */
    private List<Role> roles;

    /**
     * Gibt an, ob der Benutzer aktiv ist.
     */
    private boolean isActive;

    /**
     * Die Historie der bestimmter Vorgänge eines Prüflings.
     */
    // TODO: Problem mit History
    // private History history;

    /**
     * Gibt den Benutzernamen dieses Benutzers zurück.
     *
     * @return Den Benutzernamen dieses Benutzers.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setzt den Benutzernamen dieses Benutzers auf den gegebenen Benutzernamen.
     *
     * @param pUsername
     *            Der neue Benutzername für diesen Benutzer.
     * @throws IllegalArgumentException
     *             Falls der gegebene Benutzername den Wert {@code null} hat.
     */
    public void setUsername(final String pUsername) {
        username = Assertion.assertNotNull(pUsername);
    }

    /**
     * Gibt das verschlüsselte Passwort dieses Benutzers zurück.
     *
     * @return Das Passwort dieses Benutzers.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Verschlüsselt das gegebene Passwort (unter Verwendung von
     * {@link Crypt#hash(java.lang.String)}) und setzt das Passwort dieses Benutzers auf
     * das verschlüsselte Passwort.
     *
     * @param pPassword
     *            Das neue, zu verschlüsselnde Passwort für diesen Benutzer.
     * @throws IllegalArgumentException
     *             Falls das gegebene Passwort den Wert {@code null} hat.
     * @throws UnsupportedOperationException
     *             falls das System nicht über die Voraussetzungen verfügt, das Hashen
     *             durchzuführen.
     */
    public void setPassword(final String pPassword) {
        password = Crypt.hash(Assertion.assertNotNull(pPassword));
    }

    /**
     * Gibt die EMail-Adresse dieses Benutzers zurück.
     *
     * @return Die EMail-Adresse dieses Benutzers.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setzt die EMail-Adresse dieses Benutzers auf die gegebene EMail-Adresse.
     *
     * @param pEmail
     *            Die neue EMail-Adresse für diesen Benutzer.
     * @throws IllegalArgumentException
     *             Falls die gegebene EMail-Adresse den Wert {@code null} hat.
     */
    public void setEmail(final String pEmail) {
        email = Assertion.assertNotNull(pEmail);
    }

    /**
     * Gibt die Sprache dieses Benutzers zurück.
     * 
     * @return Die Sprache dieses Benutzers.
     */
    public Locale getLanguage() {
        return language;
    }

    /**
     * Setzt die Sprache dieses Benutzers.
     * 
     * @param pLanguage
     *            Die neue Sprache für diesen Benutzer.
     */
    public void setLanguage(final Locale pLanguage) {
        language = Assertion.assertNotNull(pLanguage);
    }

    /**
     * Fügt {@code theGrade} der Liste der Noteneintragungen hinzu.
     *
     * @param pGrade
     *            Die hinzuzufügende {@link Grade}.
     */
    public void addGrade(final Grade pGrade) {
        grades.add(Assertion.assertNotNull(pGrade));
    }

    /**
     * Entfernt {@code theGrade} aus der Liste der Noteneintragungen.
     *
     * @param pGrade
     *            Die zu entfernende {@link Grade}.
     */
    public void removeGrade(final Grade pGrade) {
        grades.remove(Assertion.assertNotNull(pGrade));
    }

    /**
     * Gibt eine unveränderliche Sicht auf die Liste der Noteneintragungen zurück. Um ein
     * {@link Grade} hinzuzufügen bzw. zu entfernen müssen die Methoden
     * {@link #addGrade(Grade)} und {@link #removeGrade(Grade)} verwendet werden.
     *
     * @return Eine unveränderliche Sicht auf die Liste der Noteneintragungen.
     */
    public List<Grade> getGrades() {
        return unmodifiableList(grades);
    }

    @Override
    public boolean equals(final Object theObject) {
        if (!(theObject instanceof User)) {
            return false;
        }
        final User otherUser = (User) theObject;
        return getId().equals(otherUser.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return String.format("User {id: %d, username: %s, password: %s, email: %s}",
                getId(), username, password, email);
    }

    /**
     * Liefert die Standardsprache zurück.
     * 
     * @return Die Standardsprache.
     */
    public static Locale getDefaultLanguage() {
        return DEFAULT_LANGUAGE;
    }

}
