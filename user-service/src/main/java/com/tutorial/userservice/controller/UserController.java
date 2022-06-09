package com.tutorial.userservice.controller;

import com.tutorial.userservice.entity.User;
import com.tutorial.userservice.model.Bike;
import com.tutorial.userservice.model.Car;
import com.tutorial.userservice.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAll(){
        List<User> users = userService.getAll();
        if( users.isEmpty() ){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok( users );
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById (@PathVariable int id){

        User user = userService.getUserById( id );

        if( user == null ){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok( user );
    }

    @PostMapping()
    public ResponseEntity<User> save (@RequestBody User user){

        User userNew = userService.save( user );
        return ResponseEntity.ok( userNew );
    }

    // restTemplate
    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallBackGetCars")
    @GetMapping("/cars/{userId}")
    public ResponseEntity<List<Car>> getCars(@PathVariable int userId){
        User user = userService.getUserById( userId );

        if( user == null ){
            return ResponseEntity.notFound().build();
        }

        List<Car> cars = userService.getCars( userId );
        return ResponseEntity.ok(cars);
    }
    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallBackGetBikes")
    @GetMapping("/bikes/{userId}")
    public ResponseEntity<List<Bike>> getBikes(@PathVariable int userId){
        User user = userService.getUserById( userId );

        if( user == null ){
            return ResponseEntity.notFound().build();
        }

        List<Bike> bikes = userService.getBikes( userId );
        return ResponseEntity.ok(bikes);
    }

    //FeignClient
    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallBackSaveCars")
    @PostMapping("/savecar/{userId}")
    public ResponseEntity<Car> saveCarFeign(@PathVariable("userId") int userId, @RequestBody Car car){

        if( userService.getUserById( userId ) == null ){
            return ResponseEntity.notFound().build();
        }

        Car carNew = userService.saveCar( userId, car );
        return ResponseEntity.ok(car);

    }

    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallBackSaveBikes")
    @PostMapping("/saveBike/{userId}")
    public ResponseEntity<Bike> saveBikeFeign(@PathVariable int userId,@RequestBody Bike bike){

        if( userService.getUserById( userId ) == null ){
            return ResponseEntity.notFound().build();
        }

        Bike bikeNew = userService.saveBike( userId, bike );
        return ResponseEntity.ok(bike);

    }
    @CircuitBreaker(name = "allCB", fallbackMethod = "fallBackGetAll")
    @GetMapping("/getAll/{userId}")
    public ResponseEntity<Map<String, Object>> getAllVehicles(@PathVariable("userId") int userId) {
        Map<String, Object> result = userService.getUserAndVehicles(userId);
        return ResponseEntity.ok(result);
    }


    //implements circuit
    public ResponseEntity<List<Car>> fallBackGetCars(@PathVariable int userId, RuntimeException e){
        return new ResponseEntity("El usuario "+ userId + " tiene los coches en el taller", HttpStatus.OK);
    }

    public ResponseEntity<List<Bike>> fallBackGetBikes(@PathVariable int userId){
        return new ResponseEntity("El usuario "+ userId + " no tiene dinero para carros", HttpStatus.OK);
    }

    public ResponseEntity<Car> fallBackSaveCars(@PathVariable int userId,@RequestBody Car car, RuntimeException e){
        return new ResponseEntity("El usuario "+ userId + " tiene los coches en el taller", HttpStatus.OK);
    }

    public ResponseEntity<Bike> fallBackSaveBikes(@PathVariable int userId,@RequestBody Bike bike, RuntimeException e){
        return new ResponseEntity("El usuario "+ userId + " no tiene dinero para motos", HttpStatus.OK);
    }

    public ResponseEntity<Map<String, Object>> fallBackGetAll(@PathVariable("userId") int userId, RuntimeException e){
        return new ResponseEntity("El usuario "+ userId + " tiene los vehiculos en el taller", HttpStatus.OK);
    }
}