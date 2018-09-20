package ru.rusoft.example.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rusoft.example.model.Car;
import ru.rusoft.example.model.Client;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.List;

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
    public void detachClient(Car car) {

        em.getTransaction().begin();
        try {
            car.setOwner(null);
            em.getTransaction().commit();

        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

//    @Override
//    public Car findById(int id) {
//        return em.find(Car.class, id);
//    }

    public Car findFreeCar(String label, Date manufactureDate){

        List<Car> freeCars = em.createQuery("SELECT c FROM Car c WHERE c.label = :label AND c.manufactureDate = :manufactureDate AND c.owner = null")
                .setParameter("label", label)
                .setParameter("manufactureDate", manufactureDate)
                .getResultList();
        return freeCars.get(1);
    }

    public Car getAndCheckClientCar(Client client,String label){
        Car ownedCar = client.getOwnedCar();
        if((ownedCar.getLabel() != label)||(ownedCar.getOwner() != client)){
            throw new IllegalArgumentException("Клиенту не назначена данная машина");
        }
        return ownedCar;
    }



}
