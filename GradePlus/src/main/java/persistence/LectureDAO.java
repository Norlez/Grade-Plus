package persistence;

import java.util.List;

import common.model.Lecture;
import common.model.User;

/**
 * Dieses DAO verwaltet die Objekte der Klasse {@link Lecture}.
 * 
 * @author Torben Groß
 */
public class LectureDAO extends JPADAO<Lecture> {

    @Override
    Class<Lecture> getClazz() {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt {@code theLecture} der Datenbank hinzu.
     * 
     * @param pLecture
     *            Das zu speichernde {@code Lecture}-Objekt.
     */
    @Override
    public synchronized void save(Lecture pLecture) {
        throw new UnsupportedOperationException();
    }

    /**
     * Aktualisiert den Eintrag von {@code theLecture} im Datenbestand.
     * 
     * @param pLecture
     *            Das zu aktualisierende {@link Lecture}-Objekt.
     */
    @Override
    public synchronized void update(Lecture pLecture) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt eine Liste mit allen innerhalb der Applikation bekannten Lehrveranstaltungen
     * zurück.
     * 
     * @return Liste mit allen innerhalb der Applikation bekannten Lehrveranstaltungen.
     */
    public List<Lecture> getAllLectures() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle Lehrveranstaltungen mit dem gegebenen Namen zurück.
     * 
     * @param pName
     *            Der Name der gesuchten Lehrveranstaltungen.
     * @return Die Lehrveranstaltungen mit dem gegebenen Namen als Liste.
     */
    public List<Lecture> getLecturesForName(String pName) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Lehrveranstaltung mit der gegebenen VAK-Nummer zurück.
     * 
     * @param pVAK
     *            Die VAK-Nummer der gesuchten Lehrveranstaltung.
     * @return Die Lehrveranstaltung mit der gegebenen VAK-Nummer.
     */
    public Lecture getLectureForVAK(String pVAK) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle Lehrveranstaltungen des gegebenen Semesters zurück.
     * 
     * @param pSemester
     *            Der Semester der gesuchten Lehrveranstaltungen.
     * @return Die Lehrveranstaltungen des gegebenen Semesters.
     */
    public List<Lecture> getLecturesForSemester(String pSemester) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle Lehrveranstaltungen des gegebenen Dozenten zurück.
     * 
     * @param pLecturer
     *            Der Dozent der gesuchten Lehrveranstaltungen.
     * @return Die Lehrveranstaltungen des gegebenen Dozenten.
     */
    public List<Lecture> getLecturesForLecturer(User pLecturer) {
        throw new UnsupportedOperationException();
    }

}
