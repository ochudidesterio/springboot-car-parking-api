package com.example.RestService.service.validation;

import com.example.RestService.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Slf4j
@Component
public class CarValidator {

    public CarValidator() {
    }

    private Predicate<String> IS_CARNUM_REGULAR_VALID =
            Pattern.compile(
                    //regular car numbers
                    //Matches: KS-4444; ks333; KS 1; A1; KS - 22
                    //Non-Matches: AA; 123; CUSTOM;
                    "^[a-z,A-Z]{1,2}\\s?[-]{0,1}\\s?[0-9]{1,4}$"
            ).asPredicate();

    private Predicate<String> IS_CARNUM_INDIVIDUAL_VALID =
            Pattern.compile(
                    //individual car numbers
                    // 2-8 symbols:                             Matches: CUSTOM     Non-Matches: A; LATVIETIS
                    // 1 char can't repeat more than 3 times:   Matches: HELLLO     Non-Matches: GOOOOOOD
                    // spaces allowed:                          Matches: YO MAN     Non-Matches YO   MAN
                    // can't contain only numbers:              Matches: A1; 1A     Non-Mathces: 11; 12345

                    //Matches: SLOW; MORGAN S; LAVASH;
                    "^(?=.*[a-zA-Z].*)(?:([\\w\\s])(?!.*\\1\\1\\1)(?!\\s{2})){2,8}$"
                    //OLD: "^(?:(?=.*[a-zA-Z].*)([\\w\\s])(?!.*\\1\\1\\1)){2,8}$"
            ).asPredicate();

    //Check car number validation with tests as a 'Regular Car Number' and
    // as a 'Individual Car Number'. In case if both tests are passed,
    // perform car number to the one common pattern.
    public String isCarNumValid(String carNum) {
        //Check for regular car numbers
        Boolean result_regular = IS_CARNUM_REGULAR_VALID.test(carNum);
        //Check for individual car numbers
        Boolean result_individual = IS_CARNUM_INDIVIDUAL_VALID.test(carNum);

        //Logs
        if (result_regular)
            log.info("Car number [" + carNum + "] validation test passed as a 'Regular Car Number'.");
        else if (result_individual) {
            log.warn("Attention! Your car number [" + carNum + "] haven't passed validation test for a 'Regular Car Number'.");
            log.info("Car number [" + carNum + "] validation test passed as a 'Individual Car Number'.");
        }

        //Error if both tests haven't passed
        if (!result_regular && !result_individual) {
            throw new ApiException(HttpStatus.EXPECTATION_FAILED,
                    "Car num validation tests haven't passed."
            );
        }

        //Perform Car Number to the common pattern (Example: From [KS - 8899] to [KS8899])
        String validatedCarNum = carNum;
        if (result_regular) {
            validatedCarNum = validatedCarNum.replaceAll("[-\\s]", "").toUpperCase();
            log.info("Car number performed from [" + carNum + "] to [" + validatedCarNum + "].");
        } else if (result_individual) {
            validatedCarNum = validatedCarNum.toUpperCase();
            log.info("Car number performed from [" + carNum + "] to [" + validatedCarNum + "].");
        }

        return validatedCarNum;
    }
}