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

import common.util.Assertion;

import static common.util.Assertion.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Ein DAO, das Listen verwendet, um einen für die Laufzeit der Applikation gültigen
 * Datenbestand zu realisieren. Alle Operationen, die auf den internen Datenbestand
 * zugreifen, sind durch geeignete Maßnahmen synchronisiert, sodass Instanzen dieser
 * Klasse parallel verwendet werden können.
 *
 * @param <T>
 *            Das durch die Liste zu verwaltene Datum.
 *
 * @author Marcel Steinbeck
 * @version 2016-07-08
 */
public class TransientDAO<T> implements GenericDAO<T> {

    /**
     * Der Lock-Mechanismus um kritische Operationen vor konkurierende Zugriffe zu
     * schützen.
     */
    private final ReentrantReadWriteLock lock;

    /**
     * Der Datenbestand.
     */
    private final List<T> data;

    /**
     * Erzeugt ein Listen-DAO mit einem neuen Lock und einem leeren Datenbestand.
     */
    public TransientDAO() {
        lock = new ReentrantReadWriteLock();
        data = new ArrayList<>();
    }

    @Override
    public void save(final T pT) {
        Assertion.assertNotNull(pT);
        try {
            lock.writeLock().lock();
            data.add(pT);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void remove(final T theT) {
        Assertion.assertNotNull(theT);
        try {
            lock.writeLock().lock();
            data.remove(theT);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Gibt eine Kopie des internen Datenbestandes zurück.
     * 
     * @return Eine Kopie des internen Datenbestandes.
     */
    public List<T> getAll() {
        try {
            lock.readLock().lock();
            return new ArrayList<>(data);
        } finally {
            lock.readLock().unlock();
        }
    }

}
