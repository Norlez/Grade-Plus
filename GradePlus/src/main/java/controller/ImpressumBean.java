package controller;

import common.model.Session;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Dieses Bean ist für das Verwalten des Impressums zuständig.
 *
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-20
 */
@Named
@RequestScoped
public class ImpressumBean extends AbstractBean implements Serializable {

    /**
     * Erzeugt eine neue ImpressumBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden ImpressumBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    @Inject
    public ImpressumBean(Session pSession) {
        super(pSession);
    }

    /**
     * Gibt den Namen aus.
     *
     * @return Der Name der Uni.
     */
    public String getName() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Strasse aus.
     *
     * @return Die Strasse.
     */
    public String getStreet() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Postleitzahl aus.
     *
     * @return Die Postleitzahl.
     */
    public String getPostcode() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt den Staedtenamen aus.
     *
     * @return Den Stadtenamen.
     */
    public String getTown() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Telefonnr. aus.
     *
     * @return Die Telefonnummer.
     */
    public String getPhone() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Mailadresse aus.
     *
     * @return Die Mailadresse.
     */
    public String getMail() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die angegebenen Daten fuers Impressum (Name, Adresse, Telefonnr., Mail).
     */
    public void init() {
        throw new UnsupportedOperationException();
    }

}
