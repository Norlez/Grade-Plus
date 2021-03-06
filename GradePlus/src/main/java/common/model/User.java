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
import static common.util.Assertion.assertNull;
import static java.util.Collections.unmodifiableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;

import businesslogic.Crypt;
import common.util.Assertion;

/**
 * Repräsentation eines Benutzers. Ein Benutzer verfügt über einen Namen, ein Passwort,
 * eine Email-Adresse sowie eine Liste von Noten.
 * <p>
 * Zwei Benutzer sind äquivalent, wenn sie die gleiche ID besitzen.
 *
 * @author Karsten Hölscher, Marcel Steinbeck, Marvin Kampen, Anil Olgun, Tugce Karakus
 * @version 2018-02-08
 */
@Entity
@Table(name = "Users")
@NamedQueries({
        // Die Umbenennung der Tabelle 'User' in 'Users' hat
        // keine Auswirkung auf den verwendeten Namen im Query.
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
        @NamedQuery(name = "User.findByRole", query = "SELECT u FROM User u WHERE u.role = :role"),
        @NamedQuery(name = "User.findByMatrNr", query = "SELECT u FROM  User u WHERE u.matrNr = :matrNr") })
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
     * Der Benutzername. Ist die gekürzte Email-Adresse
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Der Nachname des Benutzers.
     */
    @Column(nullable = false)
    private String surname;

    /**
     * Der Vorname des Benutzers.
     */
    @Column(nullable = false)
    private String givenName;

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
     * Die Liste der InstanceLectures in denen der User ein Prüfer ist.
     */
    @ManyToMany
    @JoinTable(name = "Prof_ILV", joinColumns = @JoinColumn(name = "Prof_ID", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "ILV_ID", referencedColumnName = "id"))
    private List<InstanceLecture> asProf;

    /**
     * Die Liste der InstanceLectures in denen der User ein Student ist.
     */
    @ManyToMany
    @JoinTable(name = "Student_ILV", joinColumns = @JoinColumn(name = "User_ID", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "ILV_ID", referencedColumnName = "id"))
    private List<InstanceLecture> asStudent;

    /**
     * Die Liste der Exams an denen der User als Prüfer eingetragen ist.
     */
    @ManyToMany
    @JoinTable(name = "User_Exam", joinColumns = @JoinColumn(name = "User_ID", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "Exam_ID", referencedColumnName = "id"))
    private List<Exam> ExamAsProf;

    /**
     * Die Matrikelnummer eines Studenten.
     */
    @Column(unique = true)
    private String matrNr;

    /**
     * Rolle des Benutzers.
     */
    @Column(nullable = false)
    private Role role = Role.STUDENT;

    /**
     * Gibt an, ob der Benutzer aktiv ist.
     */
    @Column(nullable = false)
    private boolean isActive = true;

    /**
     * Die Liste der JoinExams zu den Prüfungen. Ein JoinExam ist für die Teilnahme
     * an einer Prüfung als Prüfling gedacht.
     */
    @OneToMany(mappedBy = "pruefling")
    private List<JoinExam> participation;

    /**
     * Loggt die Atkionen des Users.
     */
    @Lob
    @Column(nullable = true)
    private String loggingString = "";



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
     * Setzt den Username in der Mail.
     */
    public void setUsernameForUserMail() {
        final String tmp = usernameFromEmail(email, '@');
        username = Assertion.assertNotNull(tmp);
    }

    /**
     * Gibt den Nachnamen dieses Benutzers zurück.
     *
     * @return Den Nachnamen des Benutzers.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Setzt den Nachnamen dieses Benutzers auf den gegebenen Nachnamen.
     *
     * @param pSurname
     *            Der neue Nachname für diesen Benutzer.
     * @throws IllegalArgumentException
     *             Falls der gegebene Nachname den Wert {@code null} hat.
     */
    public void setSurname(final String pSurname) {
        surname = assertNotNull(pSurname);
    }

    /**
     * Gibt den Vornamen dieses Benutzers zurück.
     *
     * @return Den Vornamen des Benutzers.
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Setzt den Vornamen dieses Benutzers auf den gegebenen Nachnamen.
     *
     * @param pGivenName
     *            Der neue Vorname für diesen Benutzer.
     * @throws IllegalArgumentException
     *             Falls der gegebene Vorname den Wert {@code null} hat.
     */
    public void setGivenName(final String pGivenName) {
        givenName = assertNotNull(pGivenName);
    }

    /**
     * Gibt die Matrikelnummer dieses Benutzers zurück.
     *
     * @return Die Matrikelnummer des Benutzers.
     */
    public String getMatrNr() {
        return matrNr;
    }

    /**
     * Setzt die Matrikelnummer dieses Benutzers auf den gegebenen Nachnamen.
     *
     * @param pMatrNr
     *            Die neue Matrikelnummer für diesen Benutzer.
     * @throws IllegalArgumentException
     *             Falls die gegebene Matrikelnummer den Wert {@code null} hat.
     */
    public void setMatrNr(final String pMatrNr) {
        matrNr = assertNotNull(pMatrNr);
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

    /**
     * Gibt das isActive-Attribut zurück
     * @return isActive
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Ändert das isActive Attribut von true auf false und von false auf true.
     */
    public void setActive() {
        if (isActive == true) {
            isActive = false;
        } else {
            isActive = true;
        }
    }

    /**
     * Gibt den LoggingString zurück.
     * @return
     */
    public String getLoggingString() {
        return loggingString;
    }

    /**
     * Setzt den LoggingString.
     * @param pLoggingString
     */
    public void setLoggingString(String pLoggingString) {
        this.loggingString = pLoggingString + loggingString;
    }

    /**
     * Gibt die Rolle des Nutzers zurück.
     * @return role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Setzt die Rolle des Nutzers.
     * @param role
     */
    public void setRole(Role role) {
        this.role = role;
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
        return String.format("%d; %s; %s; %b; %s; %s; %s; %s; %s; %s", getId(), email,
                givenName, isActive, language, matrNr, password, role, surname, username);
    }

    /**
     * Gibt den String für die CSV zurück.
     *
     * @return CSV-String mit den Attributen von User
     */
    public String toCSV() {
        return String.format("%d; %s; %s; %b; %s; %s; %s; %s; %s; %s", getId(), email,
                givenName, isActive, language, matrNr, password, role, surname, username);
    }

    /**
     * Trennt den String bis zum Trennungszeichen
     *
     * @param pString
     * @param delimeter
     * @return
     */
    public String usernameFromEmail(String pString, char delimeter) {
        return pString.substring(0, pString.indexOf(delimeter));
    }

    /**
     * Liefert die Standardsprache zurück.
     *
     * @return Die Standardsprache.
     */
    public static Locale getDefaultLanguage() {
        return DEFAULT_LANGUAGE;
    }

    /**
     * Setzt die Liste der Veranstaltungen in denen man als Prüfer eingetragen ist.
     * @param asProf
     */
    public void setAsProf(List<InstanceLecture> asProf) {
        this.asProf = asProf;
    }

    /**
     * Gibt die Liste der Veranstaltungen zurück, wo man als Prüfer eingetragen ist.
     * @return Liste der ILVs
     */
    public List<InstanceLecture> getAsProf() {
        return asProf;
    }

    /**
     * Fügt die ILV zu der Liste von Veranstaltungen hinzu bei denen man Prüfer ist.
     * @param i, die ILV die hinzugefügt wird.
     */
    public void addAsProfToIlv(final InstanceLecture i) {
        asProf.add(i);
    }

    /**
     *Entfernt die ILV aus der Liste von Veranstaltungen bei denen man Prüfer ist.
     * @param i, die ILV die entfernt wird.
     */
    public void removeProfFromIlv(final InstanceLecture i) {
        asProf.remove(i);
    }

    /**
     * Setzt die Liste der ILVs bei denen man als Student eingetragen ist.
     * @param asStudent
     */
    public void setAsStudent(List<InstanceLecture> asStudent) {
        this.asStudent = asStudent;
    }

    /**
     * Gibt die Liste der Ilvs zurück bei denen man als Student eingetragen ist.
     * @return Liste von Veranstaltungen, in denen man als Student eingetragen ist.
     */
    public List<InstanceLecture> getAsStudent() {
        return asStudent;
    }

    /**
     * Fügt einen ILV zu der Liste von Veranstaltungen,
     * in denen man als Student eingetragen ist, hinzu.
     * @param i, welche hinzugefügt wird.
     */
    public void addAsStudentToIlv(final InstanceLecture i) {
        asStudent.add(i);
    }

    /**
     * Entfernt eine ILV aus der Liste von Veranstaltungen,
     * in denen man als Student eingetragen ist.
     * @param i, welche entfernt wird.
     */
    public void removeStudentFromIlv(final InstanceLecture i) {
        asStudent.remove(i);
    }

    /**
     * Setzt die Liste der Prüfungstermine, in denen man als Prüfer agiert.
     * @param examAsProf
     */
    public void setExamAsProf(List<Exam> examAsProf) {
        ExamAsProf = examAsProf;
    }

    /**
     * Gibt die Liste der Prüfungstermine, in denen man als Prüfer agiert.
     * @return ExamAsProf
     */
    public List<Exam> getExamAsProf() {
        return ExamAsProf;
    }

    /**
     * Fügt eine Prüfung zu der Liste von Prüfungen hinzu, in denen man Prüfer ist.
     * @param e, welche hinzugefüt wird.
     */
    public void addExamAsProf(final Exam e) {
        ExamAsProf.add(e);
    }

    /**
     * Entfernt eine Prüfung aus der Liste von Prüfungen, in denen man Prüfer ist.
     * @param e, welche entfernt wird.
     */
    public void removeExamAsProf(final Exam e) {
        ExamAsProf.remove(e);
    }

    /**
     * Gibt die Liste von JoinExam für den Nutzer zurück.
     * @return participation als JoinExam Liste
     */
    public List<JoinExam> getParticipation() {
        return participation;
    }

    /**
     * Setzt die Liste von JoinExams für den Nutzer
     * @param participation als Liste von JoinExams
     */
    public void setParticipation(List<JoinExam> participation) {
        this.participation = participation;
    }

    /**
     * Fügt die übergebene JoinExam hinzu.
     * @param p, die zu übergebende JoinExam
     */
    public void addToParticipation(final JoinExam p) {
        participation.add(p);
    }

    /**
     * Entfernt die übergebene JoinExam.
     * @param p, die zu übergebende JoinExam
     */
    public void removeParticipation(final JoinExam p) {
        participation.remove(p);
    }
}
