package code;

public class Course {

    private int id;

    private String courseName;
    private String courseId;
    private String teacherName;
    private double courseMark;
    private double GPA;

    public Course(String courseName, String courseId, String teacherName, double courseMark) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.teacherName = teacherName;
        this.courseMark = courseMark;
        this.GPA = calculateGPA(courseMark);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public double getCourseMark() {
        return courseMark;
    }

    public void setCourseMark(double courseMark) {
        this.courseMark = courseMark;
    }

    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }

    // A simple method to calculate the GPA based on the course mark
    private double calculateGPA(double courseMark) {
        if (courseMark >= 90) {
            return 4.0;
        } else if (courseMark >= 80) {
            return 3.0;
        } else if (courseMark >= 70) {
            return 2.0;
        } else if (courseMark >= 60) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
}