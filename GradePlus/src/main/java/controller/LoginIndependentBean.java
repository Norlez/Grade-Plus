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
package controller;

import java.util.Locale;

import javax.faces.context.FacesContext;

import common.model.Session;
import org.apache.log4j.Logger;

import common.model.User;

/**
 * Superbean für Beans, welche über keine Login-abhängigen Operationen verfügen. Die
 * aktuelle Sprache für diese Beans ist immer die vom Client vorgegebene.
 *
 * @author Sebastian Offermann, Marcel Steinbeck
 * @version 2017-06-28
 */
public abstract class LoginIndependentBean extends AbstractBean {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(LoginIndependentBean.class);

    /**
     * Erzeugt eine neue LoginIndependentBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden LoginIndependentBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    public LoginIndependentBean(final Session pSession) {
        super(pSession);
    }

    /**
     * Liefert die aktuelle Sprache zurück. Dies ist die vom Client angeforderte Sprache,
     * sofern vorhanden. Sollte diese nicht zur Verfügung stehen, ist es die
     * Standardsprache der Applikation.
     * 
     * @return Aktuelle Sprache.
     */
    @Override
    public Locale getLanguage() {
        final FacesContext context = FacesContext.getCurrentInstance();
        final Locale clientLanguage = context.getApplication().getViewHandler()
                .calculateLocale(context);
        if (clientLanguage != null) {
            logger.trace(String.format(
                    "Current language (login independent) is acquired from client: %s",
                    clientLanguage));
            return clientLanguage;
        }
        return User.getDefaultLanguage();
    }

}
