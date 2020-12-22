package hackermancool;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class App extends Application {
    Stage window;
    Scene titleScene, builderScene;
    ComboBox<String> featuresComboBox;
    TextField primaryField, secondaryField;
    Label primaryFieldLabel, secondaryFieldLabel;

    ArrayList<Feature> features;

    @Override
    public void start(Stage primaryStage) throws Exception {
        features = new ArrayList<>();
        window = primaryStage;
        window.setTitle("VirusBuilder Plus");

        // Menu bar
        MenuBar mainMenu = new MenuBar();

        // File menu
        Menu fileMenu = new Menu("_File");
        MenuItem newVirusMenuItem = new MenuItem("_New Virus...");
        newVirusMenuItem.setOnAction(e -> window.setScene(builderScene));
        SeparatorMenuItem fileSeparator1 = new SeparatorMenuItem();
        MenuItem exitMenuItem = new MenuItem("E_xit");
        exitMenuItem.setOnAction(e -> Platform.exit());
        fileMenu.getItems().addAll(newVirusMenuItem, fileSeparator1, exitMenuItem);

        mainMenu.getMenus().addAll(fileMenu);

        // Title scene
        Label titleLabel = new Label("VirusBuilder Plus");
        Label subtitleLabel = new Label("Created by hackermancool");
        titleLabel.setStyle("-fx-font: 64 arial;");
        subtitleLabel.setStyle("-fx-font: 24 arial;");

        VBox titleTextLayout = new VBox(5);
        titleTextLayout.getChildren().addAll(titleLabel, subtitleLabel);
        titleTextLayout.setPadding(new Insets(5, 5, 5, 5));

        BorderPane titleLayout = new BorderPane();
        titleLayout.setLeft(titleTextLayout);
        titleLayout.setTop(mainMenu);

        titleScene = new Scene(titleLayout, 640, 480);

        // Builder scene
        Label builderLabel = new Label("Builder");
        builderLabel.setStyle("-fx-font: 64 arial;");

        Label featureLabel = new Label("Feature:");
        featuresComboBox = new ComboBox<>();
        featuresComboBox.getItems().addAll(
                "Title",
                "Colour",
                "Text",
                "Open",
                "Create Section",
                "Jump to Section",
                "Infinite Loop",
                "Sleep",
                "Pause"
        );
        featuresComboBox.setOnAction(e -> featureChanged());
        featuresComboBox.getSelectionModel().select(0);

        HBox features = new HBox(5);
        features.getChildren().addAll(featureLabel, featuresComboBox);

        primaryField = new TextField();
        secondaryField = new TextField();
        primaryFieldLabel = new Label();
        secondaryFieldLabel = new Label();
        HBox primary = new HBox(5);
        HBox secondary = new HBox(5);
        primary.getChildren().addAll(primaryFieldLabel, primaryField);
        secondary.getChildren().addAll(secondaryFieldLabel, secondaryField);

        VBox builderFieldLayout = new VBox(10);
        builderFieldLayout.getChildren().addAll(features, primary, secondary);

        Button addButton = new Button("Add Feature");
        addButton.setOnAction(e -> addFeature());
        TextField filenameField = new TextField();
        filenameField.setPromptText("virus.bat");
        Button buildButton = new Button("Build Virus");
        buildButton.setOnAction(e -> buildVirus(filenameField.getText()));

        HBox builderBuildLayout = new HBox(10);
        builderBuildLayout.getChildren().addAll(addButton, filenameField, buildButton);

        BorderPane builderLayout = new BorderPane();
        builderLayout.setPadding(new Insets(0, 20, 20, 20));
        builderLayout.setTop(builderLabel);
        builderLayout.setCenter(builderFieldLayout);
        builderLayout.setBottom(builderBuildLayout);

        builderScene = new Scene(builderLayout);
        featureChanged();

        window.setScene(titleScene);
        window.show();
    }

    private void buildVirus(String filename) {
        StringBuilder virus = new StringBuilder("@echo off\ncls\n");
        boolean loopExists = false;
        String loopLabel = "";

        for(Feature feature : features) {
            virus.append(Feature.actionToCommand(feature.getAction()));
            virus.append(String.join(" ", feature.getArgs()));
            if(feature.getAction() == Action.INFINITE_LOOP) {
                loopExists = true;
                loopLabel = feature.getArgs()[0];
            } else if(feature.getAction() == Action.SLEEP
                    || feature.getAction() == Action.PAUSE)
                virus.append(">nul");
            virus.append("\n");
        }

        if(loopExists) {
            virus.append("goto ");
            virus.append(loopLabel);
            virus.append("\n");
        }

        System.out.println("Virus Built, Contents:");
        System.out.println(virus.toString());

        File virusFile = new File(filename);
        try {
            virusFile.createNewFile();
            FileWriter virusWriter = new FileWriter(virusFile);
            virusWriter.write(virus.toString());
            virusWriter.close();
        } catch(IOException e) {
            System.out.println("Could not create file.");
            e.printStackTrace();
        }
    }

    private void featureChanged() {
        int feature = featuresComboBox.getSelectionModel().getSelectedIndex();

        if(!primaryField.isVisible() || !primaryFieldLabel.isVisible()) {
            primaryField.setVisible(true);
            primaryFieldLabel.setVisible(true);
        }

        switch(feature) {
            case 0:
                primaryFieldLabel.setText("Title:");
                secondaryFieldLabel.setVisible(false);
                primaryField.setPromptText("Virus Title");
                secondaryField.setText("");
                secondaryField.setVisible(false);
                break;
            case 1:
                primaryFieldLabel.setText("Colour (CMD Format):");
                secondaryFieldLabel.setVisible(false);
                primaryField.setPromptText("0a");
                secondaryField.setText("");
                secondaryField.setVisible(false);
                break;
            case 2:
                primaryFieldLabel.setText("Text:");
                secondaryFieldLabel.setVisible(false);
                primaryField.setPromptText("Hello, World!");
                secondaryField.setText("");
                secondaryField.setVisible(false);
                break;
            case 3:
                primaryFieldLabel.setText("Target:");
                secondaryFieldLabel.setText("Arguments:");
                secondaryFieldLabel.setVisible(true);
                primaryField.setPromptText("notepad");
                secondaryField.setPromptText("file.txt");
                secondaryField.setVisible(true);
                break;
            case 4:
            case 5:
            case 6:
                primaryFieldLabel.setText("Section Label:");
                secondaryFieldLabel.setVisible(false);
                primaryField.setPromptText("a, start, section, etc.");
                secondaryField.setText("");
                secondaryField.setVisible(false);
                break;
            case 7:
                primaryFieldLabel.setText("Sleep Duration (Seconds):");
                secondaryFieldLabel.setVisible(false);
                primaryField.setPromptText("5");
                secondaryField.setText("");
                secondaryField.setVisible(false);
                break;
            case 8:
                primaryFieldLabel.setVisible(false);
                secondaryField.setVisible(false);
                primaryField.setText("");
                primaryField.setVisible(false);
                secondaryField.setText("");
                secondaryField.setVisible(false);
                break;
        }
    }

    private void addFeature() {
        int index = featuresComboBox.getSelectionModel().getSelectedIndex();
        Action action = Feature.indexToEnum(index);

        String[] args = new String[2];
        args[0] = primaryField.getText();
        args[1] = secondaryField.getText();

        features.add(new Feature(action, args));

        System.out.println("Added feature: "
                + featuresComboBox.getSelectionModel().getSelectedItem());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
