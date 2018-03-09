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

import static common.util.Assertion.assertNotNull;
import static common.util.Assertion.assertWithoutNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.model.Session;
import common.util.Assertion;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import common.model.User;

/**
 * Abstrakte Superbean, die die zu dem Request gehörige Session zentral für ihre
 * Unterklassen bereitstellt. Ist also eine Backing Bean (und damit Controller im
 * MVC-Muster) für alle Facelets.
 *
 * @author Karsten Hölscher, Marcel Steinbeck, Sebastian Offermann
 * @version 2017-06-28
 */
public abstract class AbstractBean {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(AbstractBean.class);

    /**
     * Enthält die aktuelle Session.
     */
    private final Session session;

    /**
     * Erzeugt eine neue AbstractBean.
     * 
     * @param theSession
     *            Die Session der zu erzeugenden AbstractBean.
     * @throws IllegalArgumentException
     *             Falls {@code theSession} {@code null} ist.
     */
    @Inject
    public AbstractBean(final Session theSession) {
        session = Assertion.assertNotNull(theSession);
    }

    /**
     * Gibt an, ob in der zugehörigen Session aktuell jemand eingeloggt ist oder nicht.
     *
     * @return {@code true} Falls in der zugehörigen Session jemand eingeloggt ist, sonst
     *         {@code false}.
     */
    public boolean isLoggedIn() {
        return session.isLoggedIn();
    }

    /**
     * Gibt die zugehörige Session zurück.
     *
     * @return Die zugehörige Session.
     */
    protected Session getSession() {
        return session;
    }

    /**
     * Setzt den zu {@link #getSession()} zugehörigen Benutzer.
     *
     * @param pUser
     *            Der neu eingeloggte Benutzer für die zu {@link #getSession()} zugehörige
     *            Session.
     * @throws IllegalArgumentException
     *             Falls das übergebene Benutzer-Objekt den Wert {@code null} hat.
     */
    public void setUser(final User pUser) {
        session.setUser(Assertion.assertNotNull(pUser));
    }



    /**
     * Meldet die Fehlermeldung an das gegebene Ziel im gegebenen Kontext.
     * 
     * @param targetClientId
     *            ClientId der UI-Komponente, die die Nachricht anzeigen soll.
     * @param formatText
     *            Die Fehlernachricht als Format-String.
     * @param formatParameters
     *            Format-Parameter für die Fehlernachricht.
     * @throws IllegalArgumentException
     *             falls {@code formatText == null} oder {@code formatParameters == null}
     *             ist oder einer der Format-Parameter {@code null} ist.
     * @throws IllegalFormatException
     *             Wenn der Format-String ein unzulässiges oder nicht zu den Parametern
     *             passendes Format aufweist.
     */
    void addErrorMessage(final String targetClientId, final String formatText,
            final Object... formatParameters) {
        final String message = getTranslation(Assertion.assertNotNull(formatText),
                Assertion.assertWithoutNull(formatParameters));
        final FacesContext context = FacesContext.getCurrentInstance();
        final FacesMessage facesMessage = new FacesMessage(message);
        context.addMessage(targetClientId, facesMessage);
    }

    /**
     * Meldet die Fehlermeldung an das gegebene Ziel im gegebenen Kontext.
     * 
     * @param target
     *            UI-Komponente, die die Nachricht anzeigen soll.
     * @param formatText
     *            Die Fehlernachricht als Format-String.
     * @param formatParameters
     *            Format-Parameter für die Fehlernachricht.
     * @throws IllegalArgumentException
     *             falls {@code formatText == null} oder {@code formatParameters == null}
     *             ist oder einer der Format-Parameter {@code null} ist.
     * @throws IllegalFormatException
     *             Wenn der Format-String ein unzulässiges oder nicht zu den Parametern
     *             passendes Format aufweist.
     */
    void addErrorMessage(final UIComponent target, final String formatText,
            final Object... formatParameters) {
        final String targetClientId = target == null ? null : target
                .getClientId(FacesContext.getCurrentInstance());
        addErrorMessage(targetClientId, formatText, formatParameters);
    }

    /**
     * Meldet die Fehlermeldung an den Kontext weiter.
     * 
     * @param formatText
     *            Die Fehlernachricht als Format-String.
     * @param formatParameters
     *            Format-Parameter für die Fehlernachricht.
     * @throws IllegalArgumentException
     *             falls {@code formatText == null} oder {@code formatParameters == null}
     *             ist oder einer der Format-Parameter {@code null} ist.
     * @throws IllegalFormatException
     *             Wenn der Format-String ein unzulässiges oder nicht zu den Parametern
     *             passendes Format aufweist.
     */
    void addErrorMessage(final String formatText, final Object... formatParameters) {
        addErrorMessage((String) null, formatText, formatParameters);
    }

    /**
     * Meldet die Fehlermeldung an das gegebene Ziel im gegebenen Kontext weiter und
     * protokolliert die Meldung inklusive der aufgetretenen Exception mit gegegeben
     * Logging-Priorität, sofern diese aktiv ist.
     * 
     * @param targetClientId
     *            ClientId der UI-Komponente, die die Nachricht anzeigen soll.
     * @param e
     *            Die aufgetretene Exception.
     * @param theLogger
     *            Logger, in welchem die die Fehlernachricht geloggt werden soll.
     * @param level
     *            Das Logging-Level.
     * @param formatText
     *            Die Fehlernachricht als Format-String.
     * @param formatParameters
     *            Format-Parameter für die Fehlernachricht.
     * @throws IllegalArgumentException
     *             falls {@code priority == null} oder {@code formatText == null} oder
     *             {@code formatParameters == null} ist oder einer der Format-Parameter
     *             {@code null} ist.
     * @throws IllegalFormatException
     *             Wenn der Format-String ein unzulässiges oder nicht zu den Parametern
     *             passendes Format aufweist.
     */
    void addErrorMessageWithLogging(final String targetClientId, final Exception e,
            final Logger theLogger, final Level level, final String formatText,
            final Object... formatParameters) {
        addErrorMessage(targetClientId, Assertion.assertNotNull(formatText),
                Assertion.assertWithoutNull(formatParameters));
        if (theLogger.isEnabledFor(Assertion.assertNotNull(level))) {
            final String logMessage = getLogTranslation(formatText, formatParameters)
                    + String.format(" (key: '%s')", formatText);
            if (e == null) {
                theLogger.log(level, logMessage);
            } else {
                theLogger.log(level, logMessage, e);
            }
        }
    }

    /**
     * Meldet die Fehlermeldung an den Kontext weiter und protokolliert die Meldung
     * inklusive der aufgetretenen Exception mit gegegeben Logging-Priorität, sofern diese
     * aktiv ist.
     * 
     * @param e
     *            Die aufgetretene Exception.
     * @param theLogger
     *            Logger, in welchem die die Fehlernachricht geloggt werden soll.
     * @param level
     *            Das Logging-Level.
     * @param formatText
     *            Die Fehlernachricht als Format-String.
     * @param formatParameters
     *            Format-Parameter für die Fehlernachricht.
     * @throws IllegalArgumentException
     *             falls {@code priority == null} oder {@code formatText == null} oder
     *             {@code formatParameters == null} ist oder einer der Format-Parameter
     *             {@code null} ist.
     * @throws IllegalFormatException
     *             Wenn der Format-String ein unzulässiges oder nicht zu den Parametern
     *             passendes Format aufweist.
     */
    void addErrorMessageWithLogging(final Exception e, final Logger theLogger,
            final Level level, final String formatText, final Object... formatParameters) {
        addErrorMessageWithLogging(null, e, theLogger, level, formatText,
                formatParameters);
    }

    /**
     * Liefert die aktuelle Sprache zurück. Dies ist die Sprache des aktuellen Benutzers,
     * sofern einer eingeloggt ist. Ansonsten ist es die vom Client angeforderte Sprache,
     * sofern vorhanden. Sollte auch diese nicht zur Verfügung stehen, ist es die
     * Standardsprache der Applikation.
     * 
     * @return Aktuelle Sprache.
     */
    public Locale getLanguage() {
        if (session != null) {
            final User user = session.getUser();
            if (user != null) {
                final Locale language = user.getLanguage();
                logger.debug(String.format("Current language is acquired from user: %s",
                        language.toString()));
                return language;
            }
        }
        final FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            final UIViewRoot root = context.getViewRoot();
            final Locale clientLanguage = root == null ? context.getApplication()
                    .getViewHandler().calculateLocale(context) : root.getLocale();
            if (clientLanguage != null) {
                logger.debug(String.format(
                        "Current language is acquired from client: %s", clientLanguage));
                return clientLanguage;
            }
        }
        final Locale defaultLanguage = User.getDefaultLanguage();
        logger.debug(String.format(
                "Current language is acquired from application default: %s",
                defaultLanguage));
        return defaultLanguage;
    }

    /**
     * Liefert den Ausdruck zum gegebenen Schlüssel in der aktuellen Sprache zurück. Ist
     * für den gegebenen Schlüssel in der aktuellen Sprache kein Ausdruck vorhanden, wird
     * eine Standardzeichenkette inklusive des gegebenen Schlüssels zurückgegeben.
     * 
     * @param messageKey
     *            Der Schlüssel des Ausdrucks, welcher in den
     *            Internationalisierungs-Dateien verwendet wird.
     * @param formatParameters
     *            Format-Parameter zum Einfügen in die übersetzte Nachricht.
     * @return Der Ausdruck, der dem gegebenen Schlüssel in der aktuellen Sprache
     *         zugeordnet ist.
     * @throws IllegalArgumentException
     *             falls {@code messageKey == null} oder {@code formatParameters == null}
     *             ist oder einer der Format-Parameter {@code null} ist.
     * @throws IllegalFormatException
     *             Wenn der Format-String ein unzulässiges oder nicht zu den Parametern
     *             passendes Format aufweist.
     */
    public String getTranslation(final String messageKey,
            final Object... formatParameters) {
        return getTranslation(getLanguage(), Assertion.assertNotNull(messageKey),
                Assertion.assertWithoutNull(formatParameters));
    }

    /**
     * Liefert den Ausdruck zum gegebenen Schlüssel in der Logging-Sprache zurück. Ist für
     * den gegebenen Schlüssel in der Logging-Sprache kein Ausdruck vorhanden, wird eine
     * Standardzeichenkette inklusive des gegebenen Schlüssels zurückgegeben.
     * 
     * @param messageKey
     *            Der Schlüssel des Ausdrucks, welcher in den
     *            Internationalisierungs-Dateien verwendet wird.
     * @param formatParameters
     *            Format-Parameter zum Einfügen in die übersetzte Nachricht.
     * @return Der Ausdruck, der dem gegebenen Schlüssel in der Logging-Sprache zugeordnet
     *         ist.
     * @throws IllegalArgumentException
     *             falls {@code messageKey == null} oder {@code formatParameters == null}
     *             ist oder einer der Format-Parameter {@code null} ist.
     * @throws IllegalFormatException
     *             Wenn der Format-String ein unzulässiges oder nicht zu den Parametern
     *             passendes Format aufweist.
     */
    public String getLogTranslation(final String messageKey,
            final Object... formatParameters) {
        // Übung: Log-Sprache in Konfiguration setzen
        return getTranslation(Locale.ENGLISH, Assertion.assertNotNull(messageKey),
                Assertion.assertWithoutNull(formatParameters));
    }

    /**
     * Liefert den Ausdruck zum gegebenen Schlüssel in der gegebenen Sprache zurück. Ist
     * für den gegebenen Schlüssel in der gegebenen Sprache kein Ausdruck vorhanden, wird
     * eine Standardzeichenkette inklusive des gegebenen Schlüssels zurückgegeben.
     *
     * @param locale
     *            Die gewünschte Sprache, in welche übersetzt werden soll.
     * @param messageKey
     *            Der Schlüssel des Ausdrucks, welcher in den
     *            Internationalisierungs-Dateien verwendet wird.
     * @param formatParameters
     *            Format-Parameter zum Einfügen in die übersetzte Nachricht.
     * @return Der Ausdruck, der dem gegebenen Schlüssel in der gegebenen Sprache
     *         zugeordnet ist.
     * @throws IllegalArgumentException
     *             falls {@code locale == null} oder {@code messageKey == null} oder
     *             {@code formatParameters == null} ist oder einer der Format-Parameter
     *             {@code null} ist oder {@code logger == null} und eine Problemmeldung
     *             anfällt.
     * @throws IllegalFormatException
     *             Wenn der Format-String ein unzulässiges oder nicht zu den Parametern
     *             passendes Format aufweist.
     */
    public static String getTranslation(final Locale locale, final String messageKey,
            final Object... formatParameters) {
        try {
            final ResourceBundle bundle = ResourceBundle.getBundle(
                    "internationalization.messages", Assertion.assertNotNull(locale));
            return getTranslation(bundle, Assertion.assertNotNull(messageKey),
                    Assertion.assertNotNull(formatParameters));
        } catch (final MissingResourceException e) {
            Assertion
                    .assertNotNull(logger)
                    .error(String.format(
                            "Severe internationalization error: Internationalization bundle for locale '%s' not found!",
                            locale.toString()), e);
        }
        return String.format("No message found for key '%s'!", messageKey);
    }

    /**
     * Liefert den Ausdruck zum gegebenen Schlüssel in der gegebenen Sprache zurück. Ist
     * für den gegebenen Schlüssel in der gegebenen Sprache kein Ausdruck vorhanden, wird
     * eine Standardzeichenkette inklusive des gegebenen Schlüssels zurückgegeben.
     *
     * @param messageKey
     *            Der Schlüssel des Ausdrucks, welcher in den
     *            Internationalisierungs-Dateien verwendet wird.
     * @param formatParameters
     *            Format-Parameter zum Einfügen in die übersetzte Nachricht.
     * @return Der Ausdruck, der dem gegebenen Schlüssel in der gegebenen Sprache
     *         zugeordnet ist.
     * @throws IllegalArgumentException
     *             falls {@code locale == null} oder {@code messageKey == null} oder
     *             {@code formatParameters == null} ist oder einer der Format-Parameter
     *             {@code null} ist oder {@code logger == null} und eine Problemmeldung
     *             anfällt.
     * @throws IllegalFormatException
     *             Wenn der Format-String ein unzulässiges oder nicht zu den Parametern
     *             passendes Format aufweist.
     */
    private static String getTranslation(final ResourceBundle bundle,
            final String messageKey, final Object... formatParameters) {
        try {
            final String message = Assertion.assertNotNull(bundle).getString(
                    Assertion.assertNotNull(messageKey));
            return String.format(message, Assertion.assertWithoutNull(formatParameters));
        } catch (final MissingResourceException e) {
            return resolveTranslationError(bundle, messageKey, e);
        }
    }

    /**
     * Liefert den Ausdruck zum gegebenen Schlüssel in der gegebenen Sprache zurück. Ist
     * für den gegebenen Schlüssel in der gegebenen Sprache kein Ausdruck vorhanden, wird
     * eine Standardzeichenkette inklusive des gegebenen Schlüssels zurückgegeben.
     *
     * @param messageKey
     *            Der Schlüssel des Ausdrucks, welcher in den
     *            Internationalisierungs-Dateien verwendet wird.
     * @return Der Ausdruck, der dem gegebenen Schlüssel in der gegebenen Sprache
     *         zugeordnet ist.
     * @throws IllegalArgumentException
     *             falls {@code locale == null} oder {@code messageKey == null} oder
     *             {@code formatParameters == null} ist oder einer der Format-Parameter
     *             {@code null} ist oder {@code logger == null} und eine Problemmeldung
     *             anfällt.
     * @throws IllegalFormatException
     *             Wenn der Format-String ein unzulässiges oder nicht zu den Parametern
     *             passendes Format aufweist.
     */
    private static String resolveTranslationError(final ResourceBundle bundle,
            final String messageKey, final Exception cause) {
        final Logger theLogger = Assertion.assertNotNull(logger);
        theLogger
                .warn(String
                        .format("Internationalization error: No message found for key '%s' in language %s!",
                                Assertion.assertNotNull(messageKey), Assertion
                                        .assertNotNull(bundle).getLocale().toString()),
                        cause);
        try {
            return getTranslation(bundle, "errorNoTranslationFound", messageKey);
        } catch (final MissingResourceException e) {
            theLogger
                    .error("Severe internationalization error: No message found for the 'errorNoTranslationFound' key!",
                            e);
            return String.format("No message found for key '%s'!", messageKey);
        }
    }

}
