package ru.rusoft.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import ru.rusoft.example.dao.CarsDAO;
import ru.rusoft.example.dao.ClientsDAO;
import ru.rusoft.example.model.Car;
import ru.rusoft.example.model.Client;
import ru.rusoft.example.web.ClientAddFormBean;
import ru.rusoft.example.web.ClientDeleteFormBean;
import ru.rusoft.example.web.InWorkConfiguration;

import javax.persistence.EntityManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WebAppConfiguration
public class WorkWithClientControllerTest {

    @Autowired
    private ClientsDAO clientsDAO;

    @Autowired
    private CarsDAO carsDAO;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;



    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void addClientRequestTest() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String dateInString = "2013";
        Date date = format.parse(dateInString);

        Car car = new Car("test", date);
        carsDAO.add(car);

        ClientAddFormBean addFormBean = new ClientAddFormBean("tester",
                "1992",
                "test",
                "2013" );

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/main")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addFormBean))
        ).andExpect(status().isOk())
                .andExpect(content().string("object created"));
    }

    //Такой пользователь уже существует
    @Test(expected = NestedServletException.class)
    public void addClientRequestExceptionTest() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String dateInString = "2013";
        Date date = format.parse(dateInString);

        Car car = new Car("test", date);
        carsDAO.add(car);
        Client client = new Client("tester",date, car);
        clientsDAO.add(client);

        ClientAddFormBean addFormBean = new ClientAddFormBean("tester",
                "1992",
                "test",
                "2013" );

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/main")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addFormBean))
        ).andExpect(status().isBadRequest());
    }


    @Test
    public void deleteClientRequestExceptionTest() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String dateInString = "2013";
        Date date = format.parse(dateInString);

        Car car = new Car("test", date);
        carsDAO.add(car);
        Client client = new Client("tester",date, car);
        clientsDAO.add(client);

        ClientDeleteFormBean deleteFormBean = new ClientDeleteFormBean("tester", "test" );

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/main")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteFormBean))
        ).andExpect(status().isOk())
                .andExpect(content().string("object deleted"));
    }

    //Клиенту не назначена данная машина
    @Test(expected = AssertionError.class)
    public void deleteClientRequestTest() throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String dateInString = "2013";
        Date date = format.parse(dateInString);

        Car car = new Car("test", date);
        carsDAO.add(car);
        Car car1 = new Car("test2", date);
        carsDAO.add(car1);


        Client client = new Client("tester",date, car);
        clientsDAO.add(client);

        ClientDeleteFormBean deleteFormBean = new ClientDeleteFormBean("tester", "test2" );

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/main")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deleteFormBean))
        ).andExpect(status().isBadRequest());
    }
}
