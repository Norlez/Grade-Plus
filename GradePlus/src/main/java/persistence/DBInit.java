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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import common.exception.*;
import common.model.Role;
import common.model.User;
import common.model.Lecture;
import common.model.InstanceLecture;
import org.apache.log4j.Logger;

/**
 * Initialisiert den Datenbestand bei Start der Webapplikation.
 * 
 * @author Karsten Hölscher, Sebastian Offermann
 * @version 2016-07-25
 */
@ApplicationScoped
public class DBInit {

    /**
     * Name des Standard-Benutzers.
     */
    private static final String DEFAULT_USER_NAME = "admin";

    /**
     * Email-Adresse des Standard-Benutzers.
     */
    private static final String DEFAULT_USER_EMAIL = "admin@offline.de";

    /**
     * Passwort des Standard-Benutzers.
     * 
     * Hinweis: Dies dient nur einer Demonstration. In einer echten Applikation darf man
     * kein Passwort im Quelltext hardcoden!
     */
    private static final String DEFAULT_USER_PASSWORD = "123";

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(DBInit.class);

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Benutzer-Objekte
     * übernimmt.
     */
    @Inject
    private UserDAO userDAO;
    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für
     * Lehrveranstaltungs-Objekte übernimmt.
     */
    @Inject
    private LectureDAO lectureDAO;

    @Inject
    private InstanceLectureDAO instanceLectureDAO;

    /**
     * Trägt einen Standard-Benutzer in den Datenbestand ein, falls noch keine Benutzer
     * existieren. Wird beim Starten der Webanwendung ausgeführt.
     * 
     * Der Standard-Benutzer hat den Benutzernamen {@code admin}, die Email-Adresse
     * {@code admin@offline.de} und das Passwort {@code 123}.
     * 
     * @param context
     *            Der Kontext der Webapplikation.
     * @throws UnexpectedUniqueViolationException
     *             Falls beim Erstellen des Standard-Benutzers ein Fehler auftritt.
     */
    public void init(
            @Observes @Initialized(ApplicationScoped.class) ServletContext context) {
        if (userDAO.getAllUsers().isEmpty()) {
            final User user = new User();
            final User pUser = new User();
            final Lecture lecture = new Lecture();
            final InstanceLecture instanceLecture = new InstanceLecture();
            user.setUsername(DEFAULT_USER_NAME);
            user.setEmail(DEFAULT_USER_EMAIL);
            user.setPassword(DEFAULT_USER_PASSWORD);
            user.setGivenName("Putin");
            user.setSurname("Vladi");
            user.setRole(Role.ADMIN);
            pUser.setUsername("user");
            pUser.setEmail("user@offline.de");
            pUser.setPassword("123");
            pUser.setGivenName("Boris");
            pUser.setSurname("Jelzin");
            pUser.setRole(Role.EXAMINER);
            lecture.setVak("2035252-2");
            lecture.setEcts(9);
            lecture.setName("SWP-2");
            instanceLecture.setLecture(lecture);
            instanceLecture.addExaminer(pUser);
            instanceLecture.setYear("2018");
            instanceLecture.setSemester("WiSe");

            try {
                userDAO.save(user);
                userDAO.save(pUser);
                lectureDAO.save(lecture);
                instanceLectureDAO.save(instanceLecture);
            } catch (final DuplicateUsernameException ex) {
                logger.fatal(
                        String.format(
                                "Weird Error: Although there are no users, a user with the default name '%s' seems to exist.",
                                DEFAULT_USER_NAME), ex);
                throw new UnexpectedUniqueViolationException(ex);
            } catch (final DuplicateEmailException ex) {
                logger.fatal(
                        String.format(
                                "Weird Error: Although there are no users, a user with the default email address '%s' seems to exist.",
                                DEFAULT_USER_EMAIL), ex);
                throw new UnexpectedUniqueViolationException(ex);
            } catch (final DuplicateVakException ex) {
                throw new UnexpectedUniqueViolationException(ex);
            } catch (final DuplicateInstanceLectureException ex) {
                throw new UnexpectedUniqueViolationException(ex);
            }
        }
    }

}
