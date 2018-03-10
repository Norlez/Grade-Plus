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
package common.exception;

/**
 * Diese Exception signalisiert, dass das Speichern eines Datums (vgl.
 * {@link persistence.GenericDAO#save(Object)}) dazu führen würde, dass ein als `unique`
 * deklariertes Attribut doppelt vorkommen würde.
 *
 * @author Marcel Steinbeck
 * @version 2016-07-12
 */
public class DuplicateUniqueFieldException extends Exception {

    /**
     * Die eindeutige SerialisierungsID.
     */
    private static final long serialVersionUID = 5908905036654024577L;

    /**
     * Erzeugt eine neue {@link DuplicateUniqueFieldException} mit der gegebenen
     * Fehlernachricht.
     *
     * @param theMessage
     *            Die Fehlernachricht der zu erzeugenden Exception.
     */
    public DuplicateUniqueFieldException(final String theMessage) {
        super(theMessage);
    }

}
