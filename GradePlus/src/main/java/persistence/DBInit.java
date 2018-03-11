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
import common.model.Exam;
import common.model.InstanceLecture;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Initialisiert den Datenbestand bei Start der Webapplikation.
 * 
 * @author Karsten Hölscher, Sebastian Offermann, Marvin Kampen
 * @version 2018-03-11
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

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Prüfungs-Objekte
     * übernimmt.
     */
    @Inject
    private ExamDAO examDAO;

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
            final User student = new User();
            final Lecture lecture = new Lecture();
            // final Exam exam = new Exam();
            user.setUsername(DEFAULT_USER_NAME); // Der Admin
            user.setEmail(DEFAULT_USER_EMAIL);
            user.setPassword(DEFAULT_USER_PASSWORD);
            user.setGivenName("Putin");
            user.setSurname("Vladi");
            user.setRole(Role.ADMIN);
            pUser.setUsername("user"); // Der Prüfer
            pUser.setEmail("user@offline.de");
            pUser.setPassword("123");
            pUser.setGivenName("Boris");
            pUser.setSurname("Jelzin");
            pUser.setRole(Role.EXAMINER);
            student.setUsername("student"); // Der Student
            student.setGivenName("Horst");
            student.setSurname("Hubert");
            student.setRole(Role.STUDENT);
            student.setMatrNr("1415352");
            student.setPassword("123");
            student.setEmail("horstIsOffline@offline.de");
            lecture.setVak("2035252-2"); // Die Lehrveranstaltung
            lecture.setEcts(9);
            lecture.setName("SWP-2");
            // exam.setInstanceLecture(instanceLecture);
            // exam.setLocalDateTime(LocalDateTime.of(2019, 10, 2, 4, 25));

            // VON TORBEN ZUM TESTEN
            final List<User> students = new ArrayList<>();
            final List<User> examiners = new ArrayList<>();
            final List<User> admins = new ArrayList<>();

            for (int i = 1; i <= 9; i++) {
                final User s = new User();
                s.setUsername("s" + i);
                s.setEmail("s" + i + "@offline.de");
                s.setMatrNr("4" + i + i + i + i + i + i);
                s.setRole(Role.STUDENT);
                s.setPassword("s" + i);
                students.add(s);
            }
            students.get(0).setGivenName("Sabine");
            students.get(0).setSurname("Einsel");
            students.get(1).setGivenName("Samuel");
            students.get(1).setSurname("Zweierlei");
            students.get(2).setGivenName("Simon");
            students.get(2).setSurname("Dreie");
            students.get(3).setGivenName("Siegfried");
            students.get(3).setSurname("Vierman");
            students.get(4).setGivenName("Sven");
            students.get(4).setSurname("Fünffe");
            students.get(5).setGivenName("Stefan");
            students.get(5).setSurname("Sechsmer");
            students.get(6).setGivenName("Saman");
            students.get(6).setSurname("Siebenne");
            students.get(7).setGivenName("Sarah");
            students.get(7).setSurname("Achtel");
            students.get(8).setGivenName("Sara");
            students.get(8).setSurname("NeunzigerJahreMusik");
            for (int i = 1; i <= 5; i++) {
                final User e = new User();
                e.setUsername("e" + i);
                e.setEmail("e" + i + "@offline.de");
                e.setRole(Role.EXAMINER);
                e.setPassword("e" + i);
                examiners.add(e);
            }
            examiners.get(0).setGivenName("Emil");
            examiners.get(0).setSurname("Einsel");
            examiners.get(1).setGivenName("Eberhart");
            examiners.get(1).setSurname("Zweierlei");
            examiners.get(2).setGivenName("Emily");
            examiners.get(2).setSurname("Dreie");
            examiners.get(3).setGivenName("Eckerhardt");
            examiners.get(3).setSurname("Vierman");
            examiners.get(4).setGivenName("Erik");
            examiners.get(4).setSurname("Fünffe");
            for (int i = 1; i <= 2; i++) {
                final User e = new User();
                e.setUsername("a" + i);
                e.setEmail("a" + i + "@offline.de");
                e.setRole(Role.ADMIN);
                e.setPassword("a" + i);
                admins.add(e);
            }
            admins.get(0).setGivenName("Amelie");
            admins.get(0).setSurname("Einsel");
            admins.get(1).setGivenName("Arbnor");
            admins.get(1).setSurname("Zweierlei");
            // BIS HIER

            try {
                userDAO.save(user);
                userDAO.save(pUser);
                userDAO.save(student);

                // VON TORBEN ZUM TESTEN
                for (User s : students) {
                    userDAO.save(s);
                }
                for (User e : examiners) {
                    userDAO.save(e);
                }
                for (User a : admins) {
                    userDAO.save(a);
                }
                // BIS HIER

                lectureDAO.save(lecture);
                // examDAO.save(exam);
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
            }
        }
    }

}
