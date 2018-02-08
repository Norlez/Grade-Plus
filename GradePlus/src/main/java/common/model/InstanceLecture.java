package common.model;

import javax.persistence.*;

@Entity
public class InstanceLecture extends JPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "lecture_fk")
    private Lecture lecture;

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
