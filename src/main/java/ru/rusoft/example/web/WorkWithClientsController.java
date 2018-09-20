package ru.rusoft.example.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.rusoft.example.dao.CarsDAO;
import ru.rusoft.example.dao.ClientsDAO;
import ru.rusoft.example.model.Car;
import ru.rusoft.example.model.Client;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class WorkWithClientsController {

    @Autowired
    private EntityManager em;

    @Autowired
    private ClientsDAO clientsDAO;

    @Autowired
    private CarsDAO carsDAO;

    SimpleDateFormat format = new SimpleDateFormat("yyyy");

    @RequestMapping(method = RequestMethod.GET, value = "/main")
    public String mainMenuPage(){
        //Позже дописать
        return null;
    }



    @RequestMapping(method = RequestMethod.GET, value = "/main/add")
        public String addClientShowForm(){ return "/main/add"; }

    @RequestMapping(method = RequestMethod.POST, value = "/main/add")
    public String addClientPostForm(@Valid @ModelAttribute("addFormBean") ClientAddFormBean addFormBean,
                                    BindingResult binding){
        Date birthYear = null;
        Date manufactureDate = null;

        if (addFormBean.getLogin().isEmpty()) {
            binding.addError(new FieldError("addFormBean",
                    "login",
                    "Поле Login пустое"));
        }

        if (addFormBean.getLabel().isEmpty()) {
            binding.addError(new FieldError("addFormBean",
                    "label",
                    "Поле Label пустое"));
        }

        try {
            birthYear = format.parse(addFormBean.getBirthYear());
        } catch (ParseException e) {
            binding.addError(new FieldError("addFormBean",
                    "birthYear",
                    "Неверный формат даты"));
        }

        try {
            manufactureDate = format.parse(addFormBean.getManufactureDate());
        } catch (ParseException e) {
            binding.addError(new FieldError("addFormBean",
                    "manufactureDate",
                    "Неверный формат даты"));
        }

        if (binding.hasErrors()) {
            return "/main/add";
        }


        Client client = new Client(addFormBean.getLogin(),birthYear
                ,carsDAO.findFreeCar(addFormBean.getLabel(),manufactureDate));

        try {
            clientsDAO.add(client);
        }catch(IllegalArgumentException e) {
            binding.addError(new FieldError("addFormBean",
                    "login",
                    "Такой пользователь уже существует"));
        }

        if (binding.hasErrors()) {
            return "/main/add";
        }

        return "redirect:/main";
    }




    @RequestMapping(method = RequestMethod.GET, value = "/main/delete")
    public String deleteClientShowForm(){ return "/main/delete"; }

    @RequestMapping(method = RequestMethod.POST, value = "/main/delete")
    public String deleteClientPostForm(@Valid @ModelAttribute("deleteFormBean") ClientDeleteFormBean deleteFormBean,
                                       BindingResult binding){

        if (deleteFormBean.getLogin().isEmpty()) {
            binding.addError(new FieldError("deleteFormBean",
                    "login",
                    "Поле Login пустое"));
        }

        if (deleteFormBean.getLabel().isEmpty()) {
            binding.addError(new FieldError("deleteFormBean",
                    "label",
                    "Поле Label пустое"));
        }


        if (binding.hasErrors()) {
            return "/main/delete";
        }


        Client client = clientsDAO.findByLogin(deleteFormBean.getLogin());

        Car car = null;
        try {
            car = carsDAO.getAndCheckClientCar(client, deleteFormBean.getLabel());
        }catch(IllegalArgumentException e) {
            binding.addError(new FieldError("deleteFormBean",
                    "label",
                    " Клиенту не назначена эта машина"));
        }

        if (binding.hasErrors()) {
            return "/main/delete";
        }


        carsDAO.detachClient(car);
        clientsDAO.delete(client);

        return "redirect:/main";
    }

}
