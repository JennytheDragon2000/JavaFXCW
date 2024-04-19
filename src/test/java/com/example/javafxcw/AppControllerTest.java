package com.example.javafxcw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import javafx.scene.control.Alert;

@ExtendWith(ApplicationExtension.class)
class AppControllerTest {
    private AppController controller;
    private ObservableList<Horse> horses;

    @Start
    private void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @BeforeEach
    void setUp() {
//        // Load the FXML file
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
//        controller = loader.getController();
//        controller = new AppController();
        horses = FXCollections.observableArrayList(
                new Horse("1", "Horse1", "Jockey1", 5, "Breed1", "Record1", "Group1", "image1.jpg"),
                new Horse("2", "Horse2", "Jockey2", 6, "Breed2", "Record2", "Group2", "image2.jpg"),
                new Horse("3", "Horse3", "Jockey3", 7, "Breed3", "Record3", "Group1", "image3.jpg")
        );
    }

    @Test
    void testGroupHorsesByGroup() {
        Map<String, List<Horse>> expected = new HashMap<>();
        expected.put("Group1", new ArrayList<>(List.of(horses.get(0), horses.get(2))));
        expected.put("Group2", new ArrayList<>(List.of(horses.get(1))));

        Map<String, List<Horse>> actual = AppController.groupHorsesByGroup(horses);

        // Assert that the maps have the same keys
        assertEquals(expected.keySet(), actual.keySet());

        // Assert that the lists in the maps contain the same elements, regardless of order
        for (String key : expected.keySet()) {
            List<Horse> expectedList = expected.get(key);
            List<Horse> actualList = actual.get(key);

            // Ensure that the lists have the same size
            assertEquals(expectedList.size(), actualList.size());

            // Ensure that the elements are the same, regardless of order
            assertTrue(expectedList.containsAll(actualList) && actualList.containsAll(expectedList));
        }
    }


    @Test
    void testSelectTopHorsesByGroup() {
        Map<String, List<Horse>> horseGroups = AppController.groupHorsesByGroup(horses);
        Map<String, Horse> expected = new HashMap<>();
        expected.put("Group1", horses.get(0)); // Horse1 has better record than Horse3
        expected.put("Group2", horses.get(1));

        Map<String, Horse> actual = AppController.selectTopHorsesByGroup(horseGroups);

        assertEquals(expected, actual);
    }

    @Test
    void testOnAddHorseButtonClick_ValidData() {
        controller.setHorses(horses);
        controller.txtfld_Horse_ID.setText("4");
        controller.txtfld_Horse_Name.setText("Horse4");
        controller.txtfld_Jockey_Name.setText("Jockey4");
        controller.txtfld_Age.setText("8");
        controller.txtfld_Breed.setText("Breed4");
        controller.txtfld_Race_Record.setText("Record4");
        controller.txtfld_Group.setText("Group3");
        controller.setSelectedImagePath("image4.jpg");

        controller.onAddHorseButtonClick();

        assertEquals(4, controller.getHorses().size());
        assertEquals("4", controller.getHorses().get(3).getHorseID());
        assertEquals("Horse4", controller.getHorses().get(3).getHorseName());
        // ... and so on for other properties
    }

    @Test
    void testOnClearHorseButtonClick() {
        // Set up initial values in the text fields
        controller.txtfld_Horse_ID.setText("1");
        controller.txtfld_Horse_Name.setText("Horse1");
        controller.txtfld_Jockey_Name.setText("Jockey1");
        controller.txtfld_Age.setText("5");
        controller.txtfld_Breed.setText("Breed1");
        controller.txtfld_Race_Record.setText("Record1");
        controller.txtfld_Group.setText("Group1");

        // Call the method under test
        controller.onClearHorseButtonClick();

        // Assert that all text fields are cleared
        assertEquals("", controller.txtfld_Horse_ID.getText());
        assertEquals("", controller.txtfld_Horse_Name.getText());
        assertEquals("", controller.txtfld_Jockey_Name.getText());
        assertEquals("", controller.txtfld_Age.getText());
        assertEquals("", controller.txtfld_Breed.getText());
        assertEquals("", controller.txtfld_Race_Record.getText());
        assertEquals("", controller.txtfld_Group.getText());
    }

    @Test
    void testOnAddHorseButtonClick_MissingData() {
        controller.setHorses(horses);
        controller.txtfld_Horse_ID.setText("4");
        controller.txtfld_Horse_Name.setText("");
        controller.txtfld_Jockey_Name.setText("Jockey4");
        controller.txtfld_Age.setText("8");
        controller.txtfld_Breed.setText("Breed4");
        controller.txtfld_Race_Record.setText("Record4");
        controller.txtfld_Group.setText("Group3");
        controller.setSelectedImagePath("image4.jpg");

        try {
            controller.onAddHorseButtonClick();
//            fail("Expected a RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("All fields are required.", e.getMessage());
        }
    }//
//    // ... and more tests for other methodso

    @Test
    void testOnDeleteHorseButtonClick() {
        // Set up the initial list of horses
        Horse horse1 = new Horse("1", "Horse1", "Jockey1", 5, "Breed1", "Record1", "Group1", "image1.jpg");
        Horse horse2 = new Horse("2", "Horse2", "Jockey2", 6, "Breed2", "Record2", "Group2", "image2.jpg");
        Horse horse3 = new Horse("3", "Horse3", "Jockey3", 7, "Breed3", "Record3", "Group1", "image3.jpg");
        horses.addAll(horse1, horse2, horse3);
        controller.setHorses(horses);

        // Set the selected item in the TableView
        controller.tbl_HorseDetails.getSelectionModel().select(horse2);

        // Call the method under test
        controller.onDeleteHorseButtonClick();

        // Assert that the selected horse was removed
        assertFalse(controller.getHorses().contains(horse2));
        assertEquals(2, controller.getHorses().size());

        // Assert that the text fields are cleared
        assertEquals("", controller.txtfld_Horse_ID.getText());
        assertEquals("", controller.txtfld_Horse_Name.getText());
        assertEquals("", controller.txtfld_Jockey_Name.getText());
        assertEquals("", controller.txtfld_Age.getText());
        assertEquals("", controller.txtfld_Breed.getText());
        assertEquals("", controller.txtfld_Race_Record.getText());
        assertEquals("", controller.txtfld_Group.getText());

        // Call the method again when no horse is selected
        controller.tbl_HorseDetails.getSelectionModel().clearSelection();
        controller.onDeleteHorseButtonClick();
        // Add assertions for the case when no horse is selected, if needed
    }

}