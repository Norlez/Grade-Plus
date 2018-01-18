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
import static common.util.Assertion.assertNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TransactionRequiredException;

import common.exception.DuplicateUniqueFieldException;
import common.model.JPAEntity;
import common.util.Assertion;

/**
 * Ein DAO das JPA verwendet, um einen über die Laufzeit der Applikation hinaus gültigen
 * Datenbestand zu realisieren.
 *
 * @param <T>
 *            Das durch JPA zu persitierende Datum.
 *
 * @author Marcel Steinbeck, Sebastian Offermann
 * @version 2016-07-22
 */
public abstract class JPADAO<T extends JPAEntity> implements GenericDAO<T> {

    /**
     * Der für den Zugriff auf die Datenquelle verwendete Persistenzkontext. Kann in der
     * Datei 'resources/META-INF/persistence.xml' konfiguriert werden. Zudem ist es
     * möglich, mehrere Konfigurationen zu spezifizieren und die gewünschten Konfiguration
     * über 'unitName' auszuwählen.
     */
    @PersistenceContext(unitName = "embedded")
    private EntityManager em;

    /**
     * Gibt das zu {@link T} zugehörige {@link Class} Objekt zurück. Dieses Objekt wird
     * benötigt, um einen typsicheren Query mit JPA absetzen zu können und darf niemals
     * {@code null} sein. Diese Methode wird beispielsweise von {@link #getById(Long)}
     * verwendet.
     *
     * @return Das zu {@link T} zugehörige {@link Class} Objekt, niemals {@code null}.
     */
    abstract Class<T> getClazz();

    /**
     * Gibt den verwendeten Persistenzkontext zurück und erlaubt somit Unterklassen eigene
     * Queries auf die Datenquelle abzusezten. Darf niemals {@code null} sein.
     *
     * @return Der verwendete Persistenzkontext, niemals {@code null}.
     */
    EntityManager getEm() {
        return em;
    }

    /**
     * Gibt das Objekt mit der Id {@code theId} zurück. Da die Id von Objekten der selben
     * Klasse eindeutig ist, kann es höchstens ein Objekt mit der gegebenen Id geben.
     * Falls kein Objekt mit der gegebenen Id existiert, wird {@code null} zurückgegeben.
     *
     * @param pId
     *            Die Id des gesuchten Objektes.
     * @return Das Objekt mit der Id {@code theId} oder {@code null}, falls ein solches
     *         Objekt im Datenbestand nicht vorhanden ist.
     * @throws IllegalArgumentException
     *             Falls {@code theId == null}.
     */
    public T getById(final Long pId) {
        return em.find(getClazz(), Assertion.assertNotNull(pId));
    }

    /**
     * Fügt {@code theT} dem Datenbestand hinzu. Falls {@code theT} bereits im
     * Datenbestand vorhanden ist ({@code theT.getId() != null}), wird eine
     * {@link IllegalArgumentException} ausgelöst.
     *
     * @param pT
     *            Das zu persistierende Objekt.
     * @throws IllegalArgumentException
     *             Falls {@code pT == null}, {@code pT.getId() != null} oder {@code pT}
     *             kein durch JPA verwaltetes Objekt ist.
     * @throws TransactionRequiredException
     *             Falls zum Zeitpunkt des Aufrufs keine gültige Transaktion vorliegt
     *             (vgl. {@link EntityManager#persist(Object)}).
     */
    @Override
    public void save(final T pT) throws DuplicateUniqueFieldException {
        Assertion.assertNotNull(pT);
        Assertion.assertNull(pT.getId(), "The parameter aleady has an ID!");
        em.persist(pT);
    }

    /**
     * Aktualisiert den Eintrag von {@code theT} im Datenbestand. Falls {@code theT} noch
     * nicht im Datenbestand vorhanden ist, wird eine {@link IllegalArgumentException}
     * ausgelöst.
     *
     * @param theT
     *            Das zu aktualisierende Objekt.
     * @throws DuplicateUniqueFieldException
     *             Falls durch das Aktualisieren von {@code theT} ein als `unique`
     *             deklariertes Attribut doppelt vorkommen würde.
     * @throws IllegalArgumentException
     *             Falls {@code theT == null}, {@code theT.getId() == null}, es noch
     *             keinen Eintrag für {@code theT} im Datenbestand gibt oder {@code theT}
     *             kein durch JPA verwaltetes Objekt ist.
     * @throws TransactionRequiredException
     *             Falls zum Zeitpunkt des Aufrufs keine gültige Transaktion vorliegt
     *             (vlg. {@link EntityManager#merge(Object)}).
     */
    public void update(final T theT) throws DuplicateUniqueFieldException {
        Assertion.assertNotNull(theT);
        final Long tId = Assertion.assertNotNull(theT.getId(),
                "The id of the parameter must not be null!");
        Assertion.assertNotNull(getById(tId), "The parameter is not yet registered!");
        em.merge(theT);
    }

    /**
     * Entfernt {@code theT} aus dem Datenbestand. Falls {@code theT} nicht im
     * Datenbestand vorhanden ist, wird *keine* Exception ausglöst. In jedem Fall ist nach
     * Aufruf dieser Methode die Id von {@code theT} gleich {@code null}.
     *
     * Hinweis: Die Methode ist synchronisiert!
     *
     * @param theT
     *            Das zu entfernende Objekt.
     * @throws IllegalArgumentException
     *             Falls {@code theT == null} ist.
     * @throws TransactionRequiredException
     *             Falls zum Zeitpunkt des Aufrufs keine gültige Transaktion vorliegt
     *             (vlg. {@link EntityManager#remove(Object)}).
     */
    @Override
    public synchronized void remove(final T theT) {
        Assertion.assertNotNull(theT);
        if (theT.getId() != null) {
            final T entity = getById(theT.getId());
            if (entity != null) {
                em.remove(entity);
            }
            theT.clearId();
        }
    }

}
