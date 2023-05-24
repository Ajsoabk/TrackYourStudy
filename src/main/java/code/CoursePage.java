package code;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoursePage {
    private final JFXComboBox<Integer> semesterInput=new JFXComboBox<>();
    private final Label gpaLabel=new Label();
    private final List<Course> courseList = new ArrayList<>();
    private final TableView<Course> courseTable = new TableView<>();

    // Define the input fields as instance variables
    private final TextField courseNameField = new TextField();
    private final TextField courseIdField = new TextField();
    private final TextField teacherNameField = new TextField();
    private final TextField courseMarkField = new TextField();

    private final Label errorMsg = new Label();

    private final StackPane page = new StackPane();
    public CoursePage(Stage primaryStage){

        Button chooseFileButton = new Button("Choose CSV File");
        chooseFileButton.setOnAction(event -> {
            try {
                chooseCSVFile(primaryStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Create a button to export the table as a CSV file
        Button exportFileButton = new Button("Export as CSV");
        exportFileButton.setOnAction(event -> {
            try {
                exportCourseData(courseList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        courseNameField.setPromptText("Course Name");
        courseIdField.setPromptText("Course Id");
        teacherNameField.setPromptText("Teacher name");
        semesterInput.setPromptText("Semester");
        semesterInput.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8));
        courseMarkField.setPromptText("Mark");
        courseMarkField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                courseMarkField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Add button
        JFXButton addButton = new JFXButton("Add");
        addButton.setOnAction(event -> {
            try {
                addCourseButtonClicked();
            } catch (InputFieldException e) {
                errorMsg.setText(e.getMessage());
                errorMsg.setTextFill(Color.rgb(255,0,0));
            }
        });
        // Delete button
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        JFXButton deleteButton = new JFXButton("Delete");
        deleteButton.setOnAction(event -> {
            try {
                deleteCourseButtonClicked();
                errorMsg.setText("");
            } catch (InputFieldException e) {
                errorMsg.setText(e.getMessage());
                errorMsg.setTextFill(Color.rgb(255,0,0));
            }
        });
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        // Button HBox
        HBox courseInfo_Add_DeleteBox = new HBox();
        courseInfo_Add_DeleteBox.setPadding(new Insets(10));
        courseInfo_Add_DeleteBox.setSpacing(10);
        courseInfo_Add_DeleteBox.getChildren().addAll(courseNameField, courseIdField, teacherNameField,semesterInput, courseMarkField, addButton, deleteButton, gpaLabel);
        VBox courseInfo_Add_Delete_ErrorBox = new VBox();
        courseInfo_Add_Delete_ErrorBox.setPadding(new Insets(10));
        courseInfo_Add_Delete_ErrorBox.setSpacing(10);
        courseInfo_Add_Delete_ErrorBox.getChildren().addAll(courseInfo_Add_DeleteBox,errorMsg);
        courseTable.setItems(getCourses());
        displayCourseTable();
        // BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(courseTable);
        borderPane.setBottom(courseInfo_Add_Delete_ErrorBox);

        // VBox
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(borderPane);

        // Create an HBox to hold the buttons
        HBox import_ExportBox = new HBox(10);
        import_ExportBox.setAlignment(Pos.CENTER);
        import_ExportBox.getChildren().addAll(chooseFileButton, exportFileButton);

        // Create a VBox to hold the input box, button box, and table
        VBox tableBox = new VBox(10);
        tableBox.setAlignment(Pos.CENTER);
        tableBox.getChildren().addAll(import_ExportBox, vBox);
        page.getChildren().add(tableBox);
    }

    public StackPane getPage() {
        return page;
    }

    public void deleteCourseButtonClicked() throws InputFieldException {
        if(courseTable.getSelectionModel().getSelectedItems().isEmpty()){
            throw new InputFieldException("Please select a row to delete");
        }
        ObservableList<Course> selectedCourses, allCourses;
        allCourses = courseTable.getItems();
        selectedCourses = courseTable.getSelectionModel().getSelectedItems();

        selectedCourses.forEach(allCourses::remove);

        gpaLabel.setText("Average GPA: " + String.format("%.2f", Course.averageGPAOf(courseTable)));
    }
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]+[.]{0,1}[0-9]*[dD]{0,1}");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
    public void addCourseButtonClicked() throws InputFieldException {
        if(courseNameField.getText().isEmpty()){
            throw new InputFieldException("course Name field not completed");
        }
        if(courseIdField.getText().isEmpty()){
            throw new InputFieldException("course Id field not completed");
        }
        else {
            for(Course c:courseTable.getItems()){
                if(Objects.equals(c.getCourseId(), courseIdField.getText())){
                    throw new InputFieldException("course Id should be unique");
                }
            }
        }
        if(semesterInput.getSelectionModel().isEmpty()){
            throw new InputFieldException("Please select a semester");
        }
        if(teacherNameField.getText().isEmpty()){
            throw new InputFieldException("Please fill in the teacher name");
        }
        if(!courseMarkField.getText().isEmpty()&&!isNumeric(courseMarkField.getText())){
            throw new InputFieldException("Please fill in an number in the Mark field");
        }

        String courseName = courseNameField.getText();
        String courseId = courseIdField.getText();
        int semester = semesterInput.getValue();
        String teacherName = teacherNameField.getText();
        String courseMark = courseMarkField.getText();

        Course course;
        if(courseMark.isEmpty()){
            course=new Course(courseName,courseId,teacherName,semester,0,true);
        }
        else {
            course=new Course(courseName,courseId,teacherName,semester,Double.parseDouble(courseMark),false);
        }
        courseTable.getItems().add(course);

        courseNameField.clear();
        courseIdField.clear();
        teacherNameField.clear();
        semesterInput.getSelectionModel().clearSelection();
        courseMarkField.clear();

        gpaLabel.setText("Average GPA:"+String.format("%.2f",Course.averageGPAOf(courseTable)));
    }
    public ObservableList<Course> getCourses() {
        ObservableList<Course> courses = FXCollections.observableArrayList();
        courses.add(new Course("Physics", "PHY101", "Mr Wang",1, 87,false));
        courses.add(new Course( "Mathematics", "MAT101", "Prof Lee",1, 92,false));
        courses.add(new Course("Chemistry", "CHE101", "Prof Six",1, 78,false));
        courses.add(new Course("English", "ENG101", "Liu ye",1, 85,false));
        courses.add(new Course( "History", "HIS101", "Jack",1, 91,false));
        return courses;
    }
    private void chooseCSVFile(Stage primaryStage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            readCourseData(selectedFile);
            displayCourseTable();
        }
    }
    private void readCourseData(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        courseList.clear();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");

            if(Objects.equals(parts[0], "Course Name")){
                continue;
            }
            String courseName = parts[0];
            String courseId = parts[1];
            String teacherName = parts[2];
            int semester = Integer.parseInt(parts[3]);
            if(parts.length==4){
                courseList.add(new Course(courseName,courseId,teacherName,semester,0,true));
            }
            else{
                courseList.add(new Course(courseName,courseId,teacherName,semester,Double.parseDouble(parts[4]),false));
            }
        }
        scanner.close();
    }
    private void displayCourseTable() {
        // Clear the table
        courseTable.getItems().clear();
        courseTable.getColumns().clear();

        // Create TableColumn objects for each column in the table
        TableColumn<Course, String> courseNameColumn = new TableColumn<>("Course Name");
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        TableColumn<Course, String> courseIdColumn = new TableColumn<>("Course ID");
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));

        TableColumn<Course, String> teacherNameColumn = new TableColumn<>("Teacher Name");
        teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherName"));

        TableColumn<Course, Integer> semesterColumn = new TableColumn<>("Semester");
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));


        TableColumn<Course, Double> courseMarkColumn = new TableColumn<>("Course Mark");
        courseMarkColumn.setCellValueFactory(new PropertyValueFactory<>("mark"));

        TableColumn<Course, Double> GPAcolumn = new TableColumn<>("GPA");
        GPAcolumn.setCellValueFactory(new PropertyValueFactory<>("Gpa"));

        // Add the columns to the table
        courseTable.getColumns().addAll(courseNameColumn, courseIdColumn, teacherNameColumn, semesterColumn,courseMarkColumn, GPAcolumn);

        // Add the course data to the table
        courseTable.getItems().addAll(courseList);
        courseTable.getItems().clear();

        for (Course course : courseList) {
            courseTable.getItems().add(course);
        }
    }
    private void exportCourseData(List<Course> courseList) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Course Data");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            FileWriter writer = new FileWriter(file);
            // Write the header row
            writer.write(Course.headColumn()+"\n");
            // Write the data rows
            for (Course course : courseTable.getItems()) {
                writer.write(course+"\n");
            }
            writer.close();
        }
    }
}
