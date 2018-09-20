package ru.rusoft.example.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rusoft.example.model.Client;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class ClientsDAOImpl implements ClientsDAO {


    private EntityManager em;
    @Autowired
    public ClientsDAOImpl(EntityManager em) {
        this.em = em;
    }


    @Override
    public void add(Client client) {

        if(findByLogin(client.getLogin())!= null){
            throw new IllegalArgumentException("Такой пользователь уже существует");
        }

        em.getTransaction().begin();
        try {
            em.persist(client);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void delete(Client client) {

        if(findByLogin(client.getLogin())== null){
            throw new IllegalArgumentException("Такой пользователь не существует");
        }

        em.getTransaction().begin();

        try{
            em.remove(client);
            em.getTransaction().commit();

        } catch (PersistenceException e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public Client findByLogin(String login)  {
        return em.find(Client.class, login);
    }
}
