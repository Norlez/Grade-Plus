package controller;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import javax.swing.*;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import common.model.Session;
import common.util.Assertion;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import persistence.GradeDAO;
import persistence.UserDAO;

@Named
@RequestScoped
public class FileChooser extends AbstractBean {


    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFileName() {
        return fileName = file.getName();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    private Part file;

    private InputStream inputStream;

    private String fileName;



    private GradeDAO gradeDAO;
    private UserDAO userDAO;

    @Inject
    public FileChooser(final Session pSession, final GradeDAO pGradeDAO,
                      final UserDAO pUserDAO) {
        super(pSession);
        gradeDAO = Assertion.assertNotNull(pGradeDAO);
        userDAO = Assertion.assertNotNull(pUserDAO);
    }

    public void fileToInputstream() throws IOException{
        fileName = file.getName();
        System.out.println("AAAAAAAAAAAAAA" + fileName);

        //if(file != null) {
          //  inputStream = file.getInputStream();
         //   fileName = file.getName();
        //}
    }
}
