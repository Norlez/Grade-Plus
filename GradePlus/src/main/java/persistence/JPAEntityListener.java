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

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import common.util.Assertion;
import org.apache.log4j.Logger;

import common.model.JPAEntity;

/**
 * Listener, der an alle JPA-Entities, die {@link JPAEntity} erweitern, angehängt wird um
 * über Ereignisse informiert zu werden. Wird aktuell zum Loggen mit log4j verwendet.
 *
 * @author Karsten Hölscher, Sebastian Offermann
 * @version 2016-07-08
 */
public class JPAEntityListener {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(JPAEntityListener.class);

    /**
     * Wird aufgerufen, bevor eine Entität durch JPA persistiert wird.
     * 
     * @param entity
     *            die zu persistierende Entität
     */
    @PrePersist
    public void prePersist(final JPAEntity entity) {
        log("Pre-persist of entity: ", entity);
    }

    /**
     * Wird aufgerufen, wenn eine Entität durch JPA persistiert wurde.
     * 
     * @param entity
     *            die persistierte Entität
     */
    @PostPersist
    public void postPersist(final JPAEntity entity) {
        log("Post-persist of entity: ", entity);
    }

    /**
     * Wird aufgerufen, bevor eine Entität durch JPA entfernt wird.
     * 
     * @param entity
     *            die zu entfernende Entität
     */
    @PreRemove
    public void preRemove(final JPAEntity entity) {
        log("Pre-remove of entity: ", entity);
    }

    /**
     * Wird aufgerufen, nachdem eine Entität durch JPA entfernt wurde.
     * 
     * @param entity
     *            die entfernte Entität
     */
    @PostRemove
    public void postRemove(final JPAEntity entity) {
        log("Post-remove of entity: ", entity);
    }

    /**
     * Wird aufgerufen, bevor eine Entität durch JPA aktualisiert wird.
     * 
     * @param entity
     *            die zu aktualisierende Entität
     */
    @PreUpdate
    public void preUpdate(final JPAEntity entity) {
        log("Pre-Update of entity: ", entity);
    }

    /**
     * Wird aufgerufen, nachdem eine Entität durch JPA aktualisiert wurde.
     * 
     * @param entity
     *            die aktualisierte Entität
     */
    @PostUpdate
    public void postUpdate(final JPAEntity entity) {
        log("Post-update of entity: ", entity);
    }

    /**
     * Wird aufgerufen, nachdem eine Entität durch JPA eingelesen wurde.
     * 
     * @param entity
     *            die eingelesene Entität
     */
    @PostLoad
    public void postLoad(final JPAEntity entity) {
        log("Post-load of entity: ", entity);
    }

    /**
     * Komfort-Methode zum Erstellen von Log-Einträgen.
     * 
     * @param context
     *            der Kontext des Log-Eintrages; dieser wird vorne an den Log-Eintrag
     *            angehängt.
     * @param entity
     *            die Entität, auf welcher operiert wird.
     */
    private void log(final String context, final JPAEntity entity) {
        if (logger.isDebugEnabled()) {
            logger.debug(context + Assertion.assertNotNull(entity).toString());
        }
    }

}
