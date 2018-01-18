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

import java.util.List;

import javax.ejb.Stateless;

import common.exception.DuplicateUniqueFieldException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.Grade;
import common.model.User;

/**
 * Dieses DAO verwaltet Objekte der Klasse {@link Grade}.
 *
 * @author Dennis Schürholz, Karsten Hölscher, Sebastian Offermann, Marcel Steinbeck
 * @version 2016-07-12
 */
@Stateless
public class GradeDAO extends JPADAO<Grade> {

    @Override
    Class<Grade> getClazz() {
        return Grade.class;
    }

    @Override
    public void save(final Grade pT) {
        try {
            super.save(pT);
        } catch (final DuplicateUniqueFieldException e) {
            throw new UnexpectedUniqueViolationException(e);
        }
    }

    /**
     * Aktualisiert das gegebene {@link Grade}-Objekt.
     * 
     * @param pGrade
     *            Die zu aktualisierende Note.
     */
    public void update(Grade pGrade) {
        throw new UnsupportedOperationException();
    }

    /**
     * Löscht das gegebene {@link Grade}-Objekt.
     * 
     * @param pGrade
     *            Die zu löschende Note.
     */
    public void remove(Grade pGrade) {
        super.remove(pGrade);
    }

    /**
     * Gibt alle bekannten {@link Grade}-Objekte zurück.
     * 
     * @return Alle bekannten Noten.
     */
    public List<Grade> getAllGrades() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle Noten des gegebenen Nutzers zurück.
     * 
     * @param pUser
     *            Der Nutzer.
     * @return Alle Noten des Nutzers.
     */
    public List<Grade> getGradesForUser(User pUser) {
        throw new UnsupportedOperationException();
    }

}
