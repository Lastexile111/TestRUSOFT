package ru.rusoft.example.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rusoft.example.model.Car;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.Date;

@Service
public class CarsDAOImpl implements CarsDAO {

    private EntityManager em;
    @Autowired
    public CarsDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public void add(Car car) {
        em.getTransaction().begin();
        try {
            em.persist(car);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public Car find(int id) {
        return em.find(Car.class, id);
    }
}
