package com.example.RestService.model.Mappers;

import com.example.RestService.model.Car;
import com.example.RestService.model.UserRequest;
import org.springframework.stereotype.Service;

@Service
public class CarMapper {
    public Car RequestToCar(UserRequest userRequest) {
        Car car = new Car();
        //Integer.valueOf must be not null!
        String userId = userRequest.getUserId();
        if (userId != null && userId != "") {
            car.setUserId(Integer.valueOf(userId));
        }
        car.setCarNum(userRequest.getCarNum());
        car.setApprovedStatus(userRequest.getApprovedStatus());
        car.setApprovedStatus(userRequest.getApprovedStatus());

        return car;
    }
}
