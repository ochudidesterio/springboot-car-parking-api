package com.example.RestService.service.validation;

import com.example.RestService.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.regex.Pattern;


@Slf4j
@Component
public class PersonValidator {
   // private Logger log;

    public PersonValidator() {
    }

    private Predicate<String> IS_ID_VALID =
            Pattern.compile(
                    //Workers ID
                    //Matches: 666666; 1; 22;
                    //Cant start with 0; Cant be negative; 1-6 digits;
                    //Non matches 055555; -10; 0; 7777777
                    "^([1-9])(\\d{1,5})?$"
            ).asPredicate();

    //Check person id validation with test
    //In case if test hasn't passed throws an error
    public int isPersonIdValid(String id) {
        //Check for regular car numbers
        Boolean result = IS_ID_VALID.test(id);

        //Logs
        if (result)

            log.info("Person id [" + id + "] validation test passed.");

        //Error if test haven't passed
        if (!result) {
            throw new ApiException(HttpStatus.NON_AUTHORITATIVE_INFORMATION,
                    "Person id [" + id + "] validation tests haven't passed."
            );
        }
        return Integer.valueOf(id);
    }
}