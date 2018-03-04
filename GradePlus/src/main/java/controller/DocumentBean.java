package controller;

import common.model.*;
import persistence.ExamDAO;
import persistence.UserDAO;

import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

import static common.util.Assertion.*;

/**
 * Diese Bean ist für das Befüllen der Dokumente zuständig.
 *
 * @author Marvin Kampen
 * @version 2018-03-01
 */
@Named
@RequestScoped
public class DocumentBean extends AbstractBean implements Serializable {

    private final UserDAO userDAO;

    private final ExamDAO examDAO;

    /**
     * Erzeugt eine neue AbstractBean.
     *
     * @param theSession
     *            Die Session der zu erzeugenden AbstractBean.
     * @throws IllegalArgumentException
     *             Falls {@code theSession} {@code null} ist.
     */
    @Inject
    public DocumentBean(Session theSession, UserDAO pUserDao, ExamDAO pExamDao) {
        super(theSession);
        userDAO = assertNotNull(pUserDao);
        examDAO = pExamDao;
    }

    public String getNameOfStudent(User pStudent) {
        assertNotNull(pStudent);
        return userDAO.getUserForUsername(pStudent.getUsername()).getGivenName();
    }

    public String getSurnameOfStudent(User pStudent) {
        assertNotNull(pStudent);
        return userDAO.getUserForUsername(pStudent.getUsername()).getSurname();
    }

    public String getMatrNrOfStudent(User pStudent) {
        assertNotNull(pStudent);
        return userDAO.getUserForUsername(pStudent.getUsername()).getMatrNr();
    }

    public void printCertificateForStudent() throws IOException {
        System.out.println("AE.ERZEUGTE ZUSTANDSMELDUNG");
        Document document = new Document();
        System.out.println("AE.ERZEUGTE ZUSTANDSMELDUNG 2");

        try {
            document.printCertificate("d","e");
        } catch (IOException e) {
            throw new IOException("Schon wieder was schief gelaufen!");
        }
    }
}
