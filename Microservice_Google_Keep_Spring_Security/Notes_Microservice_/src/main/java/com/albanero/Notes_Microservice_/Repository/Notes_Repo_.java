package com.albanero.Notes_Microservice_.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.albanero.Notes_Microservice_.Model.Notes_;

@Repository
public interface Notes_Repo_ extends MongoRepository<Notes_, String> {

	Optional<Notes_> findByName(String name);

}
