package common.model;

import static common.util.Assertion.*;

/**
 * Repräsentiert das Impressum mit der gegebenen Anschrift.
 *
 * @author Andreas Estenfelder, Marvin Kampen
 * @version 2018-01-09
 */
public class Impressum extends JPAEntity {

    /**
     * Der Name der Firma oder Universität.
     */
    private String name;

    /**
     * Straßenname der Firma oder Universität.
     */
    private String street;

    /**
     * Postleitzahl der Firma oder Universität.
     */
    private int postcode;

    /**
     * Stadtname der Firma oder Universität.
     */
    private String town;

    /**
     * Telefonnummer der Firma oder Universität.
     */
    private String phone;

    /**
     * Mailadresse der Firma oder Universitat.
     */
    private String mail;

    /**
     * Erstellt ein Impressum für die Seite.
     * 
     * @param pName
     *            Der Name der Firma
     * @param pStreet
     *            Die Straße des Firmensitzes.
     * @param pPostcode
     *            Der Postleitzahl des Firmensitzes.
     * @param pTown
     *            Die Stadt des Firmensitzes
     * @param pPhone
     *            Die Telefonnr. des Kundenservice.
     * @param pMail
     *            Die E-Mailadresse des Kundenservices.
     */
    public Impressum(String pName, String pStreet, int pPostcode, String pTown,
            String pPhone, String pMail) {
        name = assertNotEmpty(pName);
        street = assertNotEmpty(pStreet);
        postcode = pPostcode;
        town = assertNotEmpty(pTown);
        phone = assertNotEmpty(pPhone);
        mail = assertNotEmpty(pMail);
    }

    /**
     * Gibt den Firmennamen zurück
     * 
     * @return Der Firmenname als String.
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die Straße des Firmensitzes zurück.
     * 
     * @return Die Straße als String.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Gibt die Postleitzahl zurück.
     * 
     * @return Die Postleitzahl als int.
     */
    public int getPostcode() {
        return postcode;
    }

    /**
     * Gibt die Stadt des Firmensitzes zurück.
     * 
     * @return Die Stadt als String.
     */
    public String getTown() {
        return town;
    }

    /**
     * Gibt die Telefonadresse des Firmenkontakts zurück.
     * 
     * @return Die Telefonadresse als String.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gibt die E-Mailadresse des Firmenkontakts zurück.
     * 
     * @return Die E-Mailadresse als String
     */
    public String getMail() {
        return mail;
    }
}
