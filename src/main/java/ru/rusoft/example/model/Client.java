package ru.rusoft.example.model;


import javax.persistence.*;
import javax.validation.constraints.Past;
import java.util.Date;

@Entity
@Table(name = "CLIENTS")
public class Client {

    @Id
    @Column(length = 50,unique = true, nullable = false)
    private String login;

    @Column
    @Temporal(TemporalType.DATE)
    @Past
    private Date birthYear;

    @OneToOne
    private Car ownedCar;

    public Client() {
    }

    public Client(String login, Date birthYear, Car ownedCar) {
        this.login = login;
        this.birthYear = birthYear;
        this.ownedCar = ownedCar;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Date getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Date birthYear) {
        this.birthYear = birthYear;
    }

    public Car getOwnedCar() {
        return ownedCar;
    }

    public void setOwnedCar(Car ownedCar) {
        this.ownedCar = ownedCar;
    }
}
