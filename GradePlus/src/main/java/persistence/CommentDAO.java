package persistence;

import java.util.List;

import common.model.Comment;
import common.model.User;

/**
 * Wird nicht genutzt.
 *
 * Dieses DAO verwaltet die Objekte der Klasse {@link Comment}.
 * 
 * @author Torben Groß
 * @version 2018-03-11
 */
public class CommentDAO extends JPADAO<Comment> {

    @Override
    Class<Comment> getClazz() {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt {@code theComment} der Datenbank hinzu.
     * 
     * @param pComment
     *            Das zu speichernde {@code Comment}-Objekt.
     */
    @Override
    public synchronized void save(Comment pComment) {
        throw new UnsupportedOperationException();
    }

    /**
     * Aktualisiert den Eintrag von {@code theComment} im Datenbestand.
     * 
     * @param pComment
     *            Das zu aktualisierende {@link Comment}-Objekt.
     */
    @Override
    public synchronized void update(Comment pComment) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt eine Liste mit allen innerhalb der Applikation bekannten Kommentare zurück.
     *
     * @return Liste mit allen innerhalb der Applikation bekannten Kommentaren.
     */
    public List<Comment> getAllComments() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle Kommentare des gegebenen Benutzers zurück.
     * 
     * @param pAuthor
     *            Der Autor der gesuchten Kommentare.
     * @return Die Kommentare des gegebenen Benutzers als Liste.
     */
    public List<Comment> getCommentsForUser(User pAuthor) {
        throw new UnsupportedOperationException();
    }

}
