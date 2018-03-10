package common.model;

import static common.util.Assertion.assertNotNull;

/**
 * Repräsentiert die möglichen Rollen eines Benutzers.
 * 
 * @author Torben Groß
 * @version 2018-02-27
 */
public enum Role {

    ADMIN, EXAMINER, STUDENT;

    /**
     * Gibt die Rolle mit im gegebenen String enthaltenen Namen zurück.
     *
     * @param pString
     *            Der Rollenname als String.
     * @return Die gefundene Rolle oder {@code null}, falls keine passende Rolle gefunden
     *         werden konnte.
     */
    public static Role fromString(final String pString) {
        switch (assertNotNull(pString.trim().toUpperCase())) {
        case "ADMIN":
            return ADMIN;
        case "EXAMINER":
            return EXAMINER;
        case "STUDENT":
            return STUDENT;
        }
        return null;
    }

}
