package controller;

import common.model.*;
import controller.ExamsBean;

import org.junit.Before;
import org.junit.Test;
import persistence.ExamDAO;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class ExamBeanTest {

    private LocalTime lt;

    private LocalDate ld;

    private Exam e;

    private ExamsBean eb;

    private Session s;

    private ExamDAO ed;

    @Before
    public void init()
    {
        lt = LocalTime.of(14,00);
        ld = LocalDate.of(2018,2,22);
        e = new Exam();
        s = new Session();
        ed = new ExamDAO();
       eb = new ExamsBean(s,ed);
    }

    @Test
    public void testIfDateIsOccupied()
    {
        e.setDate(LocalDate.of(2018,2,22));
        assertTrue(eb.isDateUsed(ld,e));
        e.setDate(LocalDate.of(2013,10,2));
        assertFalse(eb.isDateUsed(ld,e));
    }

    @Test
    public void testIfTimeIsOccupied()
    {
        e.setTime(LocalTime.of(14,00));
        assertTrue(eb.isTimeUsed(lt,20,e)); //Fehler bei Gleichheit der Zeit
        e.setTime(LocalTime.of(14,20));
        assertTrue(eb.isTimeUsed(lt,20,e));
        e.setTime(LocalTime.of(13,40));
        assertTrue(eb.isTimeUsed(lt,20,e));
        e.setTime(LocalTime.of(13,39));
        assertFalse(eb.isTimeUsed(lt,20,e));
        e.setTime(LocalTime.of(14,21));
        assertFalse(eb.isTimeUsed(lt,20,e));
    }
}
