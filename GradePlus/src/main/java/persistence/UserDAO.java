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
package persistence;

import static common.util.Assertion.assertNotEmpty;
import static common.util.Assertion.assertNotNull;
import static java.lang.String.format;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TransactionRequiredException;

import common.exception.DuplicateEmailException;
import common.exception.DuplicateUniqueFieldException;
import common.exception.DuplicateUsernameException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.JPAEntity;
import common.model.Role;
import common.model.User;
import common.util.Assertion;

/**
 * Dieses DAO verwaltet Objekte der Klasse {@link User}.
 *
 * @author Marcel Steinbeck, Karsten Hölscher, Dennis Schürholz, Sebastian Offermann,
 *         Falko Galperin, Hugo Hakim Damer
 * @version 2017-07-03
 */
@Stateless
public class UserDAO extends JPADAO<User> implements Serializable {

    /**
     * Die eindeutige ID für Serialisierung.
     */
    private static final long serialVersionUID = 1543383068333720238L;

    @Override
    Class<User> getClazz() {
        return User.class;
    }

    /**
     * Fügt {@code theUser} dem Datenbestand hinzu. Falls {@code theUser} bereits im
     * Datenbestand vorhanden ist (vgl. {@link JPADAO#save(JPAEntity)}, wird eine
     * {@link IllegalArgumentException} ausgelöst.
     * 
     * @param pUser
     *            Das zu speichernde {@link User}-Objekt.
     * @throws DuplicateUsernameException
     *             Falls der Benutzername bereits vergeben ist.
     * @throws DuplicateEmailException
     *             Falls die E-Mail-Adresse bereits vergeben ist.
     * @throws UnexpectedUniqueViolationException
     *             Falls der Aufruf der Oberklassenmethode unerwarteterweise eine
     *             {@link DuplicateUniqueFieldException} ausgelöst hat.
     * @throws IllegalArgumentException
     *             Falls {@code theUser == null}, {@code theUser.getId() != null},
     *             {@code theUser.getUsername() == null},
     *             {@code theUser.getEmail() == null} oder {@code theUser} kein durch JPA
     *             verwaltetes Objekt ist.
     * @throws TransactionRequiredException
     *             Falls zum Zeitpunkt des Aufrufs keine gültige Transaktion vorliegt
     *             (vgl. {@link javax.persistence.EntityManager#persist(Object)}).
     */
    @Override
    public synchronized void save(final User pUser) throws DuplicateUsernameException,
            DuplicateEmailException {
        Assertion.assertNotNull(pUser);
        final String userName = Assertion.assertNotNull(pUser.getUsername(),
                "The username of the parameter must not be null!");
        final String userEmail = Assertion.assertNotNull(pUser.getEmail(),
                "The email of the parameter must not be null!");

        final User userByName = getUserForUsername(userName);
        if (userByName != null) {
            throw new DuplicateUsernameException(format(
                    "Username '%s' is already in use", userName));
        }
        final User userByEmail = getUserForEmail(userEmail);
        if (userByEmail != null) {
            throw new DuplicateEmailException(format("Email '%s' is already in use",
                    userEmail));
        }
        try {
            super.save(pUser);
        } catch (final DuplicateUniqueFieldException e) {
            throw new UnexpectedUniqueViolationException(e);
        }
    }

    /**
     * Aktualisiert den Eintrag von {@code theUser} im Datenbestand. Falls {@code theUser}
     * noch nicht im Datenbestand vorhanden ist, wird eine
     * {@link IllegalArgumentException} ausgelöst.
     *
     * @param pUser
     *            Das zu aktualisierende {@link User}-Objekt.
     * @throws DuplicateUsernameException
     *             Falls der zu aktualisierende Benutzername bereits an ein anderes Objekt
     *             vergeben ist.
     * @throws DuplicateEmailException
     *             Falls die zu aktualisierende E-Mail-Adresse bereits an ein anderes
     *             Objekt vergeben ist.
     * @throws UnexpectedUniqueViolationException
     *             Falls der Aufruf der Oberklassenmethode unerwarteterweise eine
     *             {@link DuplicateUniqueFieldException} ausgelöst hat.
     * @throws IllegalArgumentException
     *             Falls {@code theUser == null}, {@code theUser.getId() == null}, es noch
     *             keinen Eintrag für {@code theUser} im Datenbestand gibt,
     *             {@code theUser.getUsername() == null},
     *             {@code theUser.getEmail() == null} oder {@code theUser} kein durch JPA
     *             verwaltetes Objekt ist.
     * @throws TransactionRequiredException
     *             Falls zum Zeitpunkt des Aufrufs keine gültige Transaktion vorliegt
     *             (vlg. {@link javax.persistence.EntityManager#merge(Object)}).
     */
    @Override
    public synchronized void update(final User pUser) throws DuplicateUsernameException,
            DuplicateEmailException {
        Assertion.assertNotNull(pUser);
        final Long userId = Assertion.assertNotNull(pUser.getId(),
                "The id of the parameter must not be null!");
        Assertion.assertNotNull(getById(userId), "The parameter is not yet registered!");
        final String userName = Assertion.assertNotNull(pUser.getUsername(),
                "The username of the parameter must not be null!");
        final String userEmail = Assertion.assertNotNull(pUser.getEmail(),
                "The email of the parameter must not be null!");

        final User userByName = getUserForUsername(userName);
        if (userByName != null && !userByName.getId().equals(userId)) {
            throw new DuplicateUsernameException(format(
                    "Username '%s' is already in use", userName));
        }
        final User userByEmail = getUserForEmail(userEmail);
        if (userByEmail != null && !userByEmail.getId().equals(userId)) {
            throw new DuplicateEmailException(format("Email '%s' is already in use",
                    userEmail));
        }
        try {
            super.update(pUser);
        } catch (final DuplicateUniqueFieldException e) {
            throw new UnexpectedUniqueViolationException(e);
        }
    }

    /**
     * Gibt eine Liste mit allen innerhalb der Applikation bekannten Benutzern zurück.
     *
     * @return Liste mit allen innerhalb der Applikation bekannten Benutzern.
     */
    public List<User> getAllUsers() {
        return getEm().createNamedQuery("User.findAll", getClazz()).getResultList();
    }

    /**
     * Gibt den Benutzer mit dem gegebenen Benutzernamen zurück. Falls es keinen Benutzer
     * mit dem gegebenen Benutzernamen gibt, wird {@code null} zurückgegeben.
     * 
     * @param pUsername
     *            der Benutzername des gesuchten Benutzers
     * @return Den Benutzer zum gegebenen Benutzernamen oder {@code null} falls es keinen
     *         solchen Benutzer gibt.
     * @throws IllegalArgumentException
     *             Falls der gegebene Benutzername leer ist oder den Wert {@code null}
     *             hat.
     */
    public User getUserForUsername(final String pUsername) {
        Assertion.assertNotEmpty(pUsername);
        final List<User> users = getEm()
                .createNamedQuery("User.findByUsername", getClazz())
                .setParameter("username", pUsername).getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    /**
     * Gibt den Benutzer mit der gegebenen E-Mail-Adresse zurück. Falls es keinen Benutzer
     * mit der gegebenen E-Mail-Adresse gibt, wird {@code null} zurückgegeben.
     *
     * @param pEmail
     *            Die E-Mail-Adresse des gesuchten Benutzers.
     * @return Der Benutzer zu der gegebenen E-Mail-Adresse oder {@code null}, falls es
     *         keinen solchen Benutzer gibt.
     * @throws IllegalArgumentException
     *             Falls die gegebene E-Mail-Adresse leer ist oder den Wert {@code null}
     *             hat.
     */
    public User getUserForEmail(final String pEmail) {
        Assertion.assertNotEmpty(pEmail);
        final List<User> users = getEm().createNamedQuery("User.findByEmail", getClazz())
                .setParameter("email", pEmail).getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    /**
     * Gibt eine Liste von Usern mit allen Studenten zurück.
     *
     * @return Studentenliste
     */
    public List<User> getAllStudents() {
        return getEm().createNamedQuery("User.findByRole", getClazz())
                .setParameter("role", Role.STUDENT).getResultList();
    }

    /**
     * Gibt eine Liste von Usern mit allen Admins zurück.
     *
     * @return Adminliste
     */
    public List<User> getAllAdmins() {
        return getEm().createNamedQuery("User.findByRole", getClazz())
                .setParameter("role", Role.ADMIN).getResultList();
    }

    /**
     * Gibt eine Liste von Usern mit allen Prüfern zurück.
     *
     * @return Prüferliste
     */
    public List<User> getAllExaminer() {
        return getEm().createNamedQuery("User.findByRole", getClazz())
                .setParameter("role", Role.EXAMINER).getResultList();
    }
}
