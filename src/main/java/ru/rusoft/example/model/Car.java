package ru.rusoft.example.model;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.util.Date;


@Entity
@Table(name = "CARS")
public class Car {

    @Id
    @GeneratedValue
    private int carid;

    @Column(length = 50,unique = true, nullable = false)
    private String label;

    @Column
    @Temporal(TemporalType.DATE)
    @Past
    private Date manufactureDate;

    @OneToOne(mappedBy = "ownedCar", fetch = FetchType.LAZY)
    private Client owner;

    public Car() {
    }

    public Car(String label, Date manufactureDate) {
        this.label = label;
        this.manufactureDate = manufactureDate;
        this.owner = null;
    }

    public int getCarid() {
        return carid;
    }

    public void setCarid(int carid) {
        this.carid = carid;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(Date manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }
}
