package common.model;

import javax.persistence.*;

@Entity
public class InstanceLecture extends JPAEntity {

    @ManyToOne(optional = false)
    private Lecture lecture;

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
