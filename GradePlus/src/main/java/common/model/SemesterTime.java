package common.model;

import static common.util.Assertion.assertNotNull;

public enum SemesterTime {
    WINTER, SOMMER;

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
