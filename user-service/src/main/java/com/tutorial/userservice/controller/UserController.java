package com.tutorial.userservice.controller;

import com.tutorial.userservice.entity.User;
import com.tutorial.userservice.model.Bike;
import com.tutorial.userservice.model.Car;
import com.tutorial.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/cars/{userId}")
    public ResponseEntity<List<Car>> getCars(@PathVariable int userId){
        User user = userService.getUserById( userId );

        if( user == null ){
            return ResponseEntity.notFound().build();
        }

        List<Car> cars = userService.getCars( userId );
        return ResponseEntity.ok().body( cars );
    }

    @GetMapping("/bikes/{userId}")
    public ResponseEntity<List<Bike>> getBikes(@PathVariable int userId){
        User user = userService.getUserById( userId );

        if( user == null ){
            return ResponseEntity.notFound().build();
        }

        List<Bike> bikes = userService.getBikes( userId );
        return ResponseEntity.ok().body( bikes );
    }

    // FeignClient

    @PostMapping("/savecar/{userId}")
    public ResponseEntity<Car> saveCar( @PathVariable int userId, @RequestBody Car car ){
        Car carnew = userService.saveCar( userId, car );
        return ResponseEntity.ok(car);
    }

    @PostMapping("/savebike/{userId}")
    public ResponseEntity<Bike> saveBike( @PathVariable int userId, @RequestBody Bike bike ){
        Bike bikeNew = userService.saveBike( userId, bike );
        return ResponseEntity.ok(bikeNew);
    }

    @GetMapping("/getAll/{userId}")
    public ResponseEntity<Map<String,Object>> getAllVehicles(@PathVariable("userId") int userId){
        Map<String, Object> result = userService.GetUserAndVehicles( userId );
        return ResponseEntity.ok( result );
    }
}
