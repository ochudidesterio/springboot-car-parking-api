package com.example.RestService.service.validation;

import com.example.RestService.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Slf4j
@Component
public class PhoneValidator {

    public PhoneValidator() {
    }

    private Predicate<String> IS_ID_VALID =
            Pattern.compile(
                    //Workers phone
                    //Matches: 88888888, +37188888888, +(371) 88888888, +371 88888888
                    //Non matches 112, others...
                    "^(?:[()+\\d]{0,6})\\s?(?:[\\d]){8}$"
            ).asPredicate();

    //Check person phone validation with test
    //In case if test hasn't passed throws an error
    public String isPhoneIdValid(String phone) {
        //Check for regular car numbers
        Boolean result = IS_ID_VALID.test(phone);

        //Logs
        if (result)
            log.info("Phone [" + phone + "] validation test passed.");

        //Error if test haven't passed
        if (!result) {
            throw new ApiException(HttpStatus.LOCKED,
                    "Phone [" + phone + "] validation tests haven't passed."
            );
        }

        //Perform Phone to the common pattern (Example: From [+371 88888888] to [88888888])
        String validatedPhone = phone.substring(phone.length() - 8);
        log.info("Phone performed from [" + phone + "] to [" + validatedPhone + "].");

        return validatedPhone;
    }
}