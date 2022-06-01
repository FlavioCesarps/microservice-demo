package com.tutorial.userservice.service;

import com.tutorial.userservice.entity.User;
import com.tutorial.userservice.feignClient.BikeFeignClient;
import com.tutorial.userservice.feignClient.CarFeignClient;
import com.tutorial.userservice.model.Bike;
import com.tutorial.userservice.model.Car;
import com.tutorial.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CarFeignClient carFeignClient;

    @Autowired
    BikeFeignClient bikeFeignClient;

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getUserById( int id ){
        return userRepository.findById( id ).orElse(null);
    }

    public User save( User user ){
        User userNew = userRepository.save( user );
        return userNew;
    }

    // Rest Template
    @GetMapping("/{userId}")
    public List<Car> getCars( int userId ){
        List<Car> cars = restTemplate.getForObject("http://localhost:8002/cars/byuser/" + userId, List.class);
        return cars;
    }

    @GetMapping("/{userId}")
    public List<Bike> getBikes( int userId ){
        List<Bike> bikes = restTemplate.getForObject("http://localhost:8003/bikes/byuser/" + userId, List.class);
        return bikes;
    }

    // Feign Client
    public Car saveCar( int userId, Car car ){
        car.setUserId( userId );

        Car carNew = carFeignClient.save( car );
        return carNew;
    }

    public Bike saveBike( int userId, Bike bike ){
        bike.setUserId( userId );

        Bike bikeNew = bikeFeignClient.save( bike );
        return bikeNew;
    }

    public Map<String, Object> GetUserAndVehicles( int userId ){
        Map<String, Object> result = new HashMap<>();

        User user = userRepository.findById( userId ).orElse( null );
        if( user==null ) {
            result.put("Message", "Not exists the user");
            return result;
        }
        result.put( "User", user );

        List<Car> cars = carFeignClient.getCars( userId );
        if( cars.isEmpty() ){
            result.put("Cars", "This user not have coches");
        }else{
            result.put("Cars", cars);
        }

        List<Bike> bikes = bikeFeignClient.getBikes( userId );
        if( bikes.isEmpty() ){
            result.put("Bikes", "Thi user not have motos");
        }else{
            result.put("Bikes", bikes);
        }

        return result;
    }
}
