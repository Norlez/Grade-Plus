package common.model;

/**
 * Repräsentiert die möglichen Semesterzeiten im System.
 * Winter für das Wintersemester. Sommer für das Sommersemester.
 *
 * @author Torben Groß
 * @version 2018-03-11
 */
import static common.util.Assertion.assertNotNull;

public enum SemesterTime {
    WINTER, SOMMER;

    /**
     * Wandelt die gegebene Semesterzeit in eine String Repräsentation um.
     *
    * @param pSemesterTime erhält eine Semesterzeit.
     * @return Die Semesterzeit als String.
     */
    public static String toString(final SemesterTime pSemesterTime) {
        switch (assertNotNull(pSemesterTime)) {
        case WINTER:
            return "WiSe";
        case SOMMER:
            return "SoSe";
        default:
            return pSemesterTime.toString();
        }
    }
}
