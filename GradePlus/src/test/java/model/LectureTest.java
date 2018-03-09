package model;

import common.model.InstanceLecture;
import common.model.Lecture;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static common.util.Assertion.*;
import static org.junit.Assert.assertEquals;

/**
 * Äquivalenz- und Grenzfalltests für die Methoden der Klasse {@link common.model.Lecture}
 * .
 *
 * @author Marvin Kampen
 * @version 2018-10-02
 */
public class LectureTest {

    @Test
    public void createEmptyLecture() {
        Lecture l = new Lecture();
        assertEquals(null, l.getName());
        assertEquals(null, l.getVak());
        assertEquals(null, l.getDescription());
        assertEquals(0, l.getEcts());
        assertEquals(null, l.getInstanceLectures());
    }

    @Test
    public void createNonEmptyLecture() {
        Lecture l = new Lecture();
        l.setDescription("Hallo");
        l.setEcts(3);
        l.setName("SWP");
        l.setVak("123-5");
        Set<InstanceLecture> i = new HashSet<InstanceLecture>();
        l.setInstanceLectures(i);
        assertEquals("SWP", l.getName());
        assertEquals("123-5", l.getVak());
        assertEquals("Hallo", l.getDescription());
        assertEquals(3, l.getEcts());
        assertEquals(i, l.getInstanceLectures());
    }

    @Test(expected = IllegalArgumentException.class)
    public void LectureWithNullName() {
        Lecture l = new Lecture();
        l.setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void LectureWithNullVAK() {
        Lecture l = new Lecture();
        l.setVak(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void LectureWithNegativeECTS() {
        Lecture l = new Lecture();
        l.setEcts(-2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void LectureWithNullILV() {
        Lecture l = new Lecture();
        l.setInstanceLectures(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void LectureWithNullDescription() {
        Lecture l = new Lecture();
        l.setDescription(null);
    }
}
