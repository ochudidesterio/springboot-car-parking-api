package com.example.RestService.service;

import com.example.RestService.dao.CarDao;
import com.example.RestService.exception.ApiException;
import com.example.RestService.model.Car;
import com.example.RestService.model.Mappers.CarMapper;
import com.example.RestService.model.Mappers.PersonMapper;
import com.example.RestService.model.Person;
import com.example.RestService.model.UserRequest;
import com.example.RestService.service.validation.CarValidator;
import com.example.RestService.service.validation.PersonValidator;
import com.example.RestService.service.validation.PhoneValidator;
import com.example.RestService.service.verification.DbVerificator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CarService {

    //Interface
    private CarDao carDao;
    //Verificatiors
    private DbVerificator dbVerificator = new DbVerificator();
    //Validators
    private CarValidator carValidator = new CarValidator();
    private PersonValidator personValidator = new PersonValidator();
    private PhoneValidator phoneValidator = new PhoneValidator();
    //Mappers
    private PersonMapper personMapper = new PersonMapper();
    private CarMapper carMapper = new CarMapper();

    @Autowired
    public CarService(@Qualifier("postgres") CarDao carDao) {
        this.carDao = carDao;
        dbVerificator.carDao = carDao;
    }


    //Add a Person with Car to the database
    public Car addPerson(UserRequest userRequest) {
        log.info("-- TRYING TO ADD PERSON & CAR");

        //Validate person ID
        int userId = personValidator.isPersonIdValid(userRequest.getUserId());

        //Transform request to Person object
        Person person = personMapper.RequestToPerson(userRequest);

        //Validate phone number
        String phone = userRequest.getPhone();
        if (phone != null && phone !="") person.setPhone(phoneValidator.isPhoneIdValid(phone));

        //Transform request to Car
        Car car = carMapper.RequestToCar(userRequest);
        String carNum = car.getCarNum();

        //Check If person has MORE than 3 cars, throw an error!
        dbVerificator.verifyCarLimit(userId);

        //Vlidate car number
        carNum = carValidator.isCarNumValid(carNum);
        car.setCarNum(carNum);

        //Check if Car Number doesnt exists in database
        dbVerificator.verifyCarNumDoesntExists(carNum);


        //Check if Person ID already exists in database
        if (!carDao.isUserIdTaken(userId)) {
            //Try to add new person
            try {
                carDao.insertPerson(person);
                log.info("User [" + userId + "] has been added to table PERSON.");
            } catch (Exception e) {
                throw new ApiException(HttpStatus.UNAUTHORIZED,
                        "User [" + userId + "] already exists in database!"
                );
            }
        } else {
            // !Need to think about it
            //Update person information, if he added name/surname/mobile phone
            if (person.getName() != null && person.getSurname() != null && person.getPhone() != null) {
                try {
                    carDao.updatePerson(person);
                    log.info("User [" + userId + "] has updated its information in table PERSON.");
                } catch (Exception e) {
                    throw new ApiException(HttpStatus.UNAUTHORIZED,
                            "Cant update user [" + userId + "] information!"
                    );
                }
            }
        }

        //Try to add new car
        try {
            carDao.insertCar(car);
            log.info("Car [" + carNum + "] has been added to table CAR.");
            return car;
        } catch (Exception e) {
            throw new ApiException(HttpStatus.CONFLICT,
                    "Cant add car [" + carNum + "] to database!"
            );
        }
    }


    // Get a list of all cars from database
    public List<Car> getAllCars() {
        log.info("-- TRYING TO GET LIST OF ALL CARS");
        try {
            return carDao.findAllCars();
        } catch (Exception e) {
            throw new ApiException(HttpStatus.CONFLICT,
                    "Cant get list of all cars!"
            );
        }
    }


    // Get a list of all people from database
    public List<Person> getAllPersons() {
        log.info("-- TRYING TO GET LIST OF ALL USERS");
        try {
            return carDao.findAllPersons();
        } catch (Exception e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED,
                    "Cant get list of all users!"
            );
        }
    }


    // Get a list of all person cars by his id
    public List<Car> getPersonCarsById(String userIdS) {
        log.info("-- TRYING TO GET CAR BY ID");
        //Validate person ID
        int userId = personValidator.isPersonIdValid(userIdS);

        //verify that ID exists in database
        dbVerificator.verifyPersonExists(userId);

        //Try to get person
        try {
            return carDao.findPersonCarsById(userId);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED,
                    "Cant get user [" + userId + "] cars information!"
            );
        }
    }


    //Get a person by id
    public Person getPersonById(String userIdS) {
        log.info("-- TRYING TO GET USER BY ID");
        //Validate person ID
        int userId = personValidator.isPersonIdValid(userIdS);

        //verify that ID exists in database
        dbVerificator.verifyPersonExists(userId);

        //Try to get person
        try {
            return carDao.findPersonById(userId);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED,
                    "Cant get user [" + userId + "] information!"
            );
        }
    }


    //Delete person by id and delete all his cars
    public void deletePerson(String userIdS) {
        log.info("-- TRYING TO DELETE USER AND ALL HIS CARS");
        //Validate person ID
        int userId = personValidator.isPersonIdValid(userIdS);

        //verify that ID exists in database
        dbVerificator.verifyPersonExists(userId);

        //Try to delete person
        try {
            log.info("User [" + userId + "] and all his cars have been deleted.");
            carDao.deletePersonById(userId);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.UNAUTHORIZED,
                    "Cant delete user [" + userId + "]!"
            );
        }
    }


    //Delete car by car number
    public void deleteCar(String carNum) {
        log.info("-- TRYING TO DELETE CAR");
        //Vlidate car number
        carNum = carValidator.isCarNumValid(carNum);

        //verify that car number exists in database
        dbVerificator.verifyCarNumExists(carNum);

        //Try to delete car
        try {
            log.info("Car [" + carNum + "] has been deleted from table CAR.");
            carDao.deleteCarByCarNum(carNum);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.CONFLICT,
                    "Can't delete car [" + carNum + "] from database!"
            );
        }
    }

    //Mark car as APPROVED when reception guys added car to their system
    public void updateApprovedStatusByCarNum(UserRequest userRequest) {
        log.info("-- TRYING TO UPDATE APPROVED STATUS");
        //Transform request to Car
        Car car = carMapper.RequestToCar(userRequest);
        String carNum = car.getCarNum();

        //Vlidate car number
        carNum = carValidator.isCarNumValid(carNum);
        car.setCarNum(carNum);

        //verify that car number exists in database
        dbVerificator.verifyCarNumExists(carNum);

        //Try to approve car
        try {
            carDao.updateApprovedStatusByCarNum(car);
            log.info("Car [" + carNum + "]. Approved status has been set to " + car.getApprovedStatus() + ".");
        } catch (Exception e) {
            throw new ApiException(HttpStatus.CONFLICT,
                    "Can't approve car [" + carNum + "]!"
            );
        }
        

        //If person has only one car, than automatically set it as ACTIVE CAR
        try {
            int userId = carDao.getUserIdByCarNum(carNum);//car.getUserId();
            int count = carDao.findPersonCarsById(userId).size();
            if (count == 1) {
                log.info("-- TRYING TO SET ACTIVE CAR");
                carDao.updateActiveCar(carNum);
                log.info("New Active car [" + carNum + "] for user [" + userId + "].");
            }
        } catch (Exception e) {
            throw new ApiException(HttpStatus.CONFLICT,
                    "Can't update status for car [" + carNum + "]!"
            );
        }
    }


    //Set car as active car
    public void updateActiveCar(String carNum) {
        log.info("-- TRYING TO SET ACTIVE CAR");
        //Vlidate car number
        carNum = carValidator.isCarNumValid(carNum);

        //verify that car number exists in database
        dbVerificator.verifyCarNumExists(carNum);

        //verify that car is approved by the reception
        dbVerificator.verifyCarApproved(carNum);

        //verify that car isn't already set to Active car
        dbVerificator.verifyCarActive(carNum);

        //Try to set car as active
        try {
            carDao.updateActiveCar(carNum);
            log.info("New Active car: [" + carNum + "].");
        } catch (Exception e) {
            throw new ApiException(HttpStatus.CONFLICT,
                    "Can't update status for car [" + carNum + "]!"
            );
        }
    }


    // Get a one car information by its number
    public Car getCarByCarNum(String carNum) {
        log.info("-- TRYING TO GET CAR BY CAR NUMBER");
        //Vlidate car number
        carNum = carValidator.isCarNumValid(carNum);

        //Verify that Car Number exists in database
        dbVerificator.verifyCarNumExists(carNum);

        //Try to set car as active
        try {
            return carDao.findCarByCarNum(carNum);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.CONFLICT,
                    "Can't get car [" + carNum + "]!"
            );
        }
    }
}
