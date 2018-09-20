package ru.rusoft.example.web;

import javax.validation.constraints.Size;

public class ClientDeleteFormBean {
    @Size(min = 3, max = 50)
    private String login = "";

    @Size(min = 2, max = 50)
    private String label = "";

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
