package ru.rusoft.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.rusoft.example.dao.CarsDAO;
import ru.rusoft.example.dao.ClientsDAO;
import ru.rusoft.example.model.Car;
import ru.rusoft.example.model.Client;

import javax.persistence.EntityManager;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ExampleApplicationTests {

	@Autowired
	private EntityManager em;

	@Autowired
	private ClientsDAO clientsDAO;

	@Autowired
	private CarsDAO carsDAO;

	@Test
	public void carAdd() {
		Car car = new Car("test", new Date() );
		carsDAO.add(car);

		assertEquals(car, em.find(Car.class, 1));
	}


	@Test
	public void freeCarSearchTest() {
		Date now = new Date();
		Car car = new Car("test", now);

		carsDAO.add(car);

		assertEquals(car, carsDAO.findFreeCar("test",now));
	}



	@Test
	public void clientAddTest() {
		Date now = new Date();
		Car car = new Car("test", now);
		Client client = new Client("tester", now,car);
		carsDAO.add(car);
		clientsDAO.add(client);

		assertEquals(client, em.find(Client.class, "tester"));
	}

	@Test
	public void findClientByLoginTest() {
		Date now = new Date();
		Car car = new Car("test", now);
		Client client = new Client("tester", now,car);
		carsDAO.add(car);
		clientsDAO.add(client);

		assertEquals(client, clientsDAO.findByLogin("tester"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void doubleAdditionClientExceptionTest() {
		Date now = new Date();
		Car car = new Car("test", now);
		Client client = new Client("tester", now,car);
		Client client2 = client;
		carsDAO.add(car);
		clientsDAO.add(client);

		clientsDAO.add(client2);
	}




	@Test(expected = IllegalArgumentException.class)
	public void checkClientCarExceptionTest() {
		Date now = new Date();
		Car car = new Car("test", now);
		Car car2 = new Car("test2", now);
		Client client = new Client("tester", now,car);
		carsDAO.add(car);
		clientsDAO.add(client);
		carsDAO.add(car2);

		carsDAO.getAndCheckClientCar(client,"test2");
	}

	@Test(expected = IllegalArgumentException.class)
	public void dontHaveAnyFreeCarsExceptionTest(){
		Date now = new Date();
		Car car = new Car("test", now);
		Client client = new Client("tester", now,car);
		carsDAO.add(car);
		clientsDAO.add(client);
		carsDAO.findFreeCar("test",now);
	}

	@Test
	public void detachCarTest() {
		Date now = new Date();
		Car car = new Car("test", now);
		Client client = new Client("tester", now,car);
		carsDAO.add(car);
		clientsDAO.add(client);

		carsDAO.detachClient(car);


		assertEquals(null, em.find(Car.class, 1).getOwner());
	}

}
