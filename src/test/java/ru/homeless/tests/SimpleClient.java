package ru.homeless.tests;

/**
 * Created by maxim on 06.02.2016.
 */
public class SimpleClient {

    private int id;
    private String surname;
    private String middlename;
    private String firstname;
    private String birthDate;
    private String gender; //'f' or 'm'
    private int homelessFromM;
    private int homelessFromY;
    private String birthPlace;
    private boolean important;

    public SimpleClient(String surname, String middlename, String firstname, String birthDate, String gender, int homelessFromM, int homelessFromY, String birthPlace, boolean important) {
        this.surname = surname;
        this.middlename = middlename;
        this.firstname = firstname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.homelessFromM = homelessFromM;
        this.homelessFromY = homelessFromY;
        this.birthPlace = birthPlace;
        this.important = important;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHomelessFromM() {
        return homelessFromM;
    }

    public void setHomelessFromM(int homelessFromM) {
        this.homelessFromM = homelessFromM;
    }

    public int getHomelessFromY() {
        return homelessFromY;
    }

    public void setHomelessFromY(int homelessFromY) {
        this.homelessFromY = homelessFromY;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return id+" "+ surname+" "+firstname + " "+middlename+" ("+birthDate+")";
    }

}
