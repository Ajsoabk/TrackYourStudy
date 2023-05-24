package code;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private JFXComboBox<Integer> semesterInput;
    private Label gpaLabel;
    private List<Course> courseList = new ArrayList<>();
    private TableView<Course> courseTable = new TableView<>();

    // Define the input fields as instance variables
    private TextField courseNameField = new TextField();
    private TextField courseIdField = new TextField();
    private TextField teacherNameField = new TextField();
    private TextField courseMarkField = new TextField();

    private TableView<Award> awardTable;
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
            int semester = Integer.parseInt(parts[3]);
            double courseMark = Double.parseDouble(parts[3]);

            Course course = new Course(courseName, courseId, teacherName, semester,courseMark,false);
            courseList.add(course);
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

        for (int i = 0; i < courseList.size(); i++) {
            Course course = courseList.get(i);
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
            writer.write(Course.headColumn());
            // Write the data rows
            for (Course course : courseList) {
                writer.write(course+"\n");
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
        courseNameField = new TextField();
        courseNameField.setPromptText("Course Name");

        courseIdField = new TextField();
        courseIdField.setPromptText("Course Id");

        teacherNameField = new TextField();
        teacherNameField.setPromptText("Teacher name");

        semesterInput = new JFXComboBox<>();
        semesterInput.setPromptText("Semester");
        semesterInput.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8));

        courseMarkField = new TextField();
        courseMarkField.setPromptText("Mark");
        courseMarkField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                courseMarkField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        gpaLabel = new Label();

        // Add button
        JFXButton addButton = new JFXButton("Add");
        Label errorMsg = new Label();

        addButton.setOnAction(event -> {
            try {
                addCourseButtonClicked();
                errorMsg.setText("");
            } catch (InputFieldException e) {
                errorMsg.setText(e.getMessage());
                errorMsg.setTextFill(Color.rgb(255,0,0));
            }
        });
        // Delete button
        JFXButton deleteButton = new JFXButton("Delete");
        deleteButton.setOnAction(event -> deleteCourseButtonClicked());
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
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

        return tableBox;
    }
    public void deleteCourseButtonClicked() {
        ObservableList<Course> selectedCourses, allCourses;
        allCourses = courseTable.getItems();
        selectedCourses = courseTable.getSelectionModel().getSelectedItems();

        selectedCourses.forEach(allCourses::remove);

        gpaLabel.setText("Average GPA: " + String.format("%.2f", Course.averageGPAOf(courseTable)));
    }
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]+[.]{0,1}[0-9]*[dD]{0,1}");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
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

        nameInput.clear();
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
    public void deleteAwardButtonClicked() {
        ObservableList<Award> selectedAwards, allAwards;
        allAwards = awardTable.getItems();
        selectedAwards = awardTable.getSelectionModel().getSelectedItems();
        selectedAwards.forEach(allAwards::remove);
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
        addButton.setOnAction(event -> addAwardButtonClicked());
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> deleteAwardButtonClicked());

        // Button HBox
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);
        buttonBox.getChildren().addAll(timeInput, nameInput, tagInput, descriptionInput, addButton, deleteButton);

        // Table
        awardTable = new TableView<>();
        awardTable.setItems(getAwardsExample());
        awardTable.getColumns().addAll(timeColumn, nameColumn, tagColumn, descriptionColumn);

        // BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(awardTable);
        borderPane.setBottom(buttonBox);

        // Inline CSS
        buttonBox.setStyle("-fx-background-color: white; -fx-border-style: solid; -fx-border-width: 1px; -fx-border-color: black; -fx-padding: 10px;");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        awardTable.setStyle("-fx-border-style: none none solid none; -fx-border-width: 1px; -fx-border-color: black;");

        VBox rootBox=new VBox(profileBox,borderPane);
        return rootBox;
    }
    public void addAwardButtonClicked() {
        if (timeInput.getValue() != null) {
            Award award = new Award();
            award.setTime(timeInput.getValue());
            award.setName(nameInput.getText());
            award.setTag(tagInput.getText());
            award.setDescription(descriptionInput.getText());
            awardTable.getItems().add(award);
            timeInput.setValue(null);
            nameInput.clear();
            tagInput.clear();
            descriptionInput.clear();
        }
    }

    // Get all of the awards
    public ObservableList<Award> getAwardsExample() {
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