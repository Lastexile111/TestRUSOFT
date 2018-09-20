package ru.rusoft.example.dao;

import ru.rusoft.example.model.Car;
import ru.rusoft.example.model.Client;

import java.util.Date;

public interface CarsDAO {

    void add(Car car);

    void detachClient(Car car);
//    Car find(int id);

    Car findFreeCar(String label, Date manufactureData);
    Car getAndCheckClientCar(Client client, String label);
}
