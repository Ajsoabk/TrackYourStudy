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
        CoursePage coursePage=new CoursePage(primaryStage);
        ProfilePage profilePage=new ProfilePage(primaryStage);
        Pane page1=profilePage.getPage();
        Pane page2=coursePage.getPage();
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
}