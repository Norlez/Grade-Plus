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
package util;

import java.util.Arrays;
import java.util.Collection;

/**
 * Hilfsklasse zur Generierung einer dreidimensionalen Sammlung aus einem
 * dreidimensionalen Array.
 * 
 * @author Sebastian Offermann
 * @version 2016-05-25
 */
public final class To {

    private To() {
    }

    /**
     * Erstellt eine dreidimensionale Sammlung aus einem dreidimensionalen Array. Der Typ
     * der Sammlung wird durch die gegebenen Klassenelemente bestimmt.
     * 
     * 
     * @param <T>
     *            Typ der GrundElemente der zu erstellenden Sammlung.
     * @param <U>
     *            Typ der Sammlungen auf unterster Ebene (@code innerClass); die Sammlung
     *            dieses Typs wird direkt Grundelement beinhalten.
     * @param <V>
     *            Typ der Sammlungen auf der mittleren Ebene (@code midClass); die
     *            Sammlung dieses Typs wird Sammlungen vom Typ der untersten Ebene
     *            enthalten.
     * @param <W>
     *            Typ der Sammlung auf der obersten Ebene (@code outerClass).
     * @param elements
     *            die Elemente, welche die neue Sammlung beinhaltet.
     * @param outerClass
     *            der Typ der Sammlung (erste Dimension).
     * @param midClass
     *            der Typ der Sammlungen innerhalb der Sammlung (zweite Dimension).
     * @param innerClass
     *            der Typ der Sammlungen innerhalb der Sammlungen innerhalb der Sammlung
     *            (dritte Dimension).
     * @return eine neue Sammlung vom Typ {@code W<V<U<T>>>} mit den gegebenen Elementen
     * @throws InstantiationException
     *             wenn eine der gegebeben SammlungsTypen nicht instaniierbar ist (z. B.
     *             ein Interface, eine abstrakte Klasse) oder keinen Konstruktor ohne
     *             Parameter aufweist.
     * @throws IllegalAccessException
     *             wenn der Konstruktor ohne Parameter einer der gegebenen SammlungsTypen
     *             nicht zugreifbar ist (zu geringe Sichtbarkeit aufweist).
     */
    public static <T, U extends Collection<T>, V extends Collection<U>, W extends Collection<V>> W collection(
            final T[][][] elements, final Class<W> outerClass, final Class<V> midClass,
            final Class<U> innerClass) throws InstantiationException,
            IllegalAccessException {
        if (elements == null) {
            return null;
        }
        if (outerClass == null || midClass == null || innerClass == null) {
            throw new IllegalArgumentException();
        }
        final W result = outerClass.newInstance();
        for (final T[][] wElements : elements) {
            if (wElements == null) {
                result.add(null);
                continue;
            }
            final V subResult = midClass.newInstance();
            for (final T[] vElements : wElements) {
                if (vElements == null) {
                    subResult.add(null);
                    continue;
                }
                final U subsubResult = innerClass.newInstance();
                subsubResult.addAll(Arrays.asList(vElements));
                subResult.add(subsubResult);
            }
            result.add(subResult);
        }
        return result;
    }

}
