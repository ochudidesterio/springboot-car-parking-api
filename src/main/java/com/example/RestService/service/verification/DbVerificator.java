package com.example.RestService.service.verification;

import com.example.RestService.dao.CarDao;
import com.example.RestService.exception.ApiException;
import org.springframework.http.HttpStatus;

public class DbVerificator {
    public CarDao carDao;

    public DbVerificator() {
    }

    //Check if ID already exists in database
    public void verifyPersonExists(int userId) {
        if (!carDao.isUserIdTaken(userId)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED,
                    "User [" + userId + "] doesn't exists in database!"
            );
        }
    }

    //Check if Car Number exists in database
    public void verifyCarNumExists(String carNum) {
        if (!carDao.isCarNumTaken(carNum)) {
            throw new ApiException(HttpStatus.CONFLICT,
                    "Car number [" + carNum + "] doesn't exist in database!"
            );
        }
    }

    //Check if Car Number exists in database
    public void verifyCarNumDoesntExists(String carNum) {
        if (carDao.isCarNumTaken(carNum)) {
            throw new ApiException(HttpStatus.FORBIDDEN,
                    "Car number [" + carNum + "] already exist in database!"
            );
        }
    }

    //Check if Car Number is Approved
    public void verifyCarApproved(String carNum) {
        Boolean approvedStatus = carDao.findCarByCarNum(carNum).getApprovedStatus();
        if (!approvedStatus) {
            throw new ApiException(HttpStatus.FORBIDDEN,
                    "Car [" + carNum + "] is waiting for approval by the reception!"
            );
        }
    }

    //Check if Car Number is Active
    public void verifyCarActive(String carNum) {
        Boolean active = carDao.findCarByCarNum(carNum).getActive();
        if (active) {
            throw new ApiException(HttpStatus.FORBIDDEN,
                    "Car [" + carNum + "] is already Active!"
            );
        }
    }

    //Check If person has MORE than 3 cars, throw an error!
    public void verifyCarLimit(int userId) {
        int count = carDao.findPersonCarsById(userId).size();
        if (count >= 3) {
            throw new ApiException(HttpStatus.NOT_ACCEPTABLE,
                    "You can add only 3 cars! P.S. You don't need a fourth car... Better give it to us :) "
            );
        }
    }
}
