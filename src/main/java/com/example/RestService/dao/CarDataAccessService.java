package com.example.RestService.dao;
//This class is used for postgres implementation. To connect to another data base with changing
//just one line of code
//Dependency injection

import com.example.RestService.model.Car;
import com.example.RestService.model.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Repository("postgres") //To change implementation all we have to do to imput this name in Server
public class CarDataAccessService implements CarDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CarDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertPerson(Person person) {
        String sql = "INSERT INTO person (" +
                "user_Id," +
                "name, " +
                "surname, " +
                "phone) " +
                "VALUES (?, ?, ?, ?)";
        int userId = person.getUserId();
        String name = person.getName();
        String surname = person.getSurname();
        String phone = person.getPhone();
        return jdbcTemplate.update(
                sql,
                userId,
                name,
                surname,
                phone
        );
    }

    @Override
    public int insertCar(Car car) {
        String sql = "INSERT INTO car (" +
                "user_Id, " +
                "active, " +
                "car_Num, " +
                "car_Added_Time, " +
                "approved_Status, " +
                "car_Approved_Time) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        int userId = car.getUserId();
        String carNum = car.getCarNum();
        Boolean active = false;
        Boolean approvedStatus = false;
        Timestamp carAddedTime = new Timestamp(System.currentTimeMillis());//person.getCarAddedTime();
        Timestamp carApprovedTime = null;//person.getCarApprovedTime();
        log.debug(String.valueOf(carAddedTime));
        return jdbcTemplate.update(
                sql,
                userId,
                active,
                carNum,
                carAddedTime,
                approvedStatus,
                carApprovedTime
        );

    }

    @Override
    public int updatePerson(Person newPerson) {
        String sql = "" +
                "UPDATE person " +
                "SET name = ? , " +
                "surname = ?, " +
                "phone = ? " +
                "WHERE user_Id = ?";
        int userId = newPerson.getUserId();
        String name = newPerson.getName();
        String surname = newPerson.getSurname();
        String phone = newPerson.getPhone();
        return jdbcTemplate.update(sql, name, surname, phone, userId);
    }


    @Override
    public List<Car> findAllCars() {
        final String sql = "" +
                "SELECT " +
                "user_Id, " +
                "active, " +
                "car_Num, " +
                "car_Added_Time, " +
                "approved_Status, " +
                "car_Approved_Time " +
                "FROM car";
        return jdbcTemplate.query(
                sql,
                (resultSet, i) -> {
                    Car car = new Car();
                    car.setUserId(resultSet.getInt("user_Id"));
                    car.setCarNum(resultSet.getString("car_Num"));
                    car.setActive(resultSet.getBoolean("active"));
                    car.setCarAddedTime(resultSet.getTimestamp("car_Added_Time"));
                    car.setApprovedStatus(resultSet.getBoolean("approved_Status"));
                    car.setCarApprovedTime(resultSet.getTimestamp("car_Approved_Time"));
                    return car;
                });
    }


    @Override
    public List<Person> findAllPersons() {
        final String sql = "" +
                "SELECT " +
                "user_Id, " +
                "name, " +
                "surname, " +
                "phone " +
                "FROM person";
        return jdbcTemplate.query(
                sql,
                (resultSet, i) -> {
                    Person person = new Person();
                    person.setUserId(resultSet.getInt("user_Id"));
                    person.setName(resultSet.getString("name"));
                    person.setSurname(resultSet.getString("surname"));
                    person.setPhone(resultSet.getString("phone"));
                    return person;
                });
    }


    @Override
    public List<Car> findPersonCarsById(int userId) {
        final String sql = "" +
                "SELECT " +
                "user_Id, " +
                "active, " +
                "car_Num, " +
                "car_Added_Time, " +
                "approved_Status, " +
                "car_Approved_Time " +
                "FROM car " +
                "WHERE user_Id = ?";
        return jdbcTemplate.query(
                sql,
                new Object[]{userId},
                (resultSet, i) -> {
                    Car car = new Car();
                    car.setUserId(resultSet.getInt("user_Id"));
                    car.setCarNum(resultSet.getString("car_Num"));
                    car.setActive(resultSet.getBoolean("active"));
                    car.setCarAddedTime(resultSet.getTimestamp("car_Added_Time"));
                    car.setApprovedStatus(resultSet.getBoolean("approved_Status"));
                    car.setCarApprovedTime(resultSet.getTimestamp("car_Approved_Time"));
                    return car;
                });
    }


    @Override
    public Person findPersonById(int userId) {
        final String sql = "" +
                "SELECT " +
                "user_Id, " +
                "name, " +
                "surname, " +
                "phone " +
                "FROM person " +
                "WHERE user_Id = ?";
        // return jdbcTemplate.queryForObject(sql, new Object[]{userId}, new ResultSetToPerson());
        return (Person) jdbcTemplate.queryForObject(
                sql,
                new Object[]{userId},
                new BeanPropertyRowMapper<>(Person.class));
    }


    @Override
    public int deletePersonById(int userId) {
        String sql = "" +
                "DELETE FROM car " +
                "WHERE user_Id = ?";
        jdbcTemplate.update(sql, userId);
        sql = "" +
                "DELETE FROM person " +
                "WHERE user_Id = ?";
        return jdbcTemplate.update(sql, userId);
    }

    @Override
    public int deleteCarByCarNum(String carNum) {
        String sql = "" +
                "DELETE FROM car " +
                "WHERE car_num = ?";
        return jdbcTemplate.update(sql, carNum);
    }

    @Override
    public int updateApprovedStatusByCarNum(Car car) {
        String sql = "" +
                "UPDATE car " +
                "SET approved_Status = ? , " +
                "car_Approved_Time = ? " +
                "WHERE car_Num = ?";
        String carNum = car.getCarNum();
        Boolean approvedStatus = car.getApprovedStatus();
        Timestamp carApprovedTime = approvedStatus ? new Timestamp(System.currentTimeMillis()) : null;
        return jdbcTemplate.update(sql, approvedStatus, carApprovedTime, carNum);
    }

    @Override
    public int updateActiveCar(String carNum) {

        //Get user id
        int userId = getUserIdByCarNum(carNum);
        //Set all persons cars active status to FALSE
        String sql = "" +
                "UPDATE car " +
                "SET active = false " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
        //Set choosen car active status to true
        sql = "" +
                "UPDATE car " +
                "SET active = true " +
                "WHERE car_num = ?";
        return jdbcTemplate.update(sql, carNum);
    }

    @Override
    public int getUserIdByCarNum(String carNum) {
        String sql = "" +
                "SELECT user_id " +
                "FROM car " +
                "WHERE car_num = ?";
        return (int) jdbcTemplate.queryForObject(
                sql, new Object[]{carNum}, int.class);
    }

    @SuppressWarnings("ConstantConditions")
    public boolean isCarNumTaken(String carNum) {
        String sql = "" +
                "SELECT EXISTS ( " +
                " SELECT 1 " +
                " FROM car " +
                " WHERE car_Num = ?" +
                ")";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{carNum},
                (resultSet, i) -> resultSet.getBoolean(1)
        );
    }

    @SuppressWarnings("ConstantConditions")
    public boolean isUserIdTaken(int userId) {
        String sql = "" +
                "SELECT EXISTS ( " +
                " SELECT 1 " +
                " FROM person " +
                " WHERE user_Id = ?" +
                ")";
        return jdbcTemplate.queryForObject(
                sql,
                new Object[]{userId},
                (resultSet, i) -> resultSet.getBoolean(1)
        );
    }

    @Override
    public Car findCarByCarNum(String carNum) {
        final String sql = "" +
                "SELECT " +
                "car_Num, " +
                "user_Id, " +
                "active, " +
                "approved_Status, " +
                "car_Added_Time, " +
                "car_Approved_Time " +
                "FROM car " +
                "WHERE car_num = ?";
        return (Car) jdbcTemplate.queryForObject(
                sql,
                new Object[]{carNum},
                new BeanPropertyRowMapper<>(Car.class));
    }
}

