package common.model;

import static common.util.Assertion.assertNotNull;

public enum SemesterTime {
    WINTER, SOMMER;

    public static String toString(final SemesterTime pSemester) {
        assertNotNull(pSemester);
        switch (pSemester) {
        case WINTER:
            return "WiSe";
        case SOMMER:
            return "SoSe";
        default:
            throw new IllegalArgumentException("SemesterTime: toString()");
        }
    }
}
