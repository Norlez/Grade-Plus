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
package common.model;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import persistence.JPAEntityListener;
import persistence.JPADAO;

/**
 * Die Basisklasse aller durch JPA persistierten Klassen.
 * 
 * @author Marcel Steinbeck, Karsten Hölscher, Sebastian Offermann
 * @version 2016-07-12
 */
@MappedSuperclass
@EntityListeners(JPAEntityListener.class)
public class JPAEntity {

    /**
     * Die innerhalb der Applikation eindeutige Id (Primärschlüssel) eines JPA-Objektes.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Gibt die eindeutige Id des JPA-Objektes zurück. Ist {@code null}, falls das Objekt
     * noch nicht durch sein zugehöriges DAO persistiert wurde (vgl.
     * {@link JPADAO#save(JPAEntity)}).
     *
     * @return Die Id des JPA-Objektes.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die Id des Objektes auf {@code null}.
     */
    public void clearId() {
        id = null;
    }

    @Override
    public String toString() {
        return String.format("JPAEntity {id: %d}", id);
    }

}
