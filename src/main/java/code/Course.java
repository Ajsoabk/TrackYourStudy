package code;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableView;

public class Course {
    private final SimpleStringProperty courseName;
    private final SimpleStringProperty courseId;
    private final SimpleIntegerProperty semester;
    private final SimpleStringProperty teacherName;
    private final SimpleDoubleProperty mark;
    private final SimpleDoubleProperty gpa;

    public Course(String name, String courseId,String teacher, int semester, double mark) {
        this.courseName = new SimpleStringProperty(name);
        this.courseId = new SimpleStringProperty(courseId);
        this.teacherName=new SimpleStringProperty(teacher);
        this.semester = new SimpleIntegerProperty(semester);
        this.mark = new SimpleDoubleProperty(mark);
        this.gpa = calculateGpa(mark);
    }
    @Override
    public String toString(){
        return getCourseName() + ","+getCourseId() + ","+getTeacherName() + ","+getSemester()+","+getMark();
    }
    public static String headColumn(){
        return "Course Name,Course ID,Teacher Name,Semester,Course Mark";
    }
    public String getCourseName() {
        return courseName.get();
    }

    public SimpleStringProperty courseNameProperty() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public String getTeacherName() {
        return teacherName.get();
    }

    public SimpleStringProperty teacherNameProperty() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName.set(teacherName);
    }
    public SimpleDoubleProperty calculateGpa(double mark) {
        if (mark >= 90) {
            return new SimpleDoubleProperty(4.0);
        } else if (mark >= 80) {
            return new SimpleDoubleProperty(3.0);
        } else if (mark >= 70) {
            return new SimpleDoubleProperty(2.0);
        } else if (mark >= 60) {
            return new SimpleDoubleProperty(1.0);
        } else {
            return new SimpleDoubleProperty(0);
        }
    }
    public static double averageGPAOf(TableView<Course> table){
        // Calculate and display GPA
        double totalGpa = 0;
        int count = 0;
        for (Course c : table.getItems()) {
            totalGpa += c.getGpa();
            count++;
        }
        return count > 0 ? totalGpa / count : 0;
    }
    public String getCourseId() {
        return courseId.get();
    }

    public SimpleStringProperty courseIdProperty() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId.set(courseId);
    }

    public int getSemester() {
        return semester.get();
    }

    public SimpleIntegerProperty semesterProperty() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester.set(semester);
    }

    public double getMark() {
        return mark.get();
    }

    public SimpleDoubleProperty markProperty() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark.set(mark);
    }

    public double getGpa() {
        return gpa.get();
    }

    public SimpleDoubleProperty gpaProperty() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa.set(gpa);
    }
}