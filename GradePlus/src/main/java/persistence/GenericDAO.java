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

import common.exception.DuplicateUniqueFieldException;

/**
 * Das Basisinterface aller DAOs. Ein DAO ermöglicht das Speichern, Laden und Löschen von
 * Daten und wird auch 'CRUD service' genannt. Das konkret zu verarbeitende Datum wird
 * über den Typparameter {@link T} definiert. Diese Klasse macht keine Annahme darüber,
 * wie der Datenbestand konkret realisiert wird.
 *
 * @param <T>
 *            Das zu verwaltene Datum.
 *
 * @author Marcel Steinbeck, Karsten Hölscher
 * @version 2016-07-12
 */
public interface GenericDAO<T> {

    /**
     * Fügt {@code theT} dem Datenbestand hinzu.
     *
     * @param pT
     *            Das zu speichernde Objekt.
     * @throws DuplicateUniqueFieldException
     *             Falls durch das Speichern von {@code theT} ein als `unique`
     *             deklariertes Attribut doppelt vorkommen würde.
     * @throws IllegalArgumentException
     *             Falls {@code theT == null} oder ein sonstiger, vom Datenbestand
     *             abhängiger Fehler aufgetreten ist.
     */
    public void save(final T pT) throws DuplicateUniqueFieldException;

    /**
     * Entfernt {@code theT} aus dem Datenbestand.
     *
     * @param pT
     *            Das zu entfernende Objekt.
     * @throws IllegalArgumentException
     *             Falls {@code theT == null} oder ein sonstiger, vom Datenbestand
     *             abhängiger Fehler aufgetreten ist.
     */
    public void remove(final T pT);

}
