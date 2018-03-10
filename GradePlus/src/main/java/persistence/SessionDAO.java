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
package persistence;

import static common.util.Assertion.assertNotNull;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import common.model.Session;
import common.model.User;
import common.util.Assertion;

/**
 * Dieses DAO verwaltet Objekte der Klasse {@link Session}.
 *
 * @author Marcel Steinbeck, Karsten Hölscher, Sebastian Offermann
 * @version 2016-07-22
 */
@Named
@ApplicationScoped
public class SessionDAO extends TransientDAO<Session> implements Serializable {

    /**
     * Die eindeutige SerialisierungsID.
     */
    private static final long serialVersionUID = -4448901874981927743L;

    /**
     * Gibt zurück, ob der gegebene Benutzer eine aktive Session hat, d. h. eingeloggt
     * ist.
     *
     * @param user
     *            der zu prüfende Benutzer
     * @return {@code true} falls der gegebene Benutzer aktuell eingeloggt ist, sonst
     *         {@code false}.
     * @throws IllegalArgumentException
     *             falls der übergebene Parameter den Wert {@code null} hat
     */
    public boolean isUserLoggedIn(final User user) {
        Assertion.assertNotNull(user);
        final List<Session> sessions = getAll();
        for (final Session session : sessions) {
            final User sessionUser = session.getUser();
            if (sessionUser != null && sessionUser.equals(user)) {
                return true;
            }
        }
        return false;
    }

}
