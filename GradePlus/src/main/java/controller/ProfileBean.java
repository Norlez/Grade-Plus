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
package controller;

import static common.util.Assertion.assertNotEmpty;
import static common.util.Assertion.assertNotNull;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.NoneScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import businesslogic.Math;
import common.exception.DuplicateEmailException;
import common.exception.DuplicateUniqueFieldException;
import common.exception.DuplicateUsernameException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.*;
import persistence.JoinExamDAO;
import persistence.UserDAO;
import common.util.Assertion;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Dieses Bean ist für die Verwaltung der Profil-Einstellungen des Benutzers zuständig.
 *
 * @author Sebastian Offermann, Karsten Hölscher, Marcel Steinbeck
 * @version 2017-06-28
 */
@Named
@SessionScoped
public class ProfileBean extends AbstractBean implements Serializable {

    /**
     * Die eindeutige SerialisierungsID.
     */
    private static final long serialVersionUID = -6560012086857165997L;

    /**
     * Unveränderbare Map der verfügbaren Sprachen als Pärchen aus Namen der Sprache und
     * zugehörigem Locale-Objekt. Änderungsversuche führen zu einer
     * {@code UnsupportedOperationException}.
     */
    private static final Map<String, Locale> countries = calculateCountriesMap();

    /**
     * //TODO
     */
    private static Map<String, Role> roles;

    /**
     * //TODO
     */
    private String roleName;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Benutzer-Objekte
     * übernimmt.
     */
    private final UserDAO userDao;

    /**
     * Der Name des angezeigten Sprache-Objekts, dessen Attribute durch die UIKomponenten
     * des Facelets geschrieben und gelesen werden.
     */
    private String languageName;

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(ProfileBean.class);

    /**
     * Ein weiterer User..
     */
    private User thisUser;

    /**
     *
     * Überprüft, ob die Checkbox ausgewählt wurde, um die Nutzerangaben zu bearbeiten.
     */
    private boolean editChecker = false;

    private final JoinExamDAO joinExamDAO;

    /**
     * Erzeugt eine neue ProfileBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden ProfileBean.
     * @param pUserDAO
     *            Die UserDAO der zu erzeugenden ProfileBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} oder {@code pUserDAO} {@code null} ist.
     */
    @Inject
    public ProfileBean(final Session pSession, final UserDAO pUserDAO,
            final JoinExamDAO pJoinExamDAO) {
        super(pSession);
        userDao = Assertion.assertNotNull(pUserDAO);
        thisUser = getSession().getUser();
        roles = Math.calculateRoleMap();
        joinExamDAO = pJoinExamDAO;
    }

    /**
     * Gibt den ausgewählten User zurück.
     * 
     * @return der ausgewählte User.
     */
    public User getThisUser() {
        return thisUser;
    }

    /**
     * Setzt den ausgewählten User durch den übergebenen Parameter
     * 
     * @param thisUser
     *            der zu setztende User
     */
    public void setThisUser(User thisUser) {
        this.thisUser = Assertion.assertNotNull(thisUser);
    }

    /**
     * Prüft, ob die Checkbox gesetzt ist.
     *
     * @return den Zustand des Editcheckers.
     */
    public boolean getEditChecker() {
        return editChecker;
    }

    /**
     * Ändern den Wert auf das Gegenteil.
     */
    public String setEditChecker() {
        editChecker = !editChecker;
        return "profile.xhtml";
    }

    /**
     * Liefert die unveränderbare Map mit den unterstützten Sprachen zurück.
     * Änderungsversuche auf der Map führen zu einer {@code UnsupportedOperationException}
     * .
     *
     * @return Map der unterstützen Sprachen.
     */
    public Map<String, Locale> getCountries() {
        return countries;
    }

    /**
     * Gibt den Namen der anzuzeigenden Sprache zurück.
     *
     * @return Der Name der anzuzeigenden Sprache.
     */
    public String getLanguageName() {
        return getSession().getUser().getLanguage().toString();
    }

    /**
     * Setzt den Namen der anzuzeigenden Sprache.
     *
     * @param pLanguage
     *            Der Name der anzuzeigende Sprache.
     * @throws IllegalArgumentException
     *             Falls der Name der anzuzeigende Sprache den Wert {@code null} hat oder
     *             leer ist.
     */
    public void setLanguageName(final String pLanguage) {
        languageName = Assertion.assertNotEmpty(pLanguage);
    }

    /**
     * Speichert die Einstellungen aus der GUI im Nutzerprofil. Aktuell ist lediglich die
     * Sprache einstellbar.
     *
     * @throws UnexpectedUniqueViolationException
     *             Falls beim Aktualisieren des {@link User}-Objektes eine
     *             {@link DuplicateUniqueFieldException} ausgelöst wurde.
     */
    public void save() {
        if (!isLoggedIn()) {
            logger.info("Session without user tried to save profile values!");
            return;
        }
        try {
            final Locale newLanguage = getLanguageByName(languageName);
            FacesContext.getCurrentInstance().getViewRoot().setLocale(newLanguage);
            final User user = thisUser;
            user.setLanguage(newLanguage);
            try {
                // user.setEmail(thisUser.getEmail());
                // user.setGivenName(thisUser.getGivenName());
                // user.setSurname(thisUser.getSurname());
                // user.setUsername(thisUser.getUsername());
                userDao.update(user);
                setEditChecker();

            } catch (final DuplicateUniqueFieldException e) {
                throw new UnexpectedUniqueViolationException(e);
            }
            logger.info(String.format("Language of user '%s' changed to '%s'!",
                    user.getUsername(), newLanguage.toString()));
        } catch (final NoSuchElementException e) {
            addErrorMessageWithLogging(e, logger, Level.INFO, "errorUnknownLanguage");
        }
    }

    /**
     * Liefert zu dem gegebenen Namen die passende Sprache zurück.
     *
     * @param pLanguageName
     *            Name der gesuchten Sprache.
     * @return Die Sprache zu dem gegebenen Namen.
     * @throws NoSuchElementException
     *             Wenn es keine unterstütze Sprache zu dem gegebenen Namen gibt.
     * @throws IllegalArgumentException
     *             Falls der gegebene Name den Wert {@code null} hat oder leer ist.
     */
    private static Locale getLanguageByName(final String pLanguageName) {
        Assertion.assertNotEmpty(pLanguageName);
        for (final Locale theLanguage : countries.values()) {
            if (theLanguage.toString().equals(pLanguageName)) {
                return theLanguage;
            }
        }
        throw new NoSuchElementException("No language with the given name found!");
    }

    /**
     * Liefert eine einfache Map mit den unterstützen Sprachen zurück.
     *
     * @return Eine einfache Map mit unterstützten Sprachen.
     */
    private static Map<String, Locale> calculateCountriesMap() {
        final Map<String, Locale> countriesMap = new LinkedHashMap<>();
        final List<Locale> languages = new ArrayList<>();
        final Locale defaultLanguage = FacesContext.getCurrentInstance().getApplication()
                .getDefaultLocale();
        languages.add(defaultLanguage);

        final Iterator<Locale> supportedLanguages = FacesContext.getCurrentInstance()
                .getApplication().getSupportedLocales();
        supportedLanguages.forEachRemaining(languages::add);

        languages.forEach(language -> countriesMap.put(
                getTranslation(language, "propertyDisplayedLanguageName"), language));
        return Collections.unmodifiableMap(countriesMap);
    }

    public List<Exam> getExamForStudent() {
        List<JoinExam> je = joinExamDAO.getAllJoinExams();

        List<JoinExam> tmp = je.stream()
                .filter(pFilter -> pFilter.getPruefling().equals(thisUser))
                .collect(Collectors.toList());

        if (je == null) {
            return null;
        }
        ArrayList<Exam> e = new ArrayList<Exam>();
        for (JoinExam j : tmp) {
            e.add(j.getExam());
        }
        return e;


 }
    /**
     * Loggt den aktuell in der zugehörigen Session eingeloggten Benutzer aus (falls
     * jemand eingeloggt ist) und gibt den Namen des Facelets, zu dem im Erfolgsfall
     * navigiert werden soll, zurück. Wenn niemand eingeloggt ist, passiert nichts.
     *
     * @return Den Namen des Facelets, zu dem im Erfolgsfall navigiert werden soll oder
     *         {@code null} falls niemand in der zugehörigen Session eingeloggt war.
     */
    public String logout() {
        if (isLoggedIn()) {
            if (logger.isInfoEnabled()) {
                logger.info(String.format("User %s logged out.", getSession().getUser()
                        .getUsername()));
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            User registeredUser = getSession().getUser();
            registeredUser.setLoggingString( date.toString()+ ": Abmeldung vom System.\n");
            try {
                userDao.update(registeredUser);
            } catch (final IllegalArgumentException e) {
                addErrorMessageWithLogging(e, logger, Level.DEBUG,
                        getTranslation("errorUserdataIncomplete"));
            } catch (final DuplicateUsernameException e) {
                addErrorMessageWithLogging("registerUserForm:username", e, logger,
                        Level.DEBUG, "errorUsernameAlreadyInUse",registeredUser.getUsername());
            } catch (final DuplicateEmailException e) {
                addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                        "errorEmailAlreadyInUse",registeredUser.getEmail());
            }
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            return "/index.xhtml?faces-redirect=true";
        }
        return null;
    }

}
