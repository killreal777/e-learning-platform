package itmo.blps.elearningplatform.service.exception;

public class StudentIsNotEnrolledException extends IllegalStateException {

    public StudentIsNotEnrolledException(Integer studentId, Integer courseId) {
        super(String.format("Student with id %s is not enrolled to course with id %s", studentId, courseId));
    }
}
