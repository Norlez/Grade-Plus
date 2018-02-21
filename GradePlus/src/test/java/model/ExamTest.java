package model;

import org.junit.Test;
import common.model.Exam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Äquivalenz- und Grenzfalltests für die Methoden der Klasse {@link common.model.Exam} .
 *
 * @author Marvin Kampen
 * @version 2018-10-02
 */
public class ExamTest {

    @Test
    public void createExam() {
        Exam e = new Exam();
        e.setTime(LocalTime.of(12, 15));
        e.setDate(LocalDate.of(2018, 2, 21));
        System.out.print(e.getDate() + " " + e.getTime());
    }
}
