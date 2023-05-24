package code;

import javafx.beans.property.SimpleBooleanProperty;
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
    private final SimpleBooleanProperty studying;

    public Course(String name, String courseId,String teacher, int semester, double mark,boolean studying) {
        this.courseName = new SimpleStringProperty(name);
        this.courseId = new SimpleStringProperty(courseId);
        this.teacherName=new SimpleStringProperty(teacher);
        this.semester = new SimpleIntegerProperty(semester);
        this.studying = new SimpleBooleanProperty(studying);
        if(!this.studying.get()){
            this.mark = new SimpleDoubleProperty(mark);
            this.gpa = new SimpleDoubleProperty(calculateGpa(mark));
        }
        else{
            this.mark=new SimpleDoubleProperty();
            this.gpa=new SimpleDoubleProperty();
        }
    }
    @Override
    public String toString(){
        if(isStudying()){
            return getCourseName() + ","+getCourseId() + ","+getTeacherName() + ","+getSemester();
        }
        return getCourseName() + ","+getCourseId() + ","+getTeacherName() + ","+getSemester()+","+getMark()+","+getGpa();
    }

    public void setMark(double mark) {
        this.mark.set(mark);
    }

    public boolean isStudying() {
        return studying.get();
    }

    public SimpleBooleanProperty studyingProperty() {
        return studying;
    }

    public void setStudying(boolean studying) {
        this.studying.set(studying);
    }

    public static String headColumn(){
        return "Course Name,Course ID,Teacher Name,Semester,Course Mark,Gpa";
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
    public double calculateGpa(double mark) {
        if(mark<60){
            return 0;
        }
        return mark/25;
    }
    public static double averageGPAOf(TableView<Course> table){
        // Calculate and display GPA
        double totalGpa = 0;
        int count = 0;
        for (Course c : table.getItems()) {
            if(c.getGpa()>0){
                totalGpa += c.getGpa();
                count++;
            }
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