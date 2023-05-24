package code;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.Objects;

public class ProfilePage {


    // Define the input fields as instance variables
    private final TableView<Award> awardTable;
    private final TextField nameInput=new TextField();
    private final TextField tagInput=new TextField();
    private final TextArea descriptionInput=new TextArea();
    private final DatePicker timeInput=new DatePicker();
    private final StackPane page=new StackPane();

    private final Label errMsg = new Label();

    public StackPane getPage() {
        return page;
    }

    public void deleteAwardButtonClicked() throws InputFieldException {
        if(awardTable.getSelectionModel().getSelectedItems().isEmpty()){
            throw new InputFieldException("Please select a row of award");
        }
        ObservableList<Award> selectedAwards, allAwards;
        allAwards = awardTable.getItems();
        selectedAwards = awardTable.getSelectionModel().getSelectedItems();
        selectedAwards.forEach(allAwards::remove);
    }

    public ProfilePage(Stage primaryStage){
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
            Scene editScene = new Scene(editBox, 600, 500);
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
        nameInput.setPromptText("Award Name");
        tagInput.setPromptText("Tag");
        descriptionInput.setPromptText("Description");

        // Buttons
        Button addButton = new Button("Add");
        errMsg.setTextFill(Color.rgb(255,0,0));
        addButton.setOnAction(event -> {
            try {
                addAwardButtonClicked();
                errMsg.setText("");
            } catch (InputFieldException e) {
                errMsg.setText(e.getMessage());
            }
        });
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(event -> {
            try {
                deleteAwardButtonClicked();
                errMsg.setText("");
            } catch (InputFieldException e) {
                errMsg.setText(e.getMessage());
            }
        });

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

        VBox rootBox=new VBox(profileBox,borderPane,errMsg);
        page.getChildren().add(rootBox);
    }
    public void addAwardButtonClicked() throws InputFieldException {
        if(timeInput.getValue()==null){
            throw new InputFieldException("Please select a time");
        }
        LocalDate now=LocalDate.now();
        if(timeInput.getValue().isAfter(now)){
            throw new InputFieldException("Please select a time in past");
        }
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
}
