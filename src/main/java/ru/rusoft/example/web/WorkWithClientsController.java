package ru.rusoft.example.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.rusoft.example.dao.CarsDAO;
import ru.rusoft.example.dao.ClientsDAO;
import ru.rusoft.example.model.Car;
import ru.rusoft.example.model.Client;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/main")
@Api(value = "/main", description = "Main Menu operations ")
public class WorkWithClientsController {

    @Autowired
    private EntityManager em;

    @Autowired
    private ClientsDAO clientsDAO;

    @Autowired
    private CarsDAO carsDAO;

    SimpleDateFormat format = new SimpleDateFormat("yyyy");



   @GetMapping
   @ApiOperation(value = "Show main menu")
    public String mainMenuPage(){
        //Позже дописать

       return "Main menu";

    }

    @ApiOperation(value = "Add Client")
//    @ApiImplicitParams(
//            {
//                    @ApiImplicitParam(name = "login", dataType = "String", required = true, paramType = "query"),
//                    @ApiImplicitParam(name = "birthYear", dataType = "String", required = true, paramType = "query"),
//                    @ApiImplicitParam(name = "label", dataType = "String", required = true, paramType = "query"),
//                    @ApiImplicitParam(name = "manufactureDate", dataType = "String", required = true, paramType = "query")
//            }
//    )
    @PostMapping
    public String addClientPostForm(@RequestBody ClientAddFormBean addFormBean ){
        Date birthYear = null;
        Date manufactureDate = null;


        if (addFormBean.getLogin() == null) {
            throw new IllegalArgumentException("Поле Login пустое");
        }

        if (addFormBean.getLabel()== null) {
            throw new IllegalArgumentException("Поле Label пустое");

        }


        try {
            birthYear = format.parse(addFormBean.getBirthYear());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            manufactureDate = format.parse(addFormBean.getManufactureDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }



        Client client = new Client(addFormBean.getLogin(),birthYear
                ,carsDAO.findFreeCar(addFormBean.getLabel(),manufactureDate));

        try {
            clientsDAO.add(client);
        }catch(IllegalArgumentException e) {
            e.printStackTrace();
        }


        return "object created";
    }


    @ApiOperation(value = "Delete Client")
//    @ApiImplicitParams(
//            {
//                    @ApiImplicitParam(name = "login", dataType = "String", required = true, paramType = "query"),
//                    @ApiImplicitParam(name = "label", dataType = "String", required = true, paramType = "query")
//            }
//    )
    @DeleteMapping
    public String deleteClientPostForm(@RequestBody ClientDeleteFormBean deleteFormBean){

        if (deleteFormBean.getLogin() == null) {
            throw new IllegalArgumentException("Поле Login пустое");
        }

        if (deleteFormBean.getLabel() == null) {
        throw new IllegalArgumentException("Поле Lebel пустое");
        }



        try {
            Client client = clientsDAO.findByLogin(deleteFormBean.getLogin());

            carsDAO.detachClient(carsDAO.getAndCheckClientCar(client, deleteFormBean.getLabel()));
            clientsDAO.delete(client);
        }catch(IllegalArgumentException e) {
            e.printStackTrace();
        }




        return "object deleted";
    }

}
