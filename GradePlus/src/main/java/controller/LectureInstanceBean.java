package controller;

import common.exception.DuplicateInstanceLectureException;
import common.model.InstanceLecture;
import common.model.Lecture;
import common.model.Session;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import persistence.InstanceLectureDAO;
import static common.util.Assertion.assertNotNull;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Dieses Bean ist für die Erstellung von Instanzen einer Lehrveranstaltung
 * verantwortlich.
 * 
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-22
 */
@Named
@RequestScoped
public class LectureInstanceBean extends AbstractBean implements Serializable {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(UsersBean.class);

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für
     * Lehrveranstaltungs-Objekte übernimmt.
     */
    private final InstanceLectureDAO instanceLectureDAO;

    /**
     * Die aktuell angezeigte ILV, dessen Attribute durch die UIKomponenten des Facelets
     * geschrieben und gelesen werden.
     */
    private InstanceLecture ilv;

    /**
     * Die Liste aller innerhalb der Applikation bekannten ILVs.
     */
    private List<InstanceLecture> allIlvs;

    /**
     * Die Liste aller ILVs zu einer Lehrveranstaltung.
     */
    private List<InstanceLecture> ilvs;

    /**
     * Erzeugt eine neue LectureInstanceBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden LectureInstanceBean.
     * @param pInstanceDao
     *            Die Persistierung der ILV.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    @Inject
    public LectureInstanceBean(Session pSession, InstanceLectureDAO pInstanceDao) {
        super(pSession);
        instanceLectureDAO = assertNotNull(pInstanceDao);
    }

    /**
     * Initialisiert die Attribute {@link #ilv} und {@link #allIlvs}, sodass {@link #ilv}
     * eine neu anzulegende {@link #ilv} repräsentiert und {@link #allIlvs} alle bekannten
     * ILVs der Applikation enthält.
     */
    @PostConstruct
    public void init() {
        ilv = new InstanceLecture();
        allIlvs = instanceLectureDAO.getAllInstanceLectures();
    }

    /**
     * Gibt die anzuzeigende ILV zurück.
     *
     * @return Die anzuzeigende ILV.
     */
    public InstanceLecture getLectureInstance() {
        return ilv;
    }

    /**
     * Gibt alle innerhalb der Applikation bekannten ILVs zurück.
     *
     * @return Die anzuzeigende Liste aller innerhalb der Applikation bekannten ILVs.
     */
    public List<InstanceLecture> getAllLectures() {
        return allIlvs;
    }

    /**
     * Gibt alle ILVs einer LV zurück.
     *
     * @param pLecture
     *            Die gesuchte Lehrveranstaltung.
     * @return Die anzuzeigende Liste aller ILVs zu einer LV.
     */
    public List<InstanceLecture> getAllIlvsForLecture(final Lecture pLecture) {
        Lecture l = pLecture;
        ilvs = instanceLectureDAO.getInstanceLecturesForLecture(pLecture);
        return allIlvs;
    }

    /**
     * Fügt die aktuell angezeigte ILV der Liste aller innerhalb der Applikation bekannten
     * ILVs hinzu.
     *
     * @return "lectures.xhtml", um auf das Facelet der Übersicht der Lehrveranstaltung zu
     *         leiten.
     */
    public String save() {
        try {
            instanceLectureDAO.save(ilv);
        } catch (final DuplicateInstanceLectureException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorILVdataDuplicated"));
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorILVdataIncomplete"));
        }
        init();
        return "lectures.xhtml"; // TODO: Muss auf die ILV Seite zeigen
    }

    /**
     * Gibt die VAK-Nummer der Lehrveranstaltung zurück.
     *
     * @return Die gegebene VAK.
     */
    public String getVAK() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die VAK-Nummer der Lehrveranstaltung.
     *
     * @param pVAK
     *            Die gegebene VAK.
     */
    public void setVAK(String pVAK) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt das Semester der Lehrveranstaltung aus.
     *
     * @return Das gegebene Semester.
     */
    public int getSemester() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt das Semester der Lehrveranstaltung.
     *
     * @param pSemester
     *            Das gegebene Semester.
     */
    public void setSemester(int pSemester) {
        throw new UnsupportedOperationException();
    }

    /**
     * Erstellt neue Instanz der Lehrveranstaltung.
     */
    public void createNewInstance() {
        throw new UnsupportedOperationException();
    }

    /**
     * Kopiert vorhandene Instanz.
     *
     * @param pLecture
     *            Die zu kopierende Instanz der Lehrveranstaltung.
     */
    public void copyInstance(Lecture pLecture) {
        throw new UnsupportedOperationException();
    }

    /**
     * Löscht Instanz der Lehrveranstaltung.
     *
     * @param pLecture
     *            zu löschende Instanz.
     */
    public void deleteInstance(Lecture pLecture) {
        throw new UnsupportedOperationException();
    }

}
