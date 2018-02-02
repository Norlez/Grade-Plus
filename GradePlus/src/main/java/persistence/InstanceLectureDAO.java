package persistence;

import common.model.InstanceLecture;

public class InstanceLectureDAO extends JPADAO<InstanceLecture> {

    @Override
    Class<InstanceLecture> getClazz() {
        return InstanceLecture.class;
    }

    // TODO: Save und co. einfügen
}
