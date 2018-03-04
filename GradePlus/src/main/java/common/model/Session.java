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

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import persistence.SessionDAO;
import persistence.UserDAO;

/**
 * Diese Klasse repräsentiert die Session eines Benutzers bzw. einer Sitzung vor dem
 * Login. Sie hat daher {@link SessionScoped}. Da Objekte dieser Klasse vom Container
 * durch Serialisierung passiviert und bei Bedarf wieder aktiviert werden können, sind sie
 * serialisierbar.
 *
 * Objekte dieser Klasse werden nicht über die Laufzeit der Applikation hinaus
 * persistiert!
 *
 * In der init-Methode, die nach der Erzeugung eines Objektes dieser Klasse aufgerufen
 * wird, trägt sich jedes solche Objekt in der Liste der Sessions der {@link SessionDAO}
 * ein. Bevor es zerstört wird, wird es durch die destroy-Methode wieder aus dieser Liste
 * entfernt.
 *
 * @author Karsten Hölscher, Marcel Steinbeck, Sebastian Offermann
 * @version 2016-07-19
 */
@SessionScoped
public class Session implements Serializable {

    /**
     * Die eindeutige ID für Serialisierung.
     */
    private static final long serialVersionUID = 3828357750697477354L;

    /**
     * Verwaltet die bekannten Benutzer.
     */
    @Inject
    private UserDAO userDAO;

    /**
     * Verwaltet die Sessions.
     */
    @Inject
    private SessionDAO sessionDAO;

    /**
     * Speichert den Zeitpunkt, an dem sich ein Benutzer in diese Session eingeloggt hat.
     */
    private LocalDateTime loginTime;

    /**
     * Speichert eine Vorlesung
     */
    private Lecture selectedLecture;

    /**
     * Speichert eine Instanz der Vorlesung.
     */
    private InstanceLecture selectedILV;


    public String getTmpPassword() {
        return tmpPassword;
    }

    public void setTmpPassword(String tmpPassword) {
        this.tmpPassword = tmpPassword;
    }

    /**
     * temporäres Passwort.
     */
    private String tmpPassword;

    /**
     * Die Id des innerhalb dieser Session eingeloggten {@link User}s. Falls in dieser
     * Session noch niemand eingeloggt ist, ist der Wert {@code null}.
     *
     * Hinweis: Man könnte hier auch das User-Objekt direkt speichern und bei jedem Aufruf
     * von {@link #getUser()} aktualisieren. Da die Aktualisierung jedoch auch nur mit der
     * Id des Objektes arbeiten würde, macht es keinen Unterschied. Ferner muss bei dieser
     * Lösung die {@link User}-Klasse nicht serialisierbar sein.
     */
    private Long userId;

    /**
     * Nach Erzeugung eines Objektes dieser Klasse trägt sich dieses Objekt in die Liste
     * aller Sessions der {@link SessionDAO} ein.
     */
    @PostConstruct
    public void init() {
        sessionDAO.save(this);
    }

    /**
     * Kurz vor der Zerstörung dieses Objektes trägt es sich noch aus der Liste aller
     * Sessions der {@link SessionDAO} aus.
     */
    @PreDestroy
    public void destroy() {
        sessionDAO.remove(this);
    }

    /**
     * Setzt den Zeitpunkt, an dem sich der aktuelle Benutzer in diese Session eingeloggt
     * hat, auf den gegebenen Zeitpunkt. Der Wert wird (außer einer Prüfung auf
     * {@code null}) nicht auf Plausibilität geprüft, kann also in der Zukunft oder auch
     * sehr weit in der Vergangenheit liegen.
     *
     * @param pLoginTime
     *            Der neue Login-Zeitpunkt des aktuellen Benutzers.
     * @throws IllegalArgumentException
     *             Falls der gegebene Zeitpunkt den Wert {@code null} hat.
     */
    public void setLoginTime(final LocalDateTime pLoginTime) {
        loginTime = assertNotNull(pLoginTime);
    }

    /**
     * Gibt den Login-Zeitpunkt dieser Session zurück.
     *
     * @return Den Login-Zeitpunkt dieser Session.
     */
    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    /**
     * Gibt den innerhalb dieser Session eingeloggten {@link User} zurück. Ist niemand
     * eingeloggt, so wird {@code null} zurückgegeben.
     *
     * @return Der innerhalb dieser Session eingeloggte {@link User} oder {@code null}.
     */
    public User getUser() {
        return userId == null ? null : userDAO.getById(userId);
    }

    /**
     * Setzt den innerhalb dieser Session eingeloggten {@link User} auf {@code theUser}.
     * Kann {@code null} sein, um einen {@link User} aus einer Session zu entfernen.
     *
     * @param pUser
     *            Der innerhalb dieser Session eingeloggte {@link User}. Kann {@code null}
     *            sein.
     */
    public void setUser(final User pUser) {
        userId = pUser == null ? null : pUser.getId();
    }

    /**
     * Gibt an, ob in der zugehörigen Session aktuell jemand eingeloggt ist oder nicht.
     *
     * @return {@code true} Falls in der zugehörigen Session jemand eingeloggt ist, sonst
     *         {@code false}.
     */
    public boolean isLoggedIn() {
        return userId != null;
    }

    /**
     * Gibt die Lehrveranstaltung zurück
     * 
     * @return Lehrveranstaltung
     */
    public Lecture getSelectedLecture() {
        return selectedLecture;
    }

    /**
     * Setzt eine Lehrveranstaltung.
     * 
     * @param selectedLecture
     *            , die gesetzt werden soll.
     */
    public void setSelectedLecture(Lecture selectedLecture) {
        this.selectedLecture = selectedLecture;
    }

    /**
     * Erhält die gespeicherte Instanz der Lehrveranstaltung.
     * 
     * @return ILV
     */
    public InstanceLecture getSelectedILV() {
        return selectedILV;
    }

    /**
     * Speichert eine Instanz der Lehrveranstalung
     * 
     * @param pSelectedILV
     *            , die gespeichert werden soll
     */
    public void setSelectedILV(InstanceLecture pSelectedILV) {
        selectedILV = pSelectedILV;
    }
}
