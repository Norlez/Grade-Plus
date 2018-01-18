package controller;

import common.model.Exam;
import common.model.Session;

import java.io.Serializable;

/**
 * Diese Bean ist für die Verwaltung der Dokumente verantwortlich.
 * 
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-21
 */
public class DocumentsBean extends AbstractBean implements Serializable {

    /**
     * Erzeugt eine neue DocumentsBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden DocumentsBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    public DocumentsBean(Session pSession) {
        super(pSession);
    }

    /**
     * Druckt einen Leistungsnachweis für die gegebene Prüfung aus.
     *
     * @param pExam
     *            Die Prüfung des Leistungsnachweises.
     */
    public void printCertificate(Exam pExam) {
        throw new UnsupportedOperationException();
    }

    /**
     * Druckt ein Protokoll für die gegebene Prüfung aus.
     * 
     * @param pExam
     *            Die Prüfung des Protokolls.
     */
    public void printProtocol(Exam pExam) {
        throw new UnsupportedOperationException();
    }

    /**
     * Druckt eine Quittung für die gegebene Prüfung aus.
     * 
     * @param pExam
     *            Die Prüfung der Quittung.
     */
    public void printReceipt(Exam pExam) {
        throw new UnsupportedOperationException();
    }

    /**
     * Druckt einen Leistungsnachweis für die gegebene Prüfung für einen Zeitraum aus.
     * 
     * @param pExam
     *            Die Prüfung des Leistungsnachweises.
     */
    public void printCertificateTimeFrame(Exam pExam) {
        throw new UnsupportedOperationException();
    }

    /**
     * Druckt ein Protokoll für die gegebene Prüfung für einen Zeitraum aus.
     * 
     * @param pExam
     *            Die Prüfung des Protokolls.
     */
    public void printProtocolTimeFrame(Exam pExam) {
        throw new UnsupportedOperationException();
    }

    /**
     * Druckt eine Quittung für die gegebene Prüfung für einen Zeitraum aus.
     * 
     * @param pExam
     *            Die Prüfung der Quittung.
     */
    public void printReceiptTimeFrame(Exam pExam) {
        throw new UnsupportedOperationException();
    }

}