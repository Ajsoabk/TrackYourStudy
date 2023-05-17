package code;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class HelloController extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a grid pane to hold the buttons and descriptions
        // Create the buttons with icons and add them to the grid pane

        JFXButton profileItem = createButtonWithIcon("icon1.png","Profile");
        JFXButton coursesItem = createButtonWithIcon("icon2.png","Courses");
        JFXButton settingItem = createButtonWithIcon("icon3.png","Setting");

        VBox navigationBar = new VBox(profileItem,coursesItem,settingItem);
        // Create a stack pane to hold the descriptions
        StackPane pageStackPane = new StackPane();
        pageStackPane.setPadding(new Insets(0, 0, 0, 0));
        pageStackPane.setAlignment(Pos.CENTER);
        // Create the labels with descriptions and add them to the stack pane
        Pane page1=this.createProfilePage(primaryStage);
        Pane page2=this.createCoursePage(primaryStage);
        Label label3 = new Label("This is the description for button 3.");
        label3.setFont(Font.font(20));
        pageStackPane.getChildren().addAll(page1, page2, label3);

        // Hide the descriptions initially
        pageStackPane.setVisible(false);

        // Add event handlers to show/hide the descriptions
        profileItem.setOnAction(event -> {
            pageStackPane.setVisible(true);
            page1.setVisible(true);
            page2.setVisible(false);
            label3.setVisible(false);
        });
        coursesItem.setOnAction(event -> {
            pageStackPane.setVisible(true);
            page1.setVisible(false);
            page2.setVisible(true);
            label3.setVisible(false);
        });
        settingItem.setOnAction(event -> {
            pageStackPane.setVisible(true);
            page1.setVisible(false);
            page2.setVisible(false);
            label3.setVisible(true);
        });

        // Create a VBox to hold the grid pane and stack pane
        HBox hBox = new HBox();
        hBox.getChildren().addAll(navigationBar, pageStackPane);

        // Create a scene and set it on the primary stage
        Scene scene = new Scene(hBox, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private List<Course> courseList = new ArrayList<>();
    private TableView<Course> courseTable = new TableView<>();

    // Define the input fields as instance variables
    private TextField courseNameField = new TextField();
    private TextField courseIdField = new TextField();
    private TextField teacherNameField = new TextField();
    private TextField courseMarkField = new TextField();

    private TableView<Award> table;
    private TextField nameInput;
    private TextField tagInput;
    private TextArea descriptionInput;
    private DatePicker timeInput;
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

    // A method to read the course data from a CSV file
    private void readCourseData(File file) throws IOException {
        Scanner scanner = new Scanner(file);
        courseList.clear();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");

            String courseName = parts[0];
            String courseId = parts[1];
            String teacherName = parts[2];
            double courseMark = Double.parseDouble(parts[3]);

            Course course = new Course(courseName, courseId, teacherName, courseMark);
            courseList.add(course);
        }

        scanner.close();
    }
    private void displayCourseTable() {


        // Clear the table
        courseTable.getItems().clear();
        courseTable.getColumns().clear();


        TableColumn<Course, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Create TableColumn objects for each column in the table
        TableColumn<Course, String> courseNameColumn = new TableColumn<>("Course Name");
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        TableColumn<Course, String> courseIdColumn = new TableColumn<>("Course ID");
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));

        TableColumn<Course, String> teacherNameColumn = new TableColumn<>("Teacher Name");
        teacherNameColumn.setCellValueFactory(new PropertyValueFactory<>("teacherName"));

        TableColumn<Course, Double> courseMarkColumn = new TableColumn<>("Course Mark");
        courseMarkColumn.setCellValueFactory(new PropertyValueFactory<>("courseMark"));

        TableColumn<Course, Double> GPAcolumn = new TableColumn<>("GPA");
        GPAcolumn.setCellValueFactory(new PropertyValueFactory<>("GPA"));

        // Add the columns to the table
        courseTable.getColumns().addAll(idColumn,courseNameColumn, courseIdColumn, teacherNameColumn, courseMarkColumn, GPAcolumn);

        // Add the course data to the table
        courseTable.getItems().addAll(courseList);
        courseTable.getItems().clear();

        for (int i = 0; i < courseList.size(); i++) {
            Course course = courseList.get(i);
            course.setId(i+1);
            courseTable.getItems().add(course);
        }
    }

    // A method to export the course data as a CSV file
    private void exportCourseData(List<Course> courseList) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Course Data");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            FileWriter writer = new FileWriter(file);

            // Write the header row
            writer.write("Course Name,Course ID,Teacher Name,Course Mark,GPA\n");

            // Write the data rows
            for (Course course : courseList) {
                writer.write(course.getCourseName() + ",");
                writer.write(course.getCourseId() + ",");
                writer.write(course.getTeacherName() + ",");
                writer.write(course.getCourseMark() + ",");
                writer.write(course.getGPA() + "\n");
            }

            writer.close();
        }
    }
    private Pane createCoursePage(Stage primaryStage){
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

        // Create a GridPane to hold the input fields
        // Create the input fields and labels
        GridPane inputGrid = new GridPane();
        inputGrid.setAlignment(Pos.CENTER);
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setPadding(new Insets(10, 10, 10, 10));
        inputGrid.add(new Label("Course Name:"), 0, 0);
        inputGrid.add(courseNameField, 1, 0);
        inputGrid.add(new Label("Course ID:"), 0, 1);
        inputGrid.add(courseIdField, 1, 1);
        inputGrid.add(new Label("Teacher Name:"), 0, 2);
        inputGrid.add(teacherNameField, 1, 2);
        inputGrid.add(new Label("Course Mark:"), 0, 3);
        inputGrid.add(courseMarkField, 1, 3);

        // Create a button to add the input as a new row in the table
        Button addButton = new Button("Add Course");
        addButton.setOnAction(event -> {
            addCourse();
        });
        Button deleteButton = new Button("Delete Course");
        deleteButton.setOnAction(event -> {
            Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
            if (selectedCourse != null) {
                deleteCourseById(selectedCourse.getId());
            }
        });
        // Create an HBox to hold the add button
        HBox addButtonBox = new HBox(10);
        addButtonBox.setAlignment(Pos.CENTER);
        addButtonBox.getChildren().addAll(addButton,deleteButton);

        // Create a VBox to hold the input fields and add button
        VBox inputBox = new VBox(10);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.getChildren().addAll(inputGrid, addButtonBox);

        // Create an HBox to hold the buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(chooseFileButton, exportFileButton);

        // Create a VBox to hold the input box, button box, and table
        VBox tableBox = new VBox(10);
        tableBox.setAlignment(Pos.CENTER);
        tableBox.getChildren().addAll(buttonBox, courseTable);

        HBox pageBox = new HBox(10);
        pageBox.setAlignment(Pos.CENTER);
        pageBox.getChildren().addAll(tableBox,inputBox);

        return pageBox;
    }
    private void addCourse(){
        String courseName = courseNameField.getText();
        String courseId = courseIdField.getText();
        String teacherName = teacherNameField.getText();
        String courseMarkString = courseMarkField.getText();

        if (courseName.isEmpty() || courseId.isEmpty() || teacherName.isEmpty() || courseMarkString.isEmpty()) {
            return;
        }

        double courseMark = Double.parseDouble(courseMarkString);

        Course course = new Course(courseName, courseId, teacherName, courseMark);
        courseList.add(course);
        displayCourseTable();
        clearInputFields();
    }

    private void deleteCourseById(int id) {
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).getId() == id) {
                courseList.remove(i);
                break;
            }
        }
        // Update the IDs of the remaining courses
        for (int i = 0; i < courseList.size(); i++) {
            Course course = courseList.get(i);
            course.setId(i+1);
        }
        displayCourseTable();
    }
    private Pane createProfilePage(Stage primaryStage){
        // Create a Button
        Button selectProfilePictureBtn = new Button("Select Profile Picture");

        // Create an ImageView with a default image
        Image defaultImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("profile.png"))); // replace with path to your default image
        ImageView imageView = new ImageView(defaultImage);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        // Set on action for the button to open the FileChooser
        selectProfilePictureBtn.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            }
        });
        Label nameLabel = new Label("Student Name:");
        Label nameField = new Label();
        HBox nameBox=new HBox(nameLabel,nameField);
        Label qmulLabel = new Label("QMUL Number:");
        Label qmulField = new Label();
        HBox qmulBox=new HBox(qmulLabel,qmulField);
        Label buptLabel = new Label("BUPT Number:");
        Label buptField = new Label();
        HBox buptBox=new HBox(buptLabel,buptField);
        Button editProfileBtn = new Button("Edit Profile");
        editProfileBtn.setOnAction(e -> {
            // Create a new Stage for editing student information
            Stage editStage = new Stage();
            editStage.initOwner(primaryStage);
            editStage.initModality(Modality.APPLICATION_MODAL); // Prevent user from interacting with main window while edit window is open
            editStage.setTitle("Edit Profile");

            // Create UI components for editing student information
            Label nameLabel2 = new Label("Student Name:");
            TextField nameField2 = new TextField(nameField.getText());
            Label qmulLabel2 = new Label("QMUL Number:");
            TextField qmulField2 = new TextField(qmulField.getText());
            Label buptLabel2 = new Label("BUPT Number:");
            TextField buptField2 = new TextField(buptField.getText());

            // Create a Button to save changes
            Button saveBtn = new Button("Save");
            saveBtn.setOnAction(event -> {
                // Update student information with new values
                nameField.setText(nameField2.getText());
                qmulField.setText(qmulField2.getText());
                buptField.setText(buptField2.getText());
                editStage.close();
            });

            // Create a Button to cancel changes
            Button cancelBtn = new Button("Cancel");
            cancelBtn.setOnAction(event -> {
                editStage.close();
            });

            // Create a VBox to hold UI components for editing student information
            VBox editBox = new VBox();
            editBox.setSpacing(10);
            editBox.getChildren().addAll(nameLabel2, nameField2, qmulLabel2, qmulField2, buptLabel2, buptField2, saveBtn, cancelBtn);

            // Create a Scene for the edit window and show it
            Scene editScene = new Scene(editBox, 300, 200);
            editStage.setScene(editScene);
            editStage.showAndWait();
        });
        VBox informationBox = new VBox(nameBox,qmulBox,buptBox,editProfileBtn);
        informationBox.setSpacing(10);
        HBox profileInformationBox = new HBox(imageView, informationBox);
        profileInformationBox.setSpacing(10);
        VBox profileBox = new VBox();
        profileBox.setSpacing(10);
        profileBox.getChildren().addAll(selectProfilePictureBtn,profileInformationBox );


        // Time column
        TableColumn<Award, LocalDate> timeColumn = new TableColumn<>("Time");
        timeColumn.setMinWidth(100);
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Name column
        TableColumn<Award, String> nameColumn = new TableColumn<>("Award Name");
        nameColumn.setMinWidth(150);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Tag column
        TableColumn<Award, String> tagColumn = new TableColumn<>("Tag");
        tagColumn.setMinWidth(100);
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));

        // Description column
        TableColumn<Award, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(300);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Inputs
        timeInput = new DatePicker();
        nameInput = new TextField();
        nameInput.setPromptText("Award Name");
        tagInput = new TextField();
        tagInput.setPromptText("Tag");
        descriptionInput = new TextArea();
        descriptionInput.setPromptText("Description");

        // Buttons
        Button addButton = new Button("Add");
        addButton.setOnAction(event -> addButtonClicked());
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteButtonClicked());

        // Button HBox
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);
        buttonBox.getChildren().addAll(timeInput, nameInput, tagInput, descriptionInput, addButton, deleteButton);

        // Table
        table = new TableView<>();
        table.setItems(getAwards());
        table.getColumns().addAll(timeColumn, nameColumn, tagColumn, descriptionColumn);

        // BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(table);
        borderPane.setBottom(buttonBox);

        // Inline CSS
        buttonBox.setStyle("-fx-background-color: white; -fx-border-style: solid; -fx-border-width: 1px; -fx-border-color: black; -fx-padding: 10px;");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        table.setStyle("-fx-border-style: none none solid none; -fx-border-width: 1px; -fx-border-color: black;");

        VBox rootBox=new VBox(profileBox,borderPane);
        return rootBox;
    }
    public void addButtonClicked() {
        if (timeInput.getValue() != null) {
            Award award = new Award();
            award.setTime(timeInput.getValue());
            award.setName(nameInput.getText());
            award.setTag(tagInput.getText());
            award.setDescription(descriptionInput.getText());
            table.getItems().add(award);
            timeInput.setValue(null);
            nameInput.clear();
            tagInput.clear();
            descriptionInput.clear();
        }
    }

    // Delete button clicked
    public void deleteButtonClicked() {
        ObservableList<Award> selectedAwards, allAwards;
        allAwards = table.getItems();
        selectedAwards = table.getSelectionModel().getSelectedItems();
        selectedAwards.forEach(allAwards::remove);
    }

    // Get all of the awards
    public ObservableList<Award> getAwards() {
        ObservableList<Award> awards = FXCollections.observableArrayList();
        awards.add(new Award(LocalDate.of(2023, 5, 10), "Best Actor", "Cinema", "Award for the best acting performance in a leading role"));
        awards.add(new Award(LocalDate.of(2022, 9, 20), "Best Director", "Cinema", "Award for the best directing achievement in a motion picture"));
        awards.add(new Award(LocalDate.of(2021, 11, 5), "Nobel Prize in Physics", "Science", "Award for outstanding contributions to the field of physics"));
        return awards;
    }
    // Helper method to create a button with an icon
    private JFXButton createButtonWithIcon(String iconName, String description) {
        Image iconImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconName)));
        ImageView iconView = new ImageView(iconImg);
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 50px;");
        descLabel.setVisible(false);
        VBox iconBox = new VBox(iconView, descLabel);
        iconBox.setAlignment(Pos.CENTER);
        JFXButton button = new JFXButton();
        button.setGraphic(iconBox);
        button.setScaleX(100 / iconImg.getWidth());
        button.setScaleY(100 / iconImg.getHeight());
        button.focusedProperty().addListener((observable, oldValue, newValue) -> descLabel.setVisible(newValue));
        return button;
    }
    public static void main(String[] args) {
        launch(args);
    }
    private void clearInputFields() {
        courseNameField.clear();
        courseIdField.clear();
        teacherNameField.clear();
        courseMarkField.clear();
    }
}