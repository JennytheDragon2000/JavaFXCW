package com.example.javafxcw;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import java.io.*;
import java.util.*;

public class AppController {
    public Button btn_Race;
    public AnchorPane Horse_Pane;
    public AnchorPane Race_Pane;
    public Button btn_Horse;
    public BarChart<String, Double> barChart;
    public Button btn_start_race;
    public AnchorPane barChart_cart;
    public Button btn_select_horses_by_group;
    public TableView tbl_select_horse_group;
    public TableColumn select_clmn_Horse_ID1;
    public TableColumn select_clmn_Horse_Name1;
    public TableColumn select_clmn_Jockey_Name1;
    public TableColumn select_clmn_Age1;
    public TableColumn select_clmn_Breed1;
    public TableColumn select_Race_Record1;
    public TableColumn select_Group1;
    public TableColumn select_Horse_Image1;
    public AnchorPane select_horses_pane;
    public Button btn_select_horses_pane;
    public TextField txtfld_search;
    public Button btn_logout;
    public ImageView first_place_img;
    public ImageView second_place_img;
    public ImageView third_place_img;

    public ObservableList<Horse> getHorses() {
        return horses;
    }

    public void setHorses(ObservableList<Horse> horses) {
        this.horses = horses;
    }

    private ObservableList<Horse> horses = FXCollections.observableArrayList();
    public TableView tbl_HorseDetails;
    public TableColumn clmn_Horse_ID;
    public TableColumn clmn_Horse_Name;
    public TableColumn clmn_Jockey_Name;
    public TableColumn clmn_Age;
    public TableColumn clmn_Breed;
    public TableColumn clmn_Race_Record;
    public TableColumn clmn_Group;
    public TableColumn clmn_Horse_Image;
    public TextField txtfld_Horse_ID;
    public TextField txtfld_Horse_Name;
    public TextField txtfld_Jockey_Name;
    public TextField txtfld_Age;
    public TextField txtfld_Breed;
    public TextField txtfld_Race_Record;
    public TextField txtfld_Group;
    public AnchorPane anchr_Image;
    public ImageView imgvw_Horse_Image;
    public Button btn_import_Image;
    public Button btn_Add_horse;
    public Button btn_update_horse;
    public Button btn_clear_horse;
    public Button btn_delete_horse;
    Map<String, List<Horse>> horseGroups;
    Map<String, Horse> topHorsesByGroup;

    public String getSelectedImagePath() {
        return selectedImagePath;
    }

    public void setSelectedImagePath(String selectedImagePath) {
        this.selectedImagePath = selectedImagePath;
    }

    private String selectedImagePath;

    private double x;
    private double y;

    private boolean isMouseOverImage = false;



    @FXML
    private void initialize() {
        btn_Horse.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
        loadHorsesFromFile(); // Load the horses on application start

        clmn_Horse_ID.setCellValueFactory(new PropertyValueFactory<>("horseID"));
        clmn_Horse_Name.setCellValueFactory(new PropertyValueFactory<>("horseName"));
        clmn_Jockey_Name.setCellValueFactory(new PropertyValueFactory<>("jockeyName"));
        clmn_Age.setCellValueFactory(new PropertyValueFactory<>("age"));
        clmn_Breed.setCellValueFactory(new PropertyValueFactory<>("breed"));
        clmn_Race_Record.setCellValueFactory(new PropertyValueFactory<>("raceRecord"));
        clmn_Group.setCellValueFactory(new PropertyValueFactory<>("group"));
        clmn_Horse_Image.setCellValueFactory(new PropertyValueFactory<>("imagePath"));



        // Set up table view columns
        select_clmn_Horse_ID1.setCellValueFactory(new PropertyValueFactory<>("horseID"));
        select_clmn_Horse_Name1.setCellValueFactory(new PropertyValueFactory<>("horseName"));
        select_clmn_Jockey_Name1.setCellValueFactory(new PropertyValueFactory<>("jockeyName"));
        select_clmn_Age1.setCellValueFactory(new PropertyValueFactory<>("age"));
        select_clmn_Breed1.setCellValueFactory(new PropertyValueFactory<>("breed"));
        select_Race_Record1.setCellValueFactory(new PropertyValueFactory<>("raceRecord"));
        select_Group1.setCellValueFactory(new PropertyValueFactory<>("group"));
        select_Horse_Image1.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

        // Add listener to TableView selection model
        tbl_HorseDetails.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Update text fields with selected horse's details
                Horse selectedHorse = (Horse) newSelection;
                txtfld_Horse_ID.setText(selectedHorse.getHorseID());
                txtfld_Horse_Name.setText(selectedHorse.getHorseName());
                txtfld_Jockey_Name.setText(selectedHorse.getJockeyName());
                txtfld_Age.setText(String.valueOf(selectedHorse.getAge()));
                txtfld_Breed.setText(selectedHorse.getBreed());
                txtfld_Race_Record.setText(selectedHorse.getRaceRecord());
                txtfld_Group.setText(selectedHorse.getGroup());
                // Update the image view with the selected horse's image
                String imagePath = selectedHorse.getImagePath();
                if (imagePath != null && !imagePath.isEmpty()) {
                    imgvw_Horse_Image.setImage(new Image(imagePath));
                    imgvw_Horse_Image.setPreserveRatio(false);
                    imgvw_Horse_Image.setSmooth(true);
                    imgvw_Horse_Image.setFitWidth(first_place_img.getFitWidth()); // Set the fit width to the ImageView's width
                    imgvw_Horse_Image.setFitHeight(first_place_img.getFitHeight());
                } else {
                    // Set a default image or clear the image view if no image path is available
                    imgvw_Horse_Image.setImage(null);
                }
            }
        });
        // Initialize the barChart with an empty series
        XYChart.Series<String, Double> emptySeries = new XYChart.Series<>();
        barChart.getData().add(emptySeries);


        // Add listener to txtfld_search text property
        txtfld_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTableByHorseID(newValue);
        });


        // Add event listeners to show/hide the import button when mouse enters/exits the image
        imgvw_Horse_Image.setOnMouseEntered(event -> {
            isMouseOverImage = true;
            btn_import_Image.setVisible(true);
        });

        imgvw_Horse_Image.setOnMouseExited(event -> {
            isMouseOverImage = false;
            btn_import_Image.setVisible(false);
        });


        btn_import_Image.setOnMouseEntered(event -> {
            isMouseOverImage = true;
            btn_import_Image.setVisible(true);
        });

        btn_import_Image.setOnMouseExited(event -> {
            isMouseOverImage = false;
            btn_import_Image.setVisible(false);
        });

        onSelectHorsesByGroupButtonClick();
        onStartRaceButtonClick();
    }

    private void filterTableByHorseID(String id) {
        // If the ID is empty, show all horses
        if (id.isEmpty()) {
            tbl_HorseDetails.setItems(horses);
            return;
        }

        // Filter horses based on ID
        ObservableList<Horse> filteredList = FXCollections.observableArrayList();
        for (Horse horse : horses) {
            if (horse.getHorseID().toLowerCase().contains(id.toLowerCase())) {
                filteredList.add(horse);
            }
        }
        tbl_HorseDetails.setItems(filteredList);
    }

    public void logout() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();
        try {
            if (option.get().equals(ButtonType.OK)) {

                Platform.exit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @FXML
    void onClearHorseButtonClick() {
        // Clear all text fields
        txtfld_Horse_ID.clear();
        txtfld_Horse_Name.clear();
        txtfld_Jockey_Name.clear();
        txtfld_Age.clear();
        txtfld_Breed.clear();
        txtfld_Race_Record.clear();
        txtfld_Group.clear();
    }


    @FXML
    private Label welcomeText;

    @FXML
    void onAddHorseButtonClick() {
        String horseID = txtfld_Horse_ID.getText().trim();
        String horseName = txtfld_Horse_Name.getText().trim();
        String jockeyName = txtfld_Jockey_Name.getText().trim();
        String ageText = txtfld_Age.getText().trim();
        String breed = txtfld_Breed.getText().trim();
        String raceRecord = txtfld_Race_Record.getText().trim();
        String group = txtfld_Group.getText().trim();
        String imagePath = selectedImagePath;

        if (horseID.isEmpty() || horseName.isEmpty() || jockeyName.isEmpty() || ageText.isEmpty() || breed.isEmpty() || raceRecord.isEmpty() || group.isEmpty() || imagePath == null) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("All fields are required.");
                alert.showAndWait();
            });
            return;
        }

        // Validate age input
        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Age must be a valid integer.");
                alert.showAndWait();
            });
            return;
        }

        // Check if the horse ID already exists
        boolean idExists = horses.stream().anyMatch(horse -> horse.getHorseID().equals(horseID));

        if (idExists) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Horse with ID " + horseID + " already exists.");
                alert.showAndWait();
            });
            return;
        }

        // If all validations pass, create the horse object and add it to the list
        Horse horse = new Horse(horseID, horseName, jockeyName, age, breed, raceRecord, group, imagePath);
        horses.add(horse); // Append the new horse to the list

        // Update TableView
        tbl_HorseDetails.setItems(horses);

        // Save the updated list to file
        Horse.saveHorseListToFile(horses, "horses.dat");
    }


    public void switchForm(ActionEvent event) {

        if (event.getSource() == btn_Horse) {
            Horse_Pane.setVisible(true);
            Race_Pane.setVisible(false);
            select_horses_pane.setVisible(false);

            btn_Horse.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            btn_Race.setStyle("-fx-background-color:transparent");
            btn_select_horses_pane.setStyle("-fx-background-color:transparent");

        } else if (event.getSource() == btn_Race) {
            Horse_Pane.setVisible(false);
            Race_Pane.setVisible(true);
            select_horses_pane.setVisible(false);

            btn_Race.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            btn_Horse.setStyle("-fx-background-color:transparent");
            btn_select_horses_pane.setStyle("-fx-background-color:transparent");

        } else if (event.getSource() == btn_select_horses_pane){
            Horse_Pane.setVisible(false);
            Race_Pane.setVisible(false);
            select_horses_pane.setVisible(true);

            btn_select_horses_pane.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            btn_Horse.setStyle("-fx-background-color:transparent");
            btn_Race.setStyle("-fx-background-color:transparent");

        }
    }


    private void loadHorsesFromFile() {
        try {
            FileInputStream fileIn = new FileInputStream("horses.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);

            horses.clear();

            while (true) {
                try {
                    Horse horse = (Horse) in.readObject();
                    horses.add(horse);
                } catch (EOFException e) {
                    break;
                }
            }

            in.close();
            fileIn.close();

            // Set the items for the TableView
            tbl_HorseDetails.setItems(horses);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    // Group horses by their group
    static Map<String, List<Horse>> groupHorsesByGroup(List<Horse> horses) {
        Map<String, List<Horse>> horseGroups = new HashMap<>();
        for (Horse horse : horses) {
            horseGroups.computeIfAbsent(horse.getGroup(), k -> new ArrayList<>()).add(horse);
        }
        return horseGroups;
    }

    // Select the top horse from each group with the best time
    static Map<String, Horse> selectTopHorsesByGroup(Map<String, List<Horse>> horseGroups) {
        Map<String, Horse> topHorsesByGroup = new HashMap<>();
        for (Map.Entry<String, List<Horse>> entry : horseGroups.entrySet()) {
            String group = entry.getKey();
            List<Horse> horses = entry.getValue();
            horses.sort(Comparator.comparingDouble(Horse::getRaceTime));
            if (!horses.isEmpty()) {
                topHorsesByGroup.put(group, horses.get(0));
            }
        }
        return topHorsesByGroup;
    }


    @FXML
    private void onImportImageButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        // Set the file chooser to only allow selecting image files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg")
        );

        // Show the open file dialog
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Get the path of the selected image
            selectedImagePath = selectedFile.toURI().toString();
            Image image = new Image(selectedImagePath);

            // Set the image in the ImageView
            imgvw_Horse_Image.setImage(image);

            double originalImageWidth = image.getWidth();
            double originalImageHeight = image.getHeight();
            double targetWidth = 200;
            double targetHeight = 162;

            double aspectRatio = originalImageWidth / originalImageHeight;
            double targetAspectRatio = targetWidth / targetHeight;

            if (aspectRatio > targetAspectRatio) {
                // Image is wider than target, adjust width
                imgvw_Horse_Image.setFitWidth(targetWidth);
                imgvw_Horse_Image.setFitHeight(targetWidth / aspectRatio);
            } else {
                // Image is taller than target, adjust height
                imgvw_Horse_Image.setFitHeight(targetHeight);
                imgvw_Horse_Image.setFitWidth(targetHeight * aspectRatio);
            }
        }

    }

    @FXML
    void onDeleteHorseButtonClick() {
        // Get the selected item from the TableView
        Horse selectedHorse = (Horse) tbl_HorseDetails.getSelectionModel().getSelectedItem();
        if (selectedHorse != null) {
            // Remove the selected horse from the list
            horses.remove(selectedHorse);

            // Update TableView
            tbl_HorseDetails.setItems(horses);

            // Save the updated list to file
            Horse.saveHorseListToFile(horses, "horses.dat");

            // Clear the text fields
            onClearHorseButtonClick();
        } else {
            // Display a message indicating that no horse is selected
            // You can use a dialog box or a label to display the message
        }
    }

    @FXML
    private void onUpdateHorseButtonClick() {
        // Get the selected item from the TableView
        Horse selectedHorse = (Horse) tbl_HorseDetails.getSelectionModel().getSelectedItem();
        if (selectedHorse != null) {
            // Validate all fields before updating
            String horseID = txtfld_Horse_ID.getText().trim();
            String horseName = txtfld_Horse_Name.getText().trim();
            String jockeyName = txtfld_Jockey_Name.getText().trim();
            String ageText = txtfld_Age.getText().trim();
            String breed = txtfld_Breed.getText().trim();
            String raceRecord = txtfld_Race_Record.getText().trim();
            String group = txtfld_Group.getText().trim();
            String imagePath = selectedImagePath;

            // Check if any of the fields are empty
            if (horseID.isEmpty() || horseName.isEmpty() || jockeyName.isEmpty() || ageText.isEmpty() || breed.isEmpty() || raceRecord.isEmpty() || group.isEmpty()) {
                // Display an error message indicating that all fields are required
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("All fields are required.");
                alert.showAndWait();
                return;
            }

            // Validate age input
            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException e) {
                // Display an error message indicating that the age must be an integer
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Age must be a valid integer.");
                alert.showAndWait();
                return;
            }

            // Check if the new horse ID already exists
            if (!horseID.equals(selectedHorse.getHorseID()) && horses.stream().anyMatch(horse -> horse.getHorseID().equals(horseID))) {
                // Display an error message indicating that the horse ID already exists
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Horse with ID " + horseID + " already exists.");
                alert.showAndWait();
                return;
            }

            // Update the details of the selected horse
            selectedHorse.setHorseID(horseID);
            selectedHorse.setHorseName(horseName);
            selectedHorse.setJockeyName(jockeyName);
            selectedHorse.setAge(age);
            selectedHorse.setBreed(breed);
            selectedHorse.setRaceRecord(raceRecord);
            selectedHorse.setGroup(group);

            // Update the image path if a new image was selected
            if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                selectedHorse.setImagePath(selectedImagePath);
            }

            // Refresh the TableView to reflect the changes
            tbl_HorseDetails.refresh();

            // Save the updated list to file
            Horse.saveHorseListToFile(horses, "horses.dat");
        } else {
            // Display a message indicating that no horse is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No horse is selected.");
            alert.showAndWait();
        }
    }


    @FXML
    private void onSelectHorsesButtonClick() {
        horseGroups = groupHorsesByGroup(new ArrayList<>(horses));
        topHorsesByGroup = selectTopHorsesByGroup(horseGroups);

        // Now you can do whatever you want with the top horses by group
        for (Map.Entry<String, Horse> entry : topHorsesByGroup.entrySet()) {
            String group = entry.getKey();
            Horse topHorse = entry.getValue();
            System.out.println("Group: " + group + ", Top Horse: " + topHorse.getHorseName() + ", Race Time: " + topHorse.getRaceTime());
            // Or perform any other action with the top horse, such as displaying it in a UI component
        }
    }

    @FXML
    private void onStartRaceButtonClick() {
        // Sort the top horses by race time
        List<Horse> topHorses = new ArrayList<>(topHorsesByGroup.values());
//        topHorses.sort(Comparator.comparingDouble(Horse::getRaceTime));
        for (Horse horse : topHorses) {
            horse.SetRandomRaceTime();
        }

        topHorses.sort(Comparator.comparingDouble(Horse::getRaceTime));
        // Select the top 3 horses with the least time
        List<Horse> top3Horses = topHorses.subList(0, Math.min(3, topHorses.size()));

        // Print the details of the top 3 horses to the console
        for (Horse horse : top3Horses) {
            System.out.println("Horse ID: " + horse.getHorseID());
            System.out.println("Horse Name: " + horse.getHorseName());
            System.out.println("Race Time: " + horse.getRaceTime());
            System.out.println("------------------------------------");
        }

        // Display the pictures of the top 3 horses on respective ImageView
        if (top3Horses.size() >= 1) {
            Image firstImage = new Image(top3Horses.get(0).getImagePath());
            first_place_img.setImage(firstImage);
            first_place_img.setPreserveRatio(false);
            first_place_img.setSmooth(true);
            first_place_img.setFitWidth(first_place_img.getFitWidth()); // Set the fit width to the ImageView's width
            first_place_img.setFitHeight(first_place_img.getFitHeight()); // Set the fit height to the ImageView's height
        }
        if (top3Horses.size() >= 2) {
            Image secondImage = new Image(top3Horses.get(1).getImagePath());
            second_place_img.setImage(secondImage);
            second_place_img.setPreserveRatio(false);
            second_place_img.setSmooth(true);
            second_place_img.setFitWidth(second_place_img.getFitWidth()); // Set the fit width to the ImageView's width
            second_place_img.setFitHeight(second_place_img.getFitHeight()); // Set the fit height to the ImageView's height
        }
        if (top3Horses.size() >= 3) {
            Image thirdImage = new Image(top3Horses.get(2).getImagePath());
            third_place_img.setImage(thirdImage);
            third_place_img.setPreserveRatio(false);
            third_place_img.setSmooth(true);
            third_place_img.setFitWidth(third_place_img.getFitWidth()); // Set the fit width to the ImageView's width
            third_place_img.setFitHeight(third_place_img.getFitHeight()); // Set the fit height to the ImageView's height
        }
        // Clear existing data from the bar chart
        barChart.getData().clear();

        // Create a single series and add data points to it
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        for (Horse horse : top3Horses) {
            series.getData().add(new XYChart.Data<>(horse.getHorseName(), (double) horse.getRaceTime()));
        }
        barChart.getData().add(series);

    }


    @FXML
    private void onSelectHorsesByGroupButtonClick() {
        onSelectHorsesButtonClick();
        // Clear existing data from the table view
        tbl_select_horse_group.getItems().clear();

        // Select the top horse from each group
        List<Horse> topHorses = new ArrayList<>(topHorsesByGroup.values());

        // Iterate over each group and add its top horse to the table view
        for (Map.Entry<String, Horse> entry : topHorsesByGroup.entrySet()) {
            String group = entry.getKey();
            Horse topHorse = entry.getValue();

            // Add the top horse to the table view
            tbl_select_horse_group.getItems().add(topHorse);
        }
    }



}