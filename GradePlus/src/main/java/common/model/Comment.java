package common.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static common.util.Assertion.assertNotEmpty;
import static common.util.Assertion.assertNotNull;

/**
 * Nutzen wir nicht.
 *
 * Repräsentiert einen Kommentar, der von Benutzern auf einem Dokument wie beispielsweise
 * einem Prüfungsprotokoll hinterlassen werden kann. Zu jedem Kommentar ein Autor, sowie
 * der Inhalt und das Erstellungsdatum.
 *
 * @author Torben Groß, Marvin Kampen
 * @version 2018-03-11
 */
public class Comment extends JPAEntity {

    /**
     * Der Autor des Kommentars.
     */
    private User author;

    /**
     * Der Zeitpunkt der Veröffentlichung des Kommentars.
     */
    private LocalDateTime date;

    /**
     * Der Inhalt des Kommentars.
     */
    private String content;

    /**
     * Die Prüfung zu dem der Kommentar verfasst wird.
     */
    private Exam exam;

    /**
     * Gibt die Prüfung zu dem Kommentar aus.
     */
    public Exam getExam() {
        return exam;
    }

    /**
     * Setzt den Kommentar für eine Prüfung.
     */
    public void setExam(Exam exam) {
        if (exam != null) {
            this.exam = exam;
        }
    }

    /**
     * Gibt den Autor des Kommentars zurück.
     *
     * @return Den Autor des Kommentars.
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Setzt den Autor auf den gegebenen Benutzer.
     *
     * @param pAuthor
     *            Der neue Autor. Darf nicht Null sein. Muss vom Typ User sein.
     */
    public void setAuthor(User pAuthor) {
        author = assertNotNull(pAuthor);
    }

    /**
     * Gibt den Zeitpunkt der Veröffentlichung des Kommentars wieder.
     *
     * @return Den Zeitpunkt der Veröffentlichung des Kommentars.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Setzt den Zeitpunkt der Veröffentlichung des Kommentars auf die gegebene Zeit.
     *
     * @param pDate
     *            Der neue Zeitpunkt der Veröffentlichung des Kommentars. Darf nicht Null
     *            sein und muss vom Typ LocalDateTime sein.
     */
    public void setDate(LocalDateTime pDate) {
        date = assertNotNull(pDate);
    }

    /**
     * Gibt den Inhalt des Kommentars zurück.
     *
     * @return Den Inhalt des Kommentars.
     */
    public String getContent() {
        return content;
    }

    /**
     * Setzt den Inhalt des Kommentars auf den gegebenen Inhalt.
     *
     * @param pContent
     *            Der neue Inhalt des Kommentars.
     */
    public void setContent(String pContent) {
        content = assertNotEmpty(pContent);
    }
}
