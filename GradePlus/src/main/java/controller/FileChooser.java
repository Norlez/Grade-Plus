package controller;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;


import common.model.Session;
import common.util.Assertion;
import persistence.GradeDAO;
import persistence.UserDAO;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Dieses Bean ist der Controller für das auswählen von Dateien über dem Client.
 *
 * @author Arbnor Miftari
 * @version 2018-02-24
 */
@Named
@RequestScoped
public class FileChooser extends AbstractBean {

    /**
     * Gibt den Namen der Datei zurück.
     * @return
     */
    public String getFileName() {
        return fileName = file.getName();
    }

    /**
     * Setzt den Namen der Datei.
     * @param fileName
     *            Der Name der Datei
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    /**
     * Gibt das Part-Objekt zurück.
     * @return
     *       Das Part-Objekt.
     */
    public Part getFile() {
        return file;
    }

    /**
     * Setzt das übergebene Part-Objekt
     * @param file
     *          Das Part-Object, dass gesetzt werden soll.
     */

    public void setFile(Part file) {
        this.file = file;
    }

    /**
     * Das Part-Objekt, dass die hochgeladene Datei vom User repräsentiert.
     */
    private Part file;

    /**
     * Das String-Objekt, dass den Namen der hochgeladenen Datei repräsentiert.
     */
    private String fileName;


    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für
     * user-Objekte übernimmt.
     */
    private UserDAO userDAO;



    /**
     *
     * @param pSession
     *            Die Session der zu erzeugenden UsersBean.
     * @param pUserDAO
     *            Die UserDAO der zu erzeugenden UsersBean.
     *            Die SessionDAO der zu erzeugenden UsersBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} oder {@code pUserDAO} }
     *             {@code null} ist.
     */
    @Inject
    public FileChooser(final Session pSession, final GradeDAO pGradeDAO,
            final UserDAO pUserDAO) {
        super(pSession);
        userDAO = Assertion.assertNotNull(pUserDAO);
    }


    public void saveFromCSV() throws IOException {
        InputStream inputStream = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String newLine;
        ArrayList<String> lines = new ArrayList<String>();
        while ((newLine = br.readLine()) != null) {
            System.out.println(newLine);
            lines.add(newLine);
        }

        for(int i = 0; i < lines.size(); i++){
            String[] dataString = new String[10];
            String tmpString = lines.get(i);
            dataString = tmpString.split("-");
            System.out.println(dataString.toString());
        }

        String result = org.apache.commons.io.IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        System.out.println(result);
    }

}
