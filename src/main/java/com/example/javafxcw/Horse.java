package com.example.javafxcw;

import java.io.*;

import javafx.collections.ObservableList;
import javafx.scene.image.Image;


public class Horse implements Serializable {
    private static final long serialVersionUID = 1L; // required for serialization

    private String horseID;
    private String horseName;
    private String jockeyName;
    private int age;
    private String breed;
    private String raceRecord;
    private String group;
    private transient Image horseImage; // transient fields are not serialized

    public String getHorseID() {
        return horseID;
    }

    public void setHorseID(String horseID) {
        this.horseID = horseID;
    }

    public String getHorseName() {
        return horseName;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public String getJockeyName() {
        return jockeyName;
    }

    public void setJockeyName(String jockeyName) {
        this.jockeyName = jockeyName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getRaceRecord() {
        return raceRecord;
    }

    public void setRaceRecord(String raceRecord) {
        this.raceRecord = raceRecord;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Image getHorseImage() {
        return horseImage;
    }

    public void setHorseImage(Image horseImage) {
        this.horseImage = horseImage;
    }

    public Horse(String horseID, String horseName, String jockeyName, int age, String breed, String raceRecord, String group, Image horseImage) {
        this.horseID = horseID;
        this.horseName = horseName;
        this.jockeyName = jockeyName;
        this.age = age;
        this.breed = breed;
        this.raceRecord = raceRecord;
        this.group = group;
        this.horseImage = horseImage;
    }

    // Getters and setters

    public static void saveHorseListToFile(ObservableList<Horse> horseList, String fileName) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            for (Horse horse : horseList) {
                out.writeObject(horse);
            }
            out.close();
            fileOut.close();
            System.out.println("Horses list saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}