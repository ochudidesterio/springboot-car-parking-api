package com.example.RestService.api;

import com.example.RestService.model.Car;
import com.example.RestService.model.Person;
import com.example.RestService.model.UserRequest;
import com.example.RestService.service.CarService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RequestMapping("api/car_park") //@RequestMapping shortcut for class
@RestController
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    //Add a Person with Car to the database
    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public Car addPerson(@Valid @NonNull @RequestBody UserRequest userRequest) {
        return carService.addPerson(userRequest);
    }

    //Get a list of all cars from database
    @GetMapping(path = "/car/all")
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    //Get a list of all people from database
    @GetMapping(path = "/person/all")
    public List<Person> getAllPersons() {
        return carService.getAllPersons();
    }

    //Get a list of all person cars by his id
    @GetMapping(path = "/car/{userId}/all")
    //(path = "{id}") - means that we want the actual ID in the path
    public List<Car> getPersonCarsById(@PathVariable String userId) {
        return carService.getPersonCarsById(userId);
    }

    //Get a car by car number
    @GetMapping(path = "/car/person/{carNum}")
    //(path = "{id}") - means that we want the actual ID in the path
    public Car getCarNyCarNum(@PathVariable String carNum) {
        return carService.getCarByCarNum(carNum);
    }

    //Get a person by id
    @GetMapping(path = "/person/{id}")
    //(path = "{id}") - means that we want the actual ID in the path
    public Person getPersonById(@PathVariable("id") String id) {
        return carService.getPersonById(id);
    }

    //Delete person by id and delete his cars
    @DeleteMapping(path = "/person/{id}")
    public void deletePersonById(@PathVariable("id") String id) {
        carService.deletePerson(id);
    }

    //Delete car by carNum
    @DeleteMapping(path = "/car/{carNum}")
    public void deleteCarByCarNum(@PathVariable("carNum") String carNum) {
        carService.deleteCar(carNum);
    }

    //Mark car as APPROVED when reception guys added car to their system
    @PutMapping(path = "car/approve")
    public void updateApprovedStatusByCarNum(
           // @PathVariable("carNum") String carNum,
            @RequestBody UserRequest userRequestToUpdate) {
        carService.updateApprovedStatusByCarNum(userRequestToUpdate);
    }

    //Set car as active car
    @PutMapping(path = "car/{carNum}/set_active")
    public void updateActiveCar(@PathVariable("carNum") String carNum) { carService.updateActiveCar(carNum);}



}