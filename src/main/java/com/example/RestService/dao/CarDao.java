package com.example.RestService.dao;

import com.example.RestService.model.Car;
import com.example.RestService.model.Person;

import java.util.List;

public interface CarDao {

    int insertPerson(Person person);

    int updatePerson(Person person);

    int insertCar(Car car);

    List<Person> findAllPersons();

    List<Car> findAllCars();

    List<Car> findPersonCarsById(int userId);

    Person findPersonById(int userId);

    Car findCarByCarNum(String carNum);

    int deletePersonById(int userId);

    int deleteCarByCarNum(String carNum);

    int updateApprovedStatusByCarNum(Car car);

    int updateActiveCar(String carNum);

    boolean isCarNumTaken(String carNum);

    boolean isUserIdTaken(int userId);

    int getUserIdByCarNum(String carNum);

}
