package ru.rusoft.example.dao;

import ru.rusoft.example.model.Car;

import java.util.Date;

public interface CarsDAO {

    void add(Car car);

    Car find(int id);
}
