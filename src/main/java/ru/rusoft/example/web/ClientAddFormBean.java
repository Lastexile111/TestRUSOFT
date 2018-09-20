package ru.rusoft.example.web;

import javax.validation.constraints.Size;

public class ClientAddFormBean {
    @Size(min = 3, max = 50)
    private String login = "";

    @Size(min = 4, max = 4)
    private String birthYear = "";

    @Size(min = 2, max = 50)
    private String label = "";

    @Size(min = 4, max = 4)
    private String manufactureDate = "";

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }
}
