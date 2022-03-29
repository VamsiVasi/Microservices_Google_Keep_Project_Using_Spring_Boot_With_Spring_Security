package com.albanero.User_Microservice_.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.albanero.User_Microservice_.Model.Users_;

@Repository
public interface User_Repo_ extends MongoRepository<Users_, String> {

	Optional<Users_> findByUserName(String userName);

	List<Users_> findByRole(String string);

}
