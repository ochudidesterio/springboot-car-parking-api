package com.example.RestService.model.Mappers;

import com.example.RestService.model.Person;
import com.example.RestService.model.UserRequest;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class PersonMapper {

    public PersonMapper() {
    }

    //  @Autowired

    public Person RequestToPerson(UserRequest userRequest) {
        Person person = new Person();
        //Integer.valueOf must be not null!
        String userId = userRequest.getUserId();
        if (userId != null && userId != "") {
            person.setUserId(Integer.valueOf(userRequest.getUserId()));
        }
        person.setName(userRequest.getName());
        person.setSurname(userRequest.getSurname());
        person.setPhone(userRequest.getPhone());
        return person;
    }

    public Person ResultSetToPerson(ResultSet rs, int ronWum) throws SQLException {
        Person person = new Person();
        person.setUserId(rs.getInt("user_Id"));
        person.setName(rs.getString("name"));
        person.setSurname(rs.getString("surname"));
        person.setPhone(rs.getString("phone"));
        return person;
    }
}
